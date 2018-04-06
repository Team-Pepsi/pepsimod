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

package net.daporkchop.pepsimod.module.impl.movement;

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.OptionCompletions;
import net.daporkchop.pepsimod.module.api.TimeModule;
import net.daporkchop.pepsimod.module.api.option.ExtensionSlider;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.ReflectionStuff;
import net.daporkchop.pepsimod.util.config.impl.ElytraFlyTranslator;
import net.minecraft.init.Items;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;

public class ElytraFlyMod extends TimeModule {
    public static final String[] modes = new String[]{"normal", "packet"};

    public static ElytraFlyMod INSTANCE;

    {
        INSTANCE = this;
    }

    public ElytraFlyMod() {
        super("Elytra+");
    }

    @Override
    public void onEnable() {
        if (ElytraFlyTranslator.INSTANCE.mode == ElytraFlyTranslator.ElytraFlyMode.PACKET) {
            if (mc.world == null) {
                ModuleManager.disableModule(this);
            }
        }
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
            if (ElytraFlyTranslator.INSTANCE.stopInWater && mc.player.isInWater()) {
                mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                return;
            }

            if (ElytraFlyTranslator.INSTANCE.fly && ElytraFlyTranslator.INSTANCE.mode == ElytraFlyTranslator.ElytraFlyMode.NORMAL) {
                if (ReflectionStuff.getPressed(mc.gameSettings.keyBindJump)) {
                    mc.player.motionY += 0.08;
                } else if (ReflectionStuff.getPressed(mc.gameSettings.keyBindSneak)) {
                    mc.player.motionY -= 0.04;
                }

                if (ReflectionStuff.getPressed(mc.gameSettings.keyBindForward)) {
                    double yaw = Math.toRadians(mc.player.rotationYaw);
                    mc.player.motionX -= Math.sin(yaw) * ElytraFlyTranslator.INSTANCE.speed;
                    mc.player.motionZ += Math.cos(yaw) * ElytraFlyTranslator.INSTANCE.speed;
                } else if (ReflectionStuff.getPressed(mc.gameSettings.keyBindBack)) {
                    double yaw = Math.toRadians(mc.player.rotationYaw);
                    mc.player.motionX += Math.sin(yaw) * ElytraFlyTranslator.INSTANCE.speed;
                    mc.player.motionZ -= Math.cos(yaw) * ElytraFlyTranslator.INSTANCE.speed;
                }

            }
        } else if (ElytraFlyTranslator.INSTANCE.easyStart && ElytraFlyTranslator.INSTANCE.mode != ElytraFlyTranslator.ElytraFlyMode.PACKET && ItemElytra.isUsable(chestplate) && mc.gameSettings.keyBindJump.isPressed()) {
            if (hasTimePassedM(1000)) {
                updateLastMS();
                mc.player.setJumping(false);
                mc.player.setSprinting(true);
                mc.player.jump();
            }
            mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        }
        if (ElytraFlyTranslator.INSTANCE.fly && ElytraFlyTranslator.INSTANCE.mode == ElytraFlyTranslator.ElytraFlyMode.PACKET) {
            mc.player.motionX = mc.player.motionZ = 0;
            if (ReflectionStuff.getPressed(mc.gameSettings.keyBindForward)) {
                double yaw = Math.toRadians(mc.player.rotationYaw);
                mc.player.motionX -= Math.sin(yaw) * ElytraFlyTranslator.INSTANCE.speed;
                mc.player.motionZ += Math.cos(yaw) * ElytraFlyTranslator.INSTANCE.speed;
            } else if (ReflectionStuff.getPressed(mc.gameSettings.keyBindBack)) {
                double yaw = Math.toRadians(mc.player.rotationYaw);
                mc.player.motionX += Math.sin(yaw) * ElytraFlyTranslator.INSTANCE.speed;
                mc.player.motionZ -= Math.cos(yaw) * ElytraFlyTranslator.INSTANCE.speed;
            }
            mc.player.motionY = 0;
            mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
            mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        }
    }

    @Override
    public void init() {

    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new ModuleOption<>(ElytraFlyTranslator.INSTANCE.easyStart, "easyStart", OptionCompletions.BOOLEAN,
                        (value) -> {
                            ElytraFlyTranslator.INSTANCE.easyStart = value;
                            return true;
                        },
                        () -> {
                            return ElytraFlyTranslator.INSTANCE.easyStart;
                        }, "EasyStart"),
                new ModuleOption<>(ElytraFlyTranslator.INSTANCE.stopInWater, "stopInWater", OptionCompletions.BOOLEAN,
                        (value) -> {
                            ElytraFlyTranslator.INSTANCE.stopInWater = value;
                            return true;
                        },
                        () -> {
                            return ElytraFlyTranslator.INSTANCE.stopInWater;
                        }, "StopInWater"),
                new ModuleOption<>(ElytraFlyTranslator.INSTANCE.fly, "fly", OptionCompletions.BOOLEAN,
                        (value) -> {
                            ElytraFlyTranslator.INSTANCE.fly = value;
                            return true;
                        },
                        () -> {
                            return ElytraFlyTranslator.INSTANCE.fly;
                        }, "Fly"),
                new ModuleOption<>(ElytraFlyTranslator.INSTANCE.mode, "mode", modes,
                        (value) -> {
                            ElytraFlyTranslator.INSTANCE.mode = value;
                            return true;
                        },
                        () -> {
                            return ElytraFlyTranslator.INSTANCE.mode;
                        }, "Mode", false),
                new ModuleOption<>(ElytraFlyTranslator.INSTANCE.speed, "speed", OptionCompletions.FLOAT,
                        (value) -> {
                            ElytraFlyTranslator.INSTANCE.speed = Math.max(value, 0);
                            return true;
                        },
                        () -> {
                            return ElytraFlyTranslator.INSTANCE.speed;
                        }, "Speed", new ExtensionSlider(ExtensionType.VALUE_FLOAT, 0f, 3f, 0.01f))
        };
    }

    @Override
    public boolean hasModeInName() {
        return true;
    }

    @Override
    public String getModeForName() {
        return ElytraFlyTranslator.INSTANCE.mode.name();
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.MOVEMENT;
    }

    @Override
    public String getSuggestion(String cmd, String[] args) {
        if (args.length == 2 && args[1].equals("mode")) {
            return cmd + " " + modes[0];
        } else if (args.length == 3 && args[1].equals("mode")) {
            if (args[2].isEmpty()) {
                return cmd + modes[0];
            } else {
                for (String s : modes) {
                    if (s.startsWith(args[2])) {
                        return args[0] + " " + args[1] + " " + s;
                    }
                }

                return "";
            }
        }

        return super.getSuggestion(cmd, args);
    }

    @Override
    public void execute(String cmd, String[] args) {
        if (args.length == 3 && !args[2].isEmpty() && cmd.startsWith(".elytra+ mode ")) {
            String s = args[2].toUpperCase();
            try {
                ElytraFlyTranslator.ElytraFlyMode mode = ElytraFlyTranslator.ElytraFlyMode.valueOf(s);
                if (mode == null) {
                    clientMessage("Not a valid mode: " + args[2]);
                } else {
                    getOptionByName("mode").setValue(mode);
                    clientMessage("Set " + PepsiUtils.COLOR_ESCAPE + "o" + args[1] + PepsiUtils.COLOR_ESCAPE + "r to " + PepsiUtils.COLOR_ESCAPE + "o" + s);
                }
            } catch (Exception e) {
                clientMessage("Not a valid mode: " + args[2]);
            }
            return;
        }

        super.execute(cmd, args);
    }
}
