package net.daporkchop.pepsimod.module.impl;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.api.CustomOption;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.option.OptionTypeBoolean;
import net.daporkchop.pepsimod.module.api.option.OptionTypeFloat;
import net.daporkchop.pepsimod.module.api.option.OptionTypeInteger;
import net.daporkchop.pepsimod.totally.not.skidded.EntityUtils;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.TargetBone;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHand;

public class Aura extends Module {
    public static final String[] targetBoneStrings = new String[]{"head", "feet", "middle"};
    public int lastTick = 0;

    public Aura(boolean isEnabled, int key, boolean hide) {
        super(isEnabled, "Aura", key, hide);
    }

    @Override
    public void onEnable() {
        if (PepsiMod.INSTANCE.mc.player == null) {
            return;
        }
    }

    @Override
    public void onDisable() {
        if (PepsiMod.INSTANCE.mc.player == null) {
            return;
        }
    }

    @Override
    public void tick() {
        if (PepsiMod.INSTANCE.targetSettings.use_cooldown) {
            if (PepsiMod.INSTANCE.mc.player.getCooledAttackStrength(0) == 1) {
                Entity entity = EntityUtils.getBestEntityToAttack(EntityUtils.DEFAULT_SETTINGS);
                if (entity == null) {
                    return;
                }
                PepsiMod.INSTANCE.mc.playerController.attackEntity(PepsiMod.INSTANCE.mc.player, entity);
                if (!PepsiMod.INSTANCE.targetSettings.silent) {
                    PepsiMod.INSTANCE.mc.player.swingArm(EnumHand.MAIN_HAND);
                }
            }
        } else {
            lastTick++;

            if (lastTick >= PepsiMod.INSTANCE.targetSettings.delay) {
                lastTick = 0;

                Entity entity = EntityUtils.getBestEntityToAttack(EntityUtils.DEFAULT_SETTINGS);
                if (entity == null) {
                    return;
                }
                PepsiMod.INSTANCE.mc.playerController.attackEntity(PepsiMod.INSTANCE.mc.player, entity);
                if (!PepsiMod.INSTANCE.targetSettings.silent) {
                    PepsiMod.INSTANCE.mc.player.swingArm(EnumHand.MAIN_HAND);
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
                new CustomOption<>(PepsiMod.INSTANCE.targetSettings.players, "players", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.targetSettings.players = value;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.targetSettings.players;
                        }),
                new CustomOption<>(PepsiMod.INSTANCE.targetSettings.animals, "animals", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.targetSettings.animals = value;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.targetSettings.animals;
                        }),
                new CustomOption<>(PepsiMod.INSTANCE.targetSettings.monsters, "monsters", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.targetSettings.monsters = value;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.targetSettings.monsters;
                        }),
                new CustomOption<>(PepsiMod.INSTANCE.targetSettings.golems, "golems", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.targetSettings.golems = value;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.targetSettings.golems;
                        }),
                new CustomOption<>(PepsiMod.INSTANCE.targetSettings.sleeping, "sleeping", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.targetSettings.sleeping = value;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.targetSettings.sleeping;
                        }),
                new CustomOption<>(PepsiMod.INSTANCE.targetSettings.invisible, "invisible", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.targetSettings.invisible = value;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.targetSettings.invisible;
                        }),
                new CustomOption<>(PepsiMod.INSTANCE.targetSettings.teams, "teams", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.targetSettings.teams = value;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.targetSettings.teams;
                        }),
                new CustomOption<>(PepsiMod.INSTANCE.targetSettings.friends, "friends", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.targetSettings.friends = value;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.targetSettings.friends;
                        }),
                new CustomOption<>(PepsiMod.INSTANCE.targetSettings.through_walls, "through_walls", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.targetSettings.through_walls = value;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.targetSettings.through_walls;
                        }),
                new CustomOption<>(PepsiMod.INSTANCE.targetSettings.fov, "fov", OptionTypeFloat.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.targetSettings.fov = value;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.targetSettings.fov;
                        }),
                new CustomOption<>(PepsiMod.INSTANCE.targetSettings.reach, "reach", OptionTypeFloat.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.targetSettings.reach = value;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.targetSettings.reach;
                        }),
                new CustomOption<>(PepsiMod.INSTANCE.targetSettings.delay, "delay", OptionTypeInteger.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.targetSettings.delay = value;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.targetSettings.delay;
                        }),
                new CustomOption<>(PepsiMod.INSTANCE.targetSettings.use_cooldown, "use_cooldown", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.targetSettings.use_cooldown = value;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.targetSettings.use_cooldown;
                        }),
                new CustomOption<>(PepsiMod.INSTANCE.targetSettings.silent, "silent", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.targetSettings.silent = value;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.targetSettings.silent;
                        }),
                new CustomOption<>(PepsiMod.INSTANCE.targetSettings.rotate, "rotate", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.targetSettings.rotate = value;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.targetSettings.rotate;
                        }),
                new CustomOption<>(PepsiMod.INSTANCE.targetSettings.targetBone, "bone", targetBoneStrings,
                        (value) -> {
                            PepsiMod.INSTANCE.targetSettings.targetBone = value;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.targetSettings.targetBone;
                        })
        };
    }

    @Override
    public boolean hasModeInName() {
        return true;
    }

    @Override
    public String getModeForName() {
        String mode = "";
        if (PepsiMod.INSTANCE.targetSettings.silent) {
            mode += "Silent:";
        }
        if (PepsiMod.INSTANCE.targetSettings.rotate) {
            mode += "Rotate";
        } else {
            mode += "NoRotate";
        }
        return mode;
    }

    @Override
    public String getSuggestion(String cmd, String[] args) {
        if (args.length == 2 && args[1].startsWith("bone")) {
            return cmd + " " + targetBoneStrings[0];
        } else if (args.length == 3) {
            if (args[2].isEmpty() && args[1].equals("bone")) {
                return cmd + targetBoneStrings[0];
            } else {
                for (String s : targetBoneStrings) {
                    if (s.startsWith(args[2])) {
                        return cmd + s;
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
}
