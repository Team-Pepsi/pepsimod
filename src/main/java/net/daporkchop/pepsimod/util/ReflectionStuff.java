package net.daporkchop.pepsimod.util;

import net.daporkchop.pepsimod.PepsiMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.Timer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import java.lang.reflect.Field;

public class ReflectionStuff {
    public static Field renderPosX;
    public static Field renderPosY;
    public static Field renderPosZ;
    public static Field sleeping;
    public static Field PLAYER_MODEL_FLAG;
    public static Field minX;
    public static Field minY;
    public static Field minZ;
    public static Field maxX;
    public static Field maxY;
    public static Field maxZ;
    public static Field y_vec3d;
    public static Field timer;

    public static void init() {
        try {
            renderPosX = RenderManager.class.getDeclaredField("renderPosX");
            renderPosY = RenderManager.class.getDeclaredField("renderPosY");
            renderPosZ = RenderManager.class.getDeclaredField("renderPosZ");
            sleeping = EntityPlayer.class.getDeclaredField("sleeping");
            PLAYER_MODEL_FLAG = EntityPlayer.class.getDeclaredField("PLAYER_MODEL_FLAG");
            minX = AxisAlignedBB.class.getDeclaredField("minX");
            minY = AxisAlignedBB.class.getDeclaredField("minY");
            minZ = AxisAlignedBB.class.getDeclaredField("minZ");
            maxX = AxisAlignedBB.class.getDeclaredField("maxX");
            maxY = AxisAlignedBB.class.getDeclaredField("maxY");
            maxZ = AxisAlignedBB.class.getDeclaredField("maxZ");
            y_vec3d = Vec3d.class.getDeclaredField("y");
            timer = Minecraft.class.getDeclaredField("timer");

            renderPosX.setAccessible(true);
            renderPosY.setAccessible(true);
            renderPosZ.setAccessible(true);
            sleeping.setAccessible(true);
            PLAYER_MODEL_FLAG.setAccessible(true);
            minX.setAccessible(true);
            minY.setAccessible(true);
            minZ.setAccessible(true);
            maxX.setAccessible(true);
            maxY.setAccessible(true);
            maxZ.setAccessible(true);
            y_vec3d.setAccessible(true);
            timer.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Timer getTimer() {
        try {
            return (Timer) timer.get(PepsiMod.INSTANCE.mc);
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("wtf how");
    }

    public static void setY_vec3d(Vec3d vec, double val) {
        try {
            y_vec3d.set(vec, val);
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("wtf how");
    }

    public static double getMinX(AxisAlignedBB bb) {
        try {
            return (double) minX.get(bb);
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("wtf how");
    }

    public static double getMinY(AxisAlignedBB bb) {
        try {
            return (double) minY.get(bb);
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("wtf how");
    }

    public static double getMinZ(AxisAlignedBB bb) {
        try {
            return (double) minZ.get(bb);
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("wtf how");
    }

    public static double getMaxX(AxisAlignedBB bb) {
        try {
            return (double) maxX.get(bb);
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("wtf how");
    }

    public static double getMaxY(AxisAlignedBB bb) {
        try {
            return (double) maxY.get(bb);
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("wtf how");
    }

    public static double getMaxZ(AxisAlignedBB bb) {
        try {
            return (double) maxZ.get(bb);
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("wtf how");
    }

    public static void setMinX(AxisAlignedBB bb, double val) {
        try {
            minX.set(bb, val);
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("wtf how");
    }

    public static void setMinY(AxisAlignedBB bb, double val) {
        try {
            minY.set(bb, val);
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("wtf how");
    }

    public static void setMinZ(AxisAlignedBB bb, double val) {
        try {
            minZ.set(bb, val);
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("wtf how");
    }

    public static void setMaxX(AxisAlignedBB bb, double val) {
        try {
            maxX.set(bb, val);
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("wtf how");
    }

    public static void setMaxY(AxisAlignedBB bb, double val) {
        try {
            maxY.set(bb, val);
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("wtf how");
    }

    public static void setMaxZ(AxisAlignedBB bb, double val) {
        try {
            maxZ.set(bb, val);
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("wtf how");
    }

    public static DataParameter<Byte> getPLAYER_MODEL_FLAG() {
        try {
            return (DataParameter<Byte>) PLAYER_MODEL_FLAG.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("wtf how");
    }

    public static double getRenderPosX(RenderManager mgr) {
        try {
            return (double) renderPosX.get(mgr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("wtf how");
    }

    public static double getRenderPosY(RenderManager mgr) {
        try {
            return (double) renderPosY.get(mgr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("wtf how");
    }

    public static double getRenderPosZ(RenderManager mgr) {
        try {
            return (double) renderPosZ.get(mgr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("wtf how");
    }

    public static boolean getSleeping(EntityPlayer mgr) {
        try {
            return (boolean) sleeping.get(mgr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("wtf how");
    }
}
