/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2018 DaPorkchop_
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from DaPorkchop_.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original author of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.daporkchop.pepsimod.module.impl.player;

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.OptionCompletions;
import net.daporkchop.pepsimod.module.api.option.ExtensionSlider;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;
import net.daporkchop.pepsimod.util.ReflectionStuff;
import net.daporkchop.pepsimod.util.config.impl.AutoEatTranslator;
import net.minecraft.block.BlockContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;

public class AutoEatMod extends Module {
    public static AutoEatMod INSTANCE;
    public boolean doneEating = true;

    {
        INSTANCE = this;
    }

    public AutoEatMod() {
        super("AutoEat");
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        if (mc.world != null) {
            ReflectionStuff.setPressed(mc.gameSettings.keyBindUseItem, false);
        }
        this.doneEating = true;
    }

    @Override
    public void tick() {
        if (!this.shouldEat()) {
            ReflectionStuff.setPressed(mc.gameSettings.keyBindUseItem, false);
            this.doneEating = true;
            return;
        }

        FoodStats foodStats = mc.player.getFoodStats();
        if (foodStats.getFoodLevel() <= AutoEatTranslator.INSTANCE.threshold && this.shouldEat()) {
            this.doneEating = false;
            this.eatFood();
        }
    }

    @Override
    public void init() {
        INSTANCE = this;
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new ModuleOption<>(AutoEatTranslator.INSTANCE.threshold, "threshold", OptionCompletions.FLOAT,
                        (value) -> {
                            AutoEatTranslator.INSTANCE.threshold = Math.max(0, value);
                            return true;
                        },
                        () -> {
                            return AutoEatTranslator.INSTANCE.threshold;
                        }, "Threshold", new ExtensionSlider(ExtensionType.VALUE_FLOAT, 0f, 19f, 1f))
        };
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.PLAYER;
    }

    private void eatFood() {
        for (int slot = 44; slot >= 9; slot--) {
            ItemStack stack = mc.player.inventoryContainer.getSlot(slot).getStack();

            if (stack != null) {
                if (slot >= 36 && slot <= 44) {
                    if (stack.getItem() instanceof ItemFood
                            && !(stack.getItem() instanceof ItemAppleGold)) {
                        mc.player.inventory.currentItem = slot - 36;
                        ReflectionStuff.setPressed(mc.gameSettings.keyBindUseItem, true);
                        return;
                    }
                } else if (stack.getItem() instanceof ItemFood
                        && !(stack.getItem() instanceof ItemAppleGold)) {
                    int itemSlot = slot;
                    int currentSlot = mc.player.inventory.currentItem + 36;
                    mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(0, currentSlot, 0, ClickType.PICKUP, mc.player);
                    mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, mc.player);
                    return;
                }
            }
        }
    }

    private boolean shouldEat() {
        if (!mc.player.canEat(false)) {
            return false;
        }

        if (mc.currentScreen != null) {
            return false;
        }

        if (mc.currentScreen == null && mc.objectMouseOver != null) {
            Entity entity = mc.objectMouseOver.entityHit;
            if (entity instanceof EntityVillager || entity instanceof EntityTameable) {
                return false;
            }

            if (mc.objectMouseOver.getBlockPos() != null && mc.world.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock() instanceof BlockContainer) {
                return false;
            }
        }

        return true;
    }
}
