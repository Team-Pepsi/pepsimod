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

package net.daporkchop.pepsimod.module.impl.render;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.OptionCompletions;
import net.daporkchop.pepsimod.totally.not.skidded.RotationUtils;
import net.daporkchop.pepsimod.util.Friends;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.ReflectionStuff;
import net.daporkchop.pepsimod.util.RenderColor;
import net.daporkchop.pepsimod.util.module.EntityFakePlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

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

    public TracersMod(boolean isEnabled, int key, boolean hide) {
        super(isEnabled, "Tracers", key, hide);
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
                            PepsiMod.INSTANCE.tracerSettings.sleeping = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.tracerSettings.sleeping;
                        }, "Sleeping"),
                new ModuleOption<>(false, "invisible", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.tracerSettings.invisible = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.tracerSettings.invisible;
                        }, "Invisible"),
                new ModuleOption<>(true, "friendColors", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.tracerSettings.friendColors = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.tracerSettings.friendColors;
                        }, "FriendColors"),
                new ModuleOption<>(false, "animals", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.tracerSettings.animals = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.tracerSettings.animals;
                        }, "Animals"),
                new ModuleOption<>(false, "monsters", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.tracerSettings.monsters = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.tracerSettings.monsters;
                        }, "Monsters"),
                new ModuleOption<>(false, "players", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.tracerSettings.players = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.tracerSettings.players;
                        }, "Players"),
                new ModuleOption<>(false, "items", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.tracerSettings.items = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.tracerSettings.items;
                        }, "Items"),
                new ModuleOption<>(false, "everything", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.tracerSettings.everything = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.tracerSettings.everything;
                        }, "Everything"),
                new ModuleOption<>(true, "distanceColor", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.tracerSettings.distanceColor = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.tracerSettings.distanceColor;
                        }, "DistanceColor")
        };
    }

    public void drawLines(float partialTicks) {
        // GL settings
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(2);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        GL11.glPushMatrix();
        GL11.glTranslated(-ReflectionStuff.getRenderPosX(PepsiMod.INSTANCE.mc.getRenderManager()), -ReflectionStuff.getRenderPosY(PepsiMod.INSTANCE.mc.getRenderManager()), -ReflectionStuff.getRenderPosZ(PepsiMod.INSTANCE.mc.getRenderManager()));

        // set start position
        Vec3d start = RotationUtils.getClientLookVec().addVector(0, PepsiMod.INSTANCE.mc.player.getEyeHeight(), 0).addVector(ReflectionStuff.getRenderPosX(PepsiMod.INSTANCE.mc.getRenderManager()), ReflectionStuff.getRenderPosY(PepsiMod.INSTANCE.mc.getRenderManager()), ReflectionStuff.getRenderPosZ(PepsiMod.INSTANCE.mc.getRenderManager()));

        GL11.glBegin(GL11.GL_LINES);

        RenderColor color = null;
        Vec3d end = null;
        for (Entity entity : PepsiMod.INSTANCE.mc.world.getLoadedEntityList()) {
            if (Math.abs(entity.posY - PepsiMod.INSTANCE.mc.player.posY) > 1e6) {
                continue;
            }

            if (!PepsiMod.INSTANCE.tracerSettings.invisible && entity.isInvisible()) {
                continue;
            }

            if ((PepsiMod.INSTANCE.tracerSettings.players || PepsiMod.INSTANCE.tracerSettings.everything) && entity instanceof EntityPlayer) {
                if (entity == PepsiMod.INSTANCE.mc.player || entity instanceof EntityFakePlayer) {
                    continue;
                }

                if (!PepsiMod.INSTANCE.tracerSettings.sleeping && ReflectionStuff.getSleeping((EntityPlayer) entity)) {
                    continue;
                }

                if (PepsiMod.INSTANCE.tracerSettings.friendColors && Friends.isFriend(entity.getUniqueID().toString())) {
                    color = friendColor;
                } else if (PepsiMod.INSTANCE.tracerSettings.distanceColor) {
                    double dist = PepsiMod.INSTANCE.mc.player.getDistanceSqToEntity(entity);
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
            } else if ((PepsiMod.INSTANCE.tracerSettings.monsters || PepsiMod.INSTANCE.tracerSettings.everything) && entity instanceof EntityMob) {
                if (PepsiMod.INSTANCE.tracerSettings.distanceColor) {
                    double dist = PepsiMod.INSTANCE.mc.player.getDistanceSqToEntity(entity);
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
            } else if ((PepsiMod.INSTANCE.tracerSettings.animals || PepsiMod.INSTANCE.tracerSettings.everything) && entity instanceof EntityAnimal) {
                color = animalColor;
            } else if ((PepsiMod.INSTANCE.tracerSettings.items || PepsiMod.INSTANCE.tracerSettings.everything) && entity instanceof EntityItem) {
                color = itemColor;
            } else {
                continue;
            }

            PepsiUtils.glColor(color);

            GL11.glVertex3d(start.x, start.y, start.z);
            GL11.glVertex3d(entity.posX, entity.posY, entity.posZ);
        }
        GL11.glEnd();

        GL11.glPopMatrix();

        // GL resets
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
    }
}
