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

package net.daporkchop.pepsimod.module.impl.movement;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.OptionCompletions;
import net.daporkchop.pepsimod.module.api.TimeModule;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.module.ElytraFlyMode;
import net.minecraft.init.Items;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;

public class ElytraFlyMod extends TimeModule {
    public static final String[] modes = new String[]{"normal"};

    public static ElytraFlyMod INSTANCE;

    {
        INSTANCE = this;
    }

    public ElytraFlyMod(boolean isEnabled, int key, boolean hide) {
        super(isEnabled, "Elytra+", key, hide);
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

        ItemStack chestplate = PepsiUtils.getWearingArmor(1);
        if (chestplate == null || chestplate.getItem() != Items.ELYTRA) {
            return;
        }

        if (PepsiMod.INSTANCE.mc.player.isElytraFlying()) {
            if (PepsiMod.INSTANCE.elytraFlySettings.stopInWater && PepsiMod.INSTANCE.mc.player.isInWater()) {
                PepsiMod.INSTANCE.mc.getConnection().sendPacket(new CPacketEntityAction(PepsiMod.INSTANCE.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                return;
            }

            if (PepsiMod.INSTANCE.elytraFlySettings.fly) {
                if (PepsiMod.INSTANCE.mc.gameSettings.keyBindJump.isPressed()) {
                    PepsiMod.INSTANCE.mc.player.motionY += 0.08;
                } else if (PepsiMod.INSTANCE.mc.gameSettings.keyBindSneak.isPressed()) {
                    PepsiMod.INSTANCE.mc.player.motionY -= 0.04;
                }

                if (PepsiMod.INSTANCE.mc.gameSettings.keyBindForward.isPressed()) {
                    double yaw = Math.toRadians(PepsiMod.INSTANCE.mc.player.rotationYaw);
                    PepsiMod.INSTANCE.mc.player.motionX -= Math.sin(yaw) * 0.1f;
                    PepsiMod.INSTANCE.mc.player.motionZ += Math.cos(yaw) * 0.1f;
                } else if (PepsiMod.INSTANCE.mc.gameSettings.keyBindBack.isPressed()) {
                    double yaw = Math.toRadians(PepsiMod.INSTANCE.mc.player.rotationYaw);
                    PepsiMod.INSTANCE.mc.player.motionX += Math.sin(yaw) * 0.1f;
                    PepsiMod.INSTANCE.mc.player.motionZ += Math.cos(yaw) * 0.1f;
                }
            }
        } else if (PepsiMod.INSTANCE.elytraFlySettings.easyStart && ItemElytra.isUsable(chestplate) && PepsiMod.INSTANCE.mc.gameSettings.keyBindJump.isPressed()) {
            if (hasTimePassedM(1000)) {
                updateLastMS();
                PepsiMod.INSTANCE.mc.player.setJumping(false);
                PepsiMod.INSTANCE.mc.player.setSprinting(true);
                PepsiMod.INSTANCE.mc.player.jump();
            }
            PepsiMod.INSTANCE.mc.getConnection().sendPacket(new CPacketEntityAction(PepsiMod.INSTANCE.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        }
    }

    @Override
    public void init() {

    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new ModuleOption<>(PepsiMod.INSTANCE.elytraFlySettings.easyStart, "easyStart", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.elytraFlySettings.easyStart = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.elytraFlySettings.easyStart;
                        }, "EasyStart"),
                new ModuleOption<>(PepsiMod.INSTANCE.elytraFlySettings.stopInWater, "stopInWater", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.elytraFlySettings.stopInWater = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.elytraFlySettings.stopInWater;
                        }, "StopInWater"),
                new ModuleOption<>(PepsiMod.INSTANCE.elytraFlySettings.fly, "fly", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.elytraFlySettings.fly = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.elytraFlySettings.fly;
                        }, "Fly"),
                new ModuleOption<>(PepsiMod.INSTANCE.elytraFlySettings.mode, "mode", modes,
                        (value) -> {
                            PepsiMod.INSTANCE.elytraFlySettings.mode = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.elytraFlySettings.mode;
                        }, "Mode", false)
        };
    }

    @Override
    public boolean hasModeInName() {
        return true;
    }

    @Override
    public String getModeForName() {
        return ((ElytraFlyMode) getOptionByName("mode").getValue()).name();
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.MOVEMENT;
    }
}
