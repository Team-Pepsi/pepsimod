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
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.OptionCompletions;
import net.daporkchop.pepsimod.module.api.option.ExtensionSlider;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;
import net.daporkchop.pepsimod.module.impl.player.AutoEatMod;
import net.daporkchop.pepsimod.totally.not.skidded.EntityUtils;
import net.daporkchop.pepsimod.totally.not.skidded.RotationUtils;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.module.TargetBone;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHand;

public class AuraMod extends Module {
    public static final String[] targetBoneStrings = new String[]{"head", "feet", "middle"};
    public int lastTick = 0;

    public AuraMod() {
        super("Aura");
    }

    @Override
    public void onEnable() {
        if (mc.player == null) {
            return;
        }
    }

    @Override
    public void onDisable() {
        if (mc.player == null) {
            return;
        }
    }

    @Override
    public void tick() {
        if (AutoEatMod.INSTANCE.isEnabled && !AutoEatMod.INSTANCE.doneEating) {
            return;
        }

        if (pepsiMod.targetSettings.use_cooldown) {
            if (mc.player.getCooledAttackStrength(0) == 1) {
                Entity entity = EntityUtils.getBestEntityToAttack(EntityUtils.DEFAULT_SETTINGS);
                if (entity == null) {
                    return;
                }

                if (pepsiMod.targetSettings.rotate) {
                    if (!RotationUtils.faceEntityPacket(entity)) {
                        return;
                    }
                    if (!pepsiMod.targetSettings.silent) {
                        RotationUtils.faceEntityClient(entity);
                    }
                }

                mc.playerController.attackEntity(mc.player, entity);
                if (!pepsiMod.targetSettings.silent) {
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                }
            }
        } else {
            lastTick++;

            if (lastTick >= pepsiMod.targetSettings.delay) {
                lastTick = 0;

                Entity entity = EntityUtils.getBestEntityToAttack(EntityUtils.DEFAULT_SETTINGS);
                if (entity == null) {
                    return;
                }

                if (pepsiMod.targetSettings.rotate) {
                    if (!RotationUtils.faceEntityPacket(entity)) {
                        return;
                    }
                    if (!pepsiMod.targetSettings.silent) {
                        RotationUtils.faceEntityClient(entity);
                    }
                }

                mc.playerController.attackEntity(mc.player, entity);
                if (!pepsiMod.targetSettings.silent) {
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                }
            }
        }
    }

    @Override
    public void init() {

    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{ //wtf why is this throwing an NPE
                new ModuleOption<>(pepsiMod.targetSettings.players, "players", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.targetSettings.players = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.targetSettings.players;
                        }, "Players"),
                new ModuleOption<>(pepsiMod.targetSettings.animals, "animals", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.targetSettings.animals = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.targetSettings.animals;
                        }, "Animals"),
                new ModuleOption<>(pepsiMod.targetSettings.monsters, "monsters", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.targetSettings.monsters = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.targetSettings.monsters;
                        }, "Monsters"),
                new ModuleOption<>(pepsiMod.targetSettings.golems, "golems", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.targetSettings.golems = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.targetSettings.golems;
                        }, "Golems"),
                new ModuleOption<>(pepsiMod.targetSettings.sleeping, "sleeping", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.targetSettings.sleeping = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.targetSettings.sleeping;
                        }, "Sleeping"),
                new ModuleOption<>(pepsiMod.targetSettings.invisible, "invisible", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.targetSettings.invisible = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.targetSettings.invisible;
                        }, "Invisible"),
                new ModuleOption<>(pepsiMod.targetSettings.teams, "teams", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.targetSettings.teams = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.targetSettings.teams;
                        }, "Teams"),
                new ModuleOption<>(pepsiMod.targetSettings.friends, "friends", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.targetSettings.friends = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.targetSettings.friends;
                        }, "Friends"),
                new ModuleOption<>(pepsiMod.targetSettings.through_walls, "through_walls", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.targetSettings.through_walls = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.targetSettings.through_walls;
                        }, "Through Walls"),
                new ModuleOption<>(pepsiMod.targetSettings.fov, "fov", OptionCompletions.FLOAT,
                        (value) -> {
                            pepsiMod.targetSettings.fov = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.targetSettings.fov;
                        }, "FOV", new ExtensionSlider(ExtensionType.VALUE_FLOAT, 0.0f, 360.0f, 0.5f)),
                new ModuleOption<>(pepsiMod.targetSettings.reach, "reach", OptionCompletions.FLOAT,
                        (value) -> {
                            pepsiMod.targetSettings.reach = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.targetSettings.reach;
                        }, "Reach", new ExtensionSlider(ExtensionType.VALUE_FLOAT, 0.0f, 10.0f, 0.1f)),
                new ModuleOption<>(pepsiMod.targetSettings.delay, "delay", OptionCompletions.INTEGER,
                        (value) -> {
                            pepsiMod.targetSettings.delay = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.targetSettings.delay;
                        }, "Delay", new ExtensionSlider(ExtensionType.VALUE_INT, 0, 50, 1)),
                new ModuleOption<>(pepsiMod.targetSettings.use_cooldown, "use_cooldown", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.targetSettings.use_cooldown = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.targetSettings.use_cooldown;
                        }, "Use Cooldown"),
                new ModuleOption<>(pepsiMod.targetSettings.silent, "silent", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.targetSettings.silent = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.targetSettings.silent;
                        }, "Silent"),
                new ModuleOption<>(pepsiMod.targetSettings.rotate, "rotate", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.targetSettings.rotate = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.targetSettings.rotate;
                        }, "Rotate"),
                new ModuleOption<>(pepsiMod.targetSettings.targetBone, "bone", targetBoneStrings,
                        (value) -> {
                            pepsiMod.targetSettings.targetBone = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.targetSettings.targetBone;
                        }, "Bone", false)
        };
    }

    @Override
    public boolean hasModeInName() {
        return true;
    }

    @Override
    public String getModeForName() {
        String mode = "";
        if (pepsiMod.targetSettings.silent) {
            mode += "Silent:";
        }
        if (pepsiMod.targetSettings.rotate) {
            mode += "Rotate";
        } else {
            mode += "NoRotate";
        }
        return mode;
    }

    @Override
    public String getSuggestion(String cmd, String[] args) {
        if (args.length == 2 && args[1].equals("bone")) {
            return cmd + " " + targetBoneStrings[0];
        } else if (args.length == 3 && args[1].equals("bone")) {
            if (args[2].isEmpty()) {
                return cmd + targetBoneStrings[0];
            } else {
                for (String s : targetBoneStrings) {
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
        if (args.length == 3 && !args[2].isEmpty() && cmd.startsWith(".aura bone ")) {
            String s = args[2].toUpperCase();
            try {
                TargetBone bone = TargetBone.valueOf(s);
                if (bone == null) {
                    clientMessage("Not a valid bone: " + args[2]);
                } else {
                    getOptionByName("bone").setValue(bone);
                    clientMessage("Set " + PepsiUtils.COLOR_ESCAPE + "o" + args[1] + PepsiUtils.COLOR_ESCAPE + "r to " + PepsiUtils.COLOR_ESCAPE + "o" + s);
                }
            } catch (Exception e) {
                clientMessage("Not a valid bone: " + args[2]);
            }
            return;
        }

        super.execute(cmd, args);
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.COMBAT;
    }
}
