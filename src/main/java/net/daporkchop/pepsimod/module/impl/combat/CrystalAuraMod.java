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

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.OptionCompletions;
import net.daporkchop.pepsimod.module.api.option.ExtensionSlider;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.util.EnumHand;

/**
 * #totallyNotSkidded
 */
public class CrystalAuraMod extends Module {
    public static CrystalAuraMod INSTANCE;
    private long currentMS = 0L;
    private long lastMS = -1L;

    public CrystalAuraMod() {
        super("CrystalAura");
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void tick() {
        EntityPlayerSP player = mc.player;

        currentMS = System.nanoTime() / 1000000;
        if (hasDelayRun((long) (1000 / PepsiMod.INSTANCE.miscOptions.crystalAura_speed))) {
            for (Entity e : mc.world.loadedEntityList) {
                if (player.getDistanceToEntity(e) < PepsiMod.INSTANCE.miscOptions.crystalAura_range) {
                    if (e instanceof EntityEnderCrystal) {
                        mc.playerController.attackEntity(player, e);
                        player.swingArm(EnumHand.MAIN_HAND);
                        lastMS = System.nanoTime() / 1000000;
                        break;
                    }
                }
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
                new ModuleOption<>(PepsiMod.INSTANCE.miscOptions.crystalAura_speed, "speed", OptionCompletions.FLOAT,
                        (value) -> {
                            PepsiMod.INSTANCE.miscOptions.crystalAura_speed = Math.max(value, 0);
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.miscOptions.crystalAura_speed;
                        }, "Speed", new ExtensionSlider(ExtensionType.VALUE_FLOAT, 0f, 20f, 0.5f)),
                new ModuleOption<>(PepsiMod.INSTANCE.miscOptions.crystalAura_range, "range", OptionCompletions.FLOAT,
                        (value) -> {
                            PepsiMod.INSTANCE.miscOptions.crystalAura_range = Math.max(value, 0);
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.miscOptions.crystalAura_range;
                        }, "Range", new ExtensionSlider(ExtensionType.VALUE_FLOAT, 3f, 10f, 0.05f))
        };
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.COMBAT;
    }

    public boolean hasDelayRun(long time) {
        return (currentMS - lastMS) >= time;
    }
}
