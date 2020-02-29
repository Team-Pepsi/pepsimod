/*
 * Adapted from The MIT License (MIT)
 *
 * Copyright (c) 2016-2020 DaPorkchop_
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * Any persons and/or organizations using this software must include the above copyright notice and this permission notice,
 * provide sufficient credit to the original authors of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.daporkchop.pepsimod.module.impl.combat;

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.OptionCompletions;
import net.daporkchop.pepsimod.module.api.TimeModule;
import net.daporkchop.pepsimod.module.api.option.ExtensionSlider;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;
import net.daporkchop.pepsimod.the.wurst.pkg.name.BlockUtils;
import net.daporkchop.pepsimod.util.config.impl.BedBomberTranslator;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

public class BedBomberMod extends TimeModule {
    public static BedBomberMod INSTANCE;
    private static BlockUtils.BlockValidator validator =
            (pos) -> {
                IBlockState state = mc.world.getBlockState(pos);
                return state.getBlock() instanceof BlockBed;
            };
    public int itemMoveTick = 3, bedSlot = -1;
    private int itemTimer;
    private boolean shouldRestock = false;

    {
        INSTANCE = this;
    }

    public BedBomberMod() {
        super("BedBomber");
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void tick() {
        this.updateMS();

        if (this.hasTimePassedM(BedBomberTranslator.INSTANCE.delay) && (mc.player.dimension == -1 || mc.player.dimension == 1)) {
            Iterable<BlockPos> validBlocks = BlockUtils.getValidBlocksByDistance(BedBomberTranslator.INSTANCE.range, false, validator);

            for (BlockPos pos : validBlocks) {
                if (BlockUtils.rightClickBlockLegit(pos)) {
                    return;
                }
            }
        }

        this.replaceBed(-1);

        if (this.shouldRestock && BedBomberTranslator.INSTANCE.resupply && this.itemMoveTick == 3) {
            if (this.itemTimer > 0) {
                this.itemTimer--;
                return;
            }

            ItemStack hand = mc.player.getHeldItem(EnumHand.MAIN_HAND);
            NonNullList<ItemStack> inv = mc.player.inventory.mainInventory;

            if (hand == null || hand.isEmpty()) {
                for (int inventoryIndex = 0; inventoryIndex < inv.size(); inventoryIndex++) {
                    if (inventoryIndex != mc.player.inventory.currentItem) {
                        ItemStack stack = inv.get(inventoryIndex);
                        if (!stack.isEmpty() && stack.getItem() instanceof ItemBed) {
                            this.replaceBed(inventoryIndex);
                            break;
                        }
                    }
                }
                this.shouldRestock = false;
            }
        }
    }

    @Override
    public void init() {
        INSTANCE = this;
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new ModuleOption<>(BedBomberTranslator.INSTANCE.range, "range", OptionCompletions.FLOAT,
                        (value) -> {
                            BedBomberTranslator.INSTANCE.range = Math.max(value, 0);
                            return true;
                        },
                        () -> {
                            return BedBomberTranslator.INSTANCE.range;
                        }, "Range", new ExtensionSlider(ExtensionType.VALUE_FLOAT, 0.0f, 10.0f, 0.5f)),
                new ModuleOption<>(BedBomberTranslator.INSTANCE.delay, "delay", OptionCompletions.FLOAT,
                        (value) -> {
                            BedBomberTranslator.INSTANCE.delay = Math.max(value, 0);
                            return true;
                        },
                        () -> {
                            return BedBomberTranslator.INSTANCE.delay;
                        }, "Delay", new ExtensionSlider(ExtensionType.VALUE_INT, 0, 5000, 50)),
                new ModuleOption<>(BedBomberTranslator.INSTANCE.resupply, "resupply", OptionCompletions.BOOLEAN,
                        (value) -> {
                            BedBomberTranslator.INSTANCE.resupply = value;
                            return true;
                        },
                        () -> {
                            return BedBomberTranslator.INSTANCE.resupply;
                        }, "Resupply")
        };
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.COMBAT;
    }

    public void replaceBed(int inventoryIndex) {
        if (inventoryIndex == -1) {
            inventoryIndex = this.bedSlot;
        } else {
            this.itemMoveTick = 0;
            this.bedSlot = inventoryIndex;
        }
        if (inventoryIndex == -1) {
            return;
        }
        switch (this.itemMoveTick) {
            case 0:
                mc.playerController.windowClick(0, inventoryIndex < 9 ? inventoryIndex + 36 : inventoryIndex, 0, ClickType.PICKUP, mc.player);
                break;
            case 1:
                mc.playerController.windowClick(0, 36 + mc.player.inventory.currentItem, 0, ClickType.PICKUP, mc.player);
                break;
            case 2:
                mc.playerController.windowClick(0, inventoryIndex < 9 ? inventoryIndex + 36 : inventoryIndex, 0, ClickType.PICKUP, mc.player);
                this.bedSlot = -1;
                break;
        }
        this.itemMoveTick++;
    }

    public void onPlaceBed() {
        if (this.state.enabled && BedBomberTranslator.INSTANCE.resupply) {
            this.shouldRestock = true;
            this.itemTimer = 3;
        }
    }
}
