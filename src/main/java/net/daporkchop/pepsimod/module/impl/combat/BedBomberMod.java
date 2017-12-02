/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017 Team Pepsi
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from Team Pepsi.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: Team Pepsi), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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

    {
        INSTANCE = this;
    }

    private static BlockUtils.BlockValidator validator =
            (pos) -> {
                IBlockState state = mc.world.getBlockState(pos);
                return state.getBlock() instanceof BlockBed;
            };
    public int itemMoveTick = 3, bedSlot = -1;
    private int itemTimer;
    private boolean shouldRestock = false;

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
        updateMS();

        if (hasTimePassedM(BedBomberTranslator.INSTANCE.delay) && (mc.player.dimension == -1 || mc.player.dimension == 1)) {
            Iterable<BlockPos> validBlocks = BlockUtils.getValidBlocksByDistance(BedBomberTranslator.INSTANCE.range, false, validator);

            for (BlockPos pos : validBlocks) {
                if (BlockUtils.rightClickBlockLegit(pos)) {
                    return;
                }
            }
        }

        replaceBed(-1);

        if (shouldRestock && BedBomberTranslator.INSTANCE.resupply && itemMoveTick == 3) {
            if (itemTimer > 0) {
                itemTimer--;
                return;
            }

            ItemStack hand = mc.player.getHeldItem(EnumHand.MAIN_HAND);
            NonNullList<ItemStack> inv = mc.player.inventory.mainInventory;

            if (hand == null || hand.isEmpty()) {
                for (int inventoryIndex = 0; inventoryIndex < inv.size(); inventoryIndex++) {
                    if (inventoryIndex != mc.player.inventory.currentItem) {
                        ItemStack stack = inv.get(inventoryIndex);
                        if (!stack.isEmpty() && stack.getItem() instanceof ItemBed) {
                            replaceBed(inventoryIndex);
                            break;
                        }
                    }
                }
                shouldRestock = false;
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
            inventoryIndex = bedSlot;
        } else {
            itemMoveTick = 0;
            bedSlot = inventoryIndex;
        }
        if (inventoryIndex == -1) {
            return;
        }
        switch (itemMoveTick) {
            case 0:
                mc.playerController.windowClick(0, inventoryIndex < 9 ? inventoryIndex + 36 : inventoryIndex, 0, ClickType.PICKUP, mc.player);
                break;
            case 1:
                mc.playerController.windowClick(0, 36 + mc.player.inventory.currentItem, 0, ClickType.PICKUP, mc.player);
                break;
            case 2:
                mc.playerController.windowClick(0, inventoryIndex < 9 ? inventoryIndex + 36 : inventoryIndex, 0, ClickType.PICKUP, mc.player);
                bedSlot = -1;
                break;
        }
        itemMoveTick++;
    }

    public void onPlaceBed() {
        if (state.enabled && BedBomberTranslator.INSTANCE.resupply) {
            shouldRestock = true;
            itemTimer = 3;
        }
    }
}
