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

package net.daporkchop.pepsimod.module.impl.render;

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.OptionCompletions;
import net.daporkchop.pepsimod.module.api.option.ExtensionSlider;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;
import net.daporkchop.pepsimod.util.EntityFakePlayer;
import net.daporkchop.pepsimod.util.ReflectionStuff;
import net.daporkchop.pepsimod.util.RenderColor;
import net.daporkchop.pepsimod.util.config.impl.FriendsTranslator;
import net.daporkchop.pepsimod.util.config.impl.TracersTranslator;
import net.daporkchop.pepsimod.util.render.WorldRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class TracersMod extends Module {
    public static final RenderColor friendColor = new RenderColor(76, 144, 255, 255);
    public static final RenderColor monsterColor = new RenderColor(128, 0, 0, 255);
    public static final RenderColor animalColor = new RenderColor(0, 0, 204, 255);
    public static final RenderColor itemColor = new RenderColor(179, 179, 179, 255);

    public static final RenderColor distSafe = new RenderColor(0, 255, 0, 255);
    public static final RenderColor dist20 = new RenderColor(128, 255, 0, 255);
    public static final RenderColor dist15 = new RenderColor(255, 255, 0, 255);
    public static final RenderColor dist10 = new RenderColor(255, 128, 0, 255);
    public static final RenderColor dist5 = new RenderColor(255, 0, 0, 255);

    public static TracersMod INSTANCE;

    {
        INSTANCE = this;
    }

    public TracersMod() {
        super("Tracers");
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void tick() {
    }

    @Override
    public void init() {
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new ModuleOption<>(false, "sleeping", OptionCompletions.BOOLEAN,
                        (value) -> {
                            TracersTranslator.INSTANCE.sleeping = value;
                            return true;
                        },
                        () -> {
                            return TracersTranslator.INSTANCE.sleeping;
                        }, "Sleeping"),
                new ModuleOption<>(false, "invisible", OptionCompletions.BOOLEAN,
                        (value) -> {
                            TracersTranslator.INSTANCE.invisible = value;
                            return true;
                        },
                        () -> {
                            return TracersTranslator.INSTANCE.invisible;
                        }, "Invisible"),
                new ModuleOption<>(true, "friendColors", OptionCompletions.BOOLEAN,
                        (value) -> {
                            TracersTranslator.INSTANCE.friendColors = value;
                            return true;
                        },
                        () -> {
                            return TracersTranslator.INSTANCE.friendColors;
                        }, "FriendColors"),
                new ModuleOption<>(false, "animals", OptionCompletions.BOOLEAN,
                        (value) -> {
                            TracersTranslator.INSTANCE.animals = value;
                            return true;
                        },
                        () -> {
                            return TracersTranslator.INSTANCE.animals;
                        }, "Animals"),
                new ModuleOption<>(false, "monsters", OptionCompletions.BOOLEAN,
                        (value) -> {
                            TracersTranslator.INSTANCE.monsters = value;
                            return true;
                        },
                        () -> {
                            return TracersTranslator.INSTANCE.monsters;
                        }, "Monsters"),
                new ModuleOption<>(false, "players", OptionCompletions.BOOLEAN,
                        (value) -> {
                            TracersTranslator.INSTANCE.players = value;
                            return true;
                        },
                        () -> {
                            return TracersTranslator.INSTANCE.players;
                        }, "Players"),
                new ModuleOption<>(false, "items", OptionCompletions.BOOLEAN,
                        (value) -> {
                            TracersTranslator.INSTANCE.items = value;
                            return true;
                        },
                        () -> {
                            return TracersTranslator.INSTANCE.items;
                        }, "Items"),
                new ModuleOption<>(false, "everything", OptionCompletions.BOOLEAN,
                        (value) -> {
                            TracersTranslator.INSTANCE.everything = value;
                            return true;
                        },
                        () -> {
                            return TracersTranslator.INSTANCE.everything;
                        }, "Everything"),
                new ModuleOption<>(true, "distanceColor", OptionCompletions.BOOLEAN,
                        (value) -> {
                            TracersTranslator.INSTANCE.distanceColor = value;
                            return true;
                        },
                        () -> {
                            return TracersTranslator.INSTANCE.distanceColor;
                        }, "DistanceColor"),
                new ModuleOption<>(2.0f, "width", new String[]{"0.5", "1.0", "1.5", "2.0", "2.5"},
                        val -> {
                            TracersTranslator.INSTANCE.width = val;
                            return true;
                        },
                        () -> {
                            return TracersTranslator.INSTANCE.width;
                        }, "Width", new ExtensionSlider(ExtensionType.VALUE_FLOAT, 0.0f, 10.0f, 0.1f))
        };
    }


    @Override
    public void renderOverlay(WorldRenderer renderer) {
        renderer.width(TracersTranslator.INSTANCE.width);

        RenderColor color = null;
        for (Entity entity : mc.world.getLoadedEntityList()) {
            if (Math.abs(entity.posY - mc.player.posY) > 1e6) {
                continue;
            }

            if (!TracersTranslator.INSTANCE.invisible && entity.isInvisible()) {
                continue;
            }

            if ((TracersTranslator.INSTANCE.players || TracersTranslator.INSTANCE.everything) && entity instanceof EntityPlayer) {
                if (entity == mc.player || entity instanceof EntityFakePlayer) {
                    continue;
                }

                if (!TracersTranslator.INSTANCE.sleeping && ReflectionStuff.getSleeping((EntityPlayer) entity)) {
                    continue;
                }

                if (TracersTranslator.INSTANCE.friendColors && FriendsTranslator.INSTANCE.isFriend(entity)) {
                    color = friendColor;
                } else if (TracersTranslator.INSTANCE.distanceColor) {
                    double dist = mc.player.getDistanceSq(entity);
                    if (dist >= 625) {
                        color = distSafe;
                    } else if (dist >= 400) {
                        color = dist20;
                    } else if (dist >= 225) {
                        color = dist15;
                    } else if (dist >= 100) {
                        color = dist10;
                    } else {
                        color = dist5;
                    }
                }
            } else if ((TracersTranslator.INSTANCE.monsters || TracersTranslator.INSTANCE.everything) && entity instanceof EntityMob) {
                if (TracersTranslator.INSTANCE.distanceColor) {
                    double dist = mc.player.getDistanceSq(entity);
                    if (dist >= 625) {
                        color = distSafe;
                    } else if (dist >= 400) {
                        color = dist20;
                    } else if (dist >= 225) {
                        color = dist15;
                    } else if (dist >= 100) {
                        color = dist10;
                    } else {
                        color = dist5;
                    }
                } else {
                    color = monsterColor;
                }
            } else if ((TracersTranslator.INSTANCE.animals || TracersTranslator.INSTANCE.everything) && entity instanceof EntityAnimal) {
                color = animalColor;
            } else if ((TracersTranslator.INSTANCE.items || TracersTranslator.INSTANCE.everything) && entity instanceof EntityItem) {
                color = itemColor;
            } else {
                continue;
            }

            renderer.color(color).lineFromEyes(entity);
        }
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.RENDER;
    }
}
