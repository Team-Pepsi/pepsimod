package net.daporkchop.pepsimod.module.impl.render;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.api.CustomOption;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.option.OptionTypeBoolean;
import net.daporkchop.pepsimod.totally.not.skidded.RotationUtils;
import net.daporkchop.pepsimod.util.Friends;
import net.daporkchop.pepsimod.util.PepsiUtils;
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
                new CustomOption<>(false, "sleeping", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.tracerSettings.sleeping = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.tracerSettings.sleeping;
                        }),
                new CustomOption<>(false, "invisible", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.tracerSettings.invisible = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.tracerSettings.invisible;
                        }),
                new CustomOption<>(true, "friendColors", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.tracerSettings.friendColors = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.tracerSettings.friendColors;
                        }),
                new CustomOption<>(false, "animals", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.tracerSettings.animals = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.tracerSettings.animals;
                        }),
                new CustomOption<>(false, "monsters", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.tracerSettings.monsters = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.tracerSettings.monsters;
                        }),
                new CustomOption<>(false, "players", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.tracerSettings.players = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.tracerSettings.players;
                        }),
                new CustomOption<>(false, "items", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.tracerSettings.items = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.tracerSettings.items;
                        }),
                new CustomOption<>(false, "everything", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.tracerSettings.everything = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.tracerSettings.everything;
                        }),
                new CustomOption<>(true, "distanceColor", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.tracerSettings.distanceColor = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.tracerSettings.distanceColor;
                        })
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
        GL11.glTranslated(-PepsiMod.INSTANCE.mc.getRenderManager().renderPosX, -PepsiMod.INSTANCE.mc.getRenderManager().renderPosY, -PepsiMod.INSTANCE.mc.getRenderManager().renderPosZ);

        // set start position
        Vec3d start = RotationUtils.getClientLookVec().addVector(0, PepsiMod.INSTANCE.mc.player.getEyeHeight(), 0).addVector(PepsiMod.INSTANCE.mc.getRenderManager().renderPosX, PepsiMod.INSTANCE.mc.getRenderManager().renderPosY, PepsiMod.INSTANCE.mc.getRenderManager().renderPosZ);

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

                if (!PepsiMod.INSTANCE.tracerSettings.sleeping && ((EntityPlayer) entity).sleeping) {
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
