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

package net.daporkchop.pepsimod.util;

import net.daporkchop.pepsimod.PepsiMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.Timer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.FMLLog;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

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
    public static Field boundingBox;

    public static Field getField(Class c, String... names)   {
        for (String s : names)  {
            try {
                return c.getDeclaredField(s);
            } catch (NoSuchFieldException e)    {
                FMLLog.log.info("unable to find field: " + s);
            }
        }

        throw new IllegalStateException("Field with names: " + names + " not found!");
    }

    public static void init() {
        try {
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);

            renderPosX = getField(RenderManager.class, "renderPosX", "field_78725_b", "o");
            renderPosY = getField(RenderManager.class, "renderPosY", "field_78726_c", "p");
            renderPosZ = getField(RenderManager.class, "renderPosZ", "field_78723_d", "q");
            sleeping = getField(EntityPlayer.class, "sleeping", "field_71083_bS", "bK");
            PLAYER_MODEL_FLAG = getField(EntityPlayer.class, "PLAYER_MODEL_FLAG", "field_184827_bp", "br");
            minX = getField(AxisAlignedBB.class, "minX", "field_72340_a", "a");
            minY = getField(AxisAlignedBB.class, "minY", "field_72338_b", "b");
            minZ = getField(AxisAlignedBB.class, "minZ", "field_72339_c", "c");
            maxX = getField(AxisAlignedBB.class, "maxX", "field_72336_d", "d");
            maxY = getField(AxisAlignedBB.class, "maxY", "field_72337_e", "e");
            maxZ = getField(AxisAlignedBB.class, "maxZ", "field_72334_f", "f");
            y_vec3d = getField(Vec3d.class, "y", "field_72448_b", "c");
            timer = getField(Minecraft.class, "timer", "field_71428_T", "Y");
            boundingBox = getField(Entity.class, "boundingBox", "field_70121_D", "av");

            renderPosX.setAccessible(true);
            modifiersField.setInt(renderPosX, renderPosX.getModifiers() & ~Modifier.FINAL);
            renderPosY.setAccessible(true);
            modifiersField.setInt(renderPosY, renderPosY.getModifiers() & ~Modifier.FINAL);
            renderPosZ.setAccessible(true);
            modifiersField.setInt(renderPosZ, renderPosZ.getModifiers() & ~Modifier.FINAL);
            sleeping.setAccessible(true);
            modifiersField.setInt(sleeping, sleeping.getModifiers() & ~Modifier.FINAL);
            PLAYER_MODEL_FLAG.setAccessible(true);
            modifiersField.setInt(PLAYER_MODEL_FLAG, PLAYER_MODEL_FLAG.getModifiers() & ~Modifier.FINAL);
            minX.setAccessible(true);
            modifiersField.setInt(minX, minX.getModifiers() & ~Modifier.FINAL);
            minY.setAccessible(true);
            modifiersField.setInt(minY, minY.getModifiers() & ~Modifier.FINAL);
            minZ.setAccessible(true);
            modifiersField.setInt(minZ, minZ.getModifiers() & ~Modifier.FINAL);
            maxX.setAccessible(true);
            modifiersField.setInt(maxX, maxX.getModifiers() & ~Modifier.FINAL);
            maxY.setAccessible(true);
            modifiersField.setInt(maxY, maxY.getModifiers() & ~Modifier.FINAL);
            maxZ.setAccessible(true);
            modifiersField.setInt(maxZ, maxZ.getModifiers() & ~Modifier.FINAL);
            y_vec3d.setAccessible(true);
            modifiersField.setInt(y_vec3d, y_vec3d.getModifiers() & ~Modifier.FINAL);
            timer.setAccessible(true);
            modifiersField.setInt(timer, timer.getModifiers() & ~Modifier.FINAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AxisAlignedBB getBoundingBox(Entity entity) {
        try {
            return (AxisAlignedBB) boundingBox.get(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new IllegalStateException("wtf how");
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
            throw new IllegalStateException("wtf how");
        }
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
            throw new IllegalStateException("wtf how");
        }
    }

    public static void setMinY(AxisAlignedBB bb, double val) {
        try {
            minY.set(bb, val);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("wtf how");
        }
    }

    public static void setMinZ(AxisAlignedBB bb, double val) {
        try {
            minZ.set(bb, val);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("wtf how");
        }
    }

    public static void setMaxX(AxisAlignedBB bb, double val) {
        try {
            maxX.set(bb, val);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("wtf how");
        }
    }

    public static void setMaxY(AxisAlignedBB bb, double val) {
        try {
            maxY.set(bb, val);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("wtf how");
        }
    }

    public static void setMaxZ(AxisAlignedBB bb, double val) {
        try {
            maxZ.set(bb, val);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("wtf how");
        }
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
