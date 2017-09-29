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

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.OptionCompletions;
import net.daporkchop.pepsimod.module.api.TimeModule;
import net.daporkchop.pepsimod.module.api.option.ExtensionSlider;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.module.ElytraFlyMode;
import net.minecraft.init.Items;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import org.lwjgl.input.Keyboard;

public class ElytraFlyMod extends TimeModule {
    public static final String[] modes = new String[]{"normal"};

    public static ElytraFlyMod INSTANCE;

    {
        INSTANCE = this;
    }

    public ElytraFlyMod() {
        super("Elytra+");
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

        if (mc.player.isElytraFlying()) {
            if (pepsiMod.elytraFlySettings.stopInWater && mc.player.isInWater()) {
                mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                return;
            }

            if (pepsiMod.elytraFlySettings.fly) {
                if (Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode())) {
                    mc.player.motionY += 0.08;
                } else if (Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode())) {
                    mc.player.motionY -= 0.04;
                }

                if (Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode())) {
                    double yaw = Math.toRadians(mc.player.rotationYaw);
                    mc.player.motionX -= Math.sin(yaw) * pepsiMod.elytraFlySettings.speed;
                    mc.player.motionZ += Math.cos(yaw) * pepsiMod.elytraFlySettings.speed;
                } else if (Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode())) {
                    double yaw = Math.toRadians(mc.player.rotationYaw);
                    mc.player.motionX += Math.sin(yaw) * pepsiMod.elytraFlySettings.speed;
                    mc.player.motionZ += Math.cos(yaw) * pepsiMod.elytraFlySettings.speed;
                }
            }
        } else if (pepsiMod.elytraFlySettings.easyStart && ItemElytra.isUsable(chestplate) && mc.gameSettings.keyBindJump.isPressed()) {
            if (hasTimePassedM(1000)) {
                updateLastMS();
                mc.player.setJumping(false);
                mc.player.setSprinting(true);
                mc.player.jump();
            }
            mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        }
    }

    @Override
    public void init() {

    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new ModuleOption<>(pepsiMod.elytraFlySettings.easyStart, "easyStart", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.elytraFlySettings.easyStart = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.elytraFlySettings.easyStart;
                        }, "EasyStart"),
                new ModuleOption<>(pepsiMod.elytraFlySettings.stopInWater, "stopInWater", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.elytraFlySettings.stopInWater = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.elytraFlySettings.stopInWater;
                        }, "StopInWater"),
                new ModuleOption<>(pepsiMod.elytraFlySettings.fly, "fly", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.elytraFlySettings.fly = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.elytraFlySettings.fly;
                        }, "Fly"),
                new ModuleOption<>(pepsiMod.elytraFlySettings.mode, "mode", modes,
                        (value) -> {
                            pepsiMod.elytraFlySettings.mode = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.elytraFlySettings.mode;
                        }, "Mode", false),
                new ModuleOption<>(pepsiMod.elytraFlySettings.speed, "speed", OptionCompletions.FLOAT,
                        (value) -> {
                            pepsiMod.elytraFlySettings.speed = Math.max(value, 0);
                            return true;
                        },
                        () -> {
                            return pepsiMod.elytraFlySettings.speed;
                        }, "Speed", new ExtensionSlider(ExtensionType.VALUE_FLOAT, 0f, 3f, 0.01f))
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
