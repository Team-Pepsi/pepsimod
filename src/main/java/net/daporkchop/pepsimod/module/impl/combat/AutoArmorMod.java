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
import net.daporkchop.pepsimod.module.api.TimeModule;
import net.daporkchop.pepsimod.the.wurst.pkg.name.WPlayerController;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class AutoArmorMod extends TimeModule {
    public static AutoArmorMod INSTANCE;

    {
        INSTANCE = this;
    }

    public AutoArmorMod() {
        super("AutoArmor");
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void tick() {
        if (mc.player.capabilities.isCreativeMode) {
            return;
        }

        this.updateMS();
        if (this.hasTimePassedM(500)) {
            this.updateLastMS();
            int[] bestArmorValues = new int[4];
            for (int type = 0; type < 4; type++) {
                ItemStack oldArmor = mc.player.inventory.armorItemInSlot(type);
                if (oldArmor.getItem() instanceof ItemArmor) {
                    bestArmorValues[type] = ((ItemArmor) oldArmor.getItem()).damageReduceAmount;
                }
            }
            int[] bestArmorSlots = {-1, -1, -1, -1};
            for (int slot = 0; slot < 36; slot++) {
                ItemStack stack = mc.player.inventory.getStackInSlot(slot);
                if (stack.getItem() instanceof ItemArmor) {
                    ItemArmor armor = (ItemArmor) stack.getItem();
                    int type = PepsiUtils.getArmorType(armor);
                    if (armor.damageReduceAmount > bestArmorValues[type]) {
                        bestArmorValues[type] = armor.damageReduceAmount;
                        bestArmorSlots[type] = slot;
                    }
                }
            }
            for (int type = 0; type < 4; type++) {
                int slot = bestArmorSlots[type];
                if (slot != -1) {
                    WPlayerController.windowClick_PICKUP(slot < 9 ? 36 + slot : slot);
                    WPlayerController.windowClick_PICKUP(8 - type);
                    WPlayerController.windowClick_PICKUP(slot < 9 ? 36 + slot : slot);
                }
            }
        }
    }

    @Override
    public void init() {
        INSTANCE = this;
        this.updateLastMS();
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[0];
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.COMBAT;
    }
}
