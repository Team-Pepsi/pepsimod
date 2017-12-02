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

        updateMS();
        if (hasTimePassedM(500)) {
            updateLastMS();
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
        updateLastMS();
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[0];
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.COMBAT;
    }
}
