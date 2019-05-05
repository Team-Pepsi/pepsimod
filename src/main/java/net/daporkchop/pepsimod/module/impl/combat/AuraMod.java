/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2019 DaPorkchop_
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

package net.daporkchop.pepsimod.module.impl.combat;

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.OptionCompletions;
import net.daporkchop.pepsimod.module.api.option.ExtensionSlider;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;
import net.daporkchop.pepsimod.module.impl.player.AutoEatMod;
import net.daporkchop.pepsimod.the.wurst.pkg.name.EntityUtils;
import net.daporkchop.pepsimod.the.wurst.pkg.name.RotationUtils;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.config.impl.TargettingTranslator;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHand;

public class AuraMod extends Module {
    public static final String[] targetBoneStrings = new String[]{"head", "feet", "middle"};
    public static AuraMod INSTANCE;
    public int lastTick = 0;

    {
        INSTANCE = this;
    }

    public AuraMod() {
        super("Aura");
    }

    @Override
    public void onEnable() {
        if (mc.player == null) {
        }
    }

    @Override
    public void onDisable() {
        if (mc.player == null) {
        }
    }

    @Override
    public void tick() {
        if (AutoEatMod.INSTANCE.state.enabled && !AutoEatMod.INSTANCE.doneEating) {
            return;
        }

        if (TargettingTranslator.INSTANCE.use_cooldown) {
            if (mc.player.getCooledAttackStrength(0) == 1) {
                Entity entity = EntityUtils.getBestEntityToAttack(EntityUtils.DEFAULT_SETTINGS);
                if (entity == null) {
                    return;
                }

                if (TargettingTranslator.INSTANCE.rotate) {
                    if (!RotationUtils.faceEntityPacket(entity)) {
                        return;
                    }
                    if (!TargettingTranslator.INSTANCE.silent) {
                        RotationUtils.faceEntityClient(entity);
                    }
                }

                mc.playerController.attackEntity(mc.player, entity);
                if (!TargettingTranslator.INSTANCE.silent) {
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                }
            }
        } else {
            this.lastTick++;

            if (this.lastTick >= TargettingTranslator.INSTANCE.delay) {
                this.lastTick = 0;

                Entity entity = EntityUtils.getBestEntityToAttack(EntityUtils.DEFAULT_SETTINGS);
                if (entity == null) {
                    return;
                }

                if (TargettingTranslator.INSTANCE.rotate) {
                    if (!RotationUtils.faceEntityPacket(entity)) {
                        return;
                    }
                    if (!TargettingTranslator.INSTANCE.silent) {
                        RotationUtils.faceEntityClient(entity);
                    }
                }

                mc.playerController.attackEntity(mc.player, entity);
                if (!TargettingTranslator.INSTANCE.silent) {
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
                new ModuleOption<>(TargettingTranslator.INSTANCE.players, "players", OptionCompletions.BOOLEAN,
                        (value) -> {
                            TargettingTranslator.INSTANCE.players = value;
                            return true;
                        },
                        () -> {
                            return TargettingTranslator.INSTANCE.players;
                        }, "Players"),
                new ModuleOption<>(TargettingTranslator.INSTANCE.animals, "animals", OptionCompletions.BOOLEAN,
                        (value) -> {
                            TargettingTranslator.INSTANCE.animals = value;
                            return true;
                        },
                        () -> {
                            return TargettingTranslator.INSTANCE.animals;
                        }, "Animals"),
                new ModuleOption<>(TargettingTranslator.INSTANCE.monsters, "monsters", OptionCompletions.BOOLEAN,
                        (value) -> {
                            TargettingTranslator.INSTANCE.monsters = value;
                            return true;
                        },
                        () -> {
                            return TargettingTranslator.INSTANCE.monsters;
                        }, "Monsters"),
                new ModuleOption<>(TargettingTranslator.INSTANCE.golems, "golems", OptionCompletions.BOOLEAN,
                        (value) -> {
                            TargettingTranslator.INSTANCE.golems = value;
                            return true;
                        },
                        () -> {
                            return TargettingTranslator.INSTANCE.golems;
                        }, "Golems"),
                new ModuleOption<>(TargettingTranslator.INSTANCE.sleeping, "sleeping", OptionCompletions.BOOLEAN,
                        (value) -> {
                            TargettingTranslator.INSTANCE.sleeping = value;
                            return true;
                        },
                        () -> {
                            return TargettingTranslator.INSTANCE.sleeping;
                        }, "Sleeping"),
                new ModuleOption<>(TargettingTranslator.INSTANCE.invisible, "invisible", OptionCompletions.BOOLEAN,
                        (value) -> {
                            TargettingTranslator.INSTANCE.invisible = value;
                            return true;
                        },
                        () -> {
                            return TargettingTranslator.INSTANCE.invisible;
                        }, "Invisible"),
                new ModuleOption<>(TargettingTranslator.INSTANCE.teams, "teams", OptionCompletions.BOOLEAN,
                        (value) -> {
                            TargettingTranslator.INSTANCE.teams = value;
                            return true;
                        },
                        () -> {
                            return TargettingTranslator.INSTANCE.teams;
                        }, "Teams"),
                new ModuleOption<>(TargettingTranslator.INSTANCE.friends, "friends", OptionCompletions.BOOLEAN,
                        (value) -> {
                            TargettingTranslator.INSTANCE.friends = value;
                            return true;
                        },
                        () -> {
                            return TargettingTranslator.INSTANCE.friends;
                        }, "Friends"),
                new ModuleOption<>(TargettingTranslator.INSTANCE.through_walls, "through_walls", OptionCompletions.BOOLEAN,
                        (value) -> {
                            TargettingTranslator.INSTANCE.through_walls = value;
                            return true;
                        },
                        () -> {
                            return TargettingTranslator.INSTANCE.through_walls;
                        }, "Through Walls"),
                new ModuleOption<>(TargettingTranslator.INSTANCE.fov, "fov", OptionCompletions.FLOAT,
                        (value) -> {
                            TargettingTranslator.INSTANCE.fov = value;
                            return true;
                        },
                        () -> {
                            return TargettingTranslator.INSTANCE.fov;
                        }, "FOV", new ExtensionSlider(ExtensionType.VALUE_FLOAT, 0.0f, 360.0f, 0.5f)),
                new ModuleOption<>(TargettingTranslator.INSTANCE.reach, "reach", OptionCompletions.FLOAT,
                        (value) -> {
                            TargettingTranslator.INSTANCE.reach = value;
                            return true;
                        },
                        () -> {
                            return TargettingTranslator.INSTANCE.reach;
                        }, "Reach", new ExtensionSlider(ExtensionType.VALUE_FLOAT, 0.0f, 10.0f, 0.1f)),
                new ModuleOption<>(TargettingTranslator.INSTANCE.delay, "delay", OptionCompletions.INTEGER,
                        (value) -> {
                            TargettingTranslator.INSTANCE.delay = value;
                            return true;
                        },
                        () -> {
                            return TargettingTranslator.INSTANCE.delay;
                        }, "Delay", new ExtensionSlider(ExtensionType.VALUE_INT, 0, 50, 1)),
                new ModuleOption<>(TargettingTranslator.INSTANCE.use_cooldown, "use_cooldown", OptionCompletions.BOOLEAN,
                        (value) -> {
                            TargettingTranslator.INSTANCE.use_cooldown = value;
                            return true;
                        },
                        () -> {
                            return TargettingTranslator.INSTANCE.use_cooldown;
                        }, "Use Cooldown"),
                new ModuleOption<>(TargettingTranslator.INSTANCE.silent, "silent", OptionCompletions.BOOLEAN,
                        (value) -> {
                            TargettingTranslator.INSTANCE.silent = value;
                            return true;
                        },
                        () -> {
                            return TargettingTranslator.INSTANCE.silent;
                        }, "Silent"),
                new ModuleOption<>(TargettingTranslator.INSTANCE.rotate, "rotate", OptionCompletions.BOOLEAN,
                        (value) -> {
                            TargettingTranslator.INSTANCE.rotate = value;
                            return true;
                        },
                        () -> {
                            return TargettingTranslator.INSTANCE.rotate;
                        }, "Rotate"),
                new ModuleOption<>(TargettingTranslator.INSTANCE.targetBone, "bone", targetBoneStrings,
                        (value) -> {
                            TargettingTranslator.INSTANCE.targetBone = value;
                            return true;
                        },
                        () -> {
                            return TargettingTranslator.INSTANCE.targetBone;
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
        if (TargettingTranslator.INSTANCE.silent) {
            mode += "Silent:";
        }
        if (TargettingTranslator.INSTANCE.rotate) {
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
                TargettingTranslator.TargetBone bone = TargettingTranslator.TargetBone.valueOf(s);
                if (bone == null) {
                    clientMessage("Not a valid bone: " + args[2]);
                } else {
                    this.getOptionByName("bone").setValue(bone);
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
