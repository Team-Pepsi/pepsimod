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

package net.daporkchop.pepsimod.util;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.util.Timer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.translation.LanguageMap;
import net.minecraftforge.fml.common.FMLLog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;

//TODO: replace this with Unsafe or access transformers
public class ReflectionStuff extends PepsiConstants {
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
    public static Field debugFps;
    public static Field itemRenderer;
    public static Field pressed;
    public static Field ridingEntity;
    public static Field horseJumpPower;
    public static Field cPacketPlayer_x;
    public static Field cPacketPlayer_y;
    public static Field cPacketPlayer_z;
    public static Field landMovementFactor;
    public static Field inWater;
    public static Field rightClickDelayTimer;
    public static Field curBlockDamageMP;
    public static Field blockHitDelay;
    public static Field cPacketPlayer_onGround;
    public static Field parentScreen;
    public static Field DEFAULT_RESOURCE_DOMAINS;
    public static Field cPacketVehicleMove_y;
    public static Field currentPlayerItem;
    public static Field languageMap_instance;
    public static Field languageMap_languageList;

    public static Method updateFallState;
    public static Method rightClickMouse;

    private static Field modifiersField;

    static {
        try {
            modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
        } catch (Exception e) {
            //impossible!
        }
    }

    public static Field getField(Class c, String... names) {
        for (String s : names) {
            try {
                Field f = c.getDeclaredField(s);
                f.setAccessible(true);
                modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
                return f;
            } catch (NoSuchFieldException e) {
                FMLLog.log.info("unable to find field: " + s);
            } catch (IllegalAccessException e) {
                FMLLog.log.info("unable to make field changeable!");
            }
        }

        throw new IllegalStateException("Field with names: " + names + " not found!");
    }

    public static Method getMethod(Class c, String[] names, Class<?>... args) {
        for (String s : names) {
            try {
                Method m = c.getDeclaredMethod(s, args);
                m.setAccessible(true);
                return m;
            } catch (NoSuchMethodException e) {
                FMLLog.log.info("unable to find method: " + s);
            }
        }

        throw new IllegalStateException("Method with names: " + names + " not found!");
    }

    public static void init() {
        try {
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
            debugFps = getField(Minecraft.class, "debugFPS", "field_71470_ab", "ar");
            itemRenderer = getField(ItemRenderer.class, "itemRenderer", "field_178112_h", "k");
            pressed = getField(KeyBinding.class, "pressed", "field_74513_e", "i");
            ridingEntity = getField(Entity.class, "ridingEntity", "field_184239_as", "au");
            horseJumpPower = getField(EntityPlayerSP.class, "horseJumpPower", "field_110321_bQ", "cq");
            cPacketPlayer_x = getField(CPacketPlayer.class, "x", "field_149479_a", "a");
            cPacketPlayer_y = getField(CPacketPlayer.class, "y", "field_149477_b", "b");
            cPacketPlayer_z = getField(CPacketPlayer.class, "z", "field_149478_c", "c");
            inWater = getField(Entity.class, "inWater", "field_70171_ac", "U");
            landMovementFactor = getField(EntityLivingBase.class, "landMovementFactor", "field_70746_aG", "bC");
            rightClickDelayTimer = getField(Minecraft.class, "rightClickDelayTimer", "field_71467_ac", "as");
            blockHitDelay = getField(PlayerControllerMP.class, "blockHitDelay", "field_78781_i", "g");
            curBlockDamageMP = getField(PlayerControllerMP.class, "curBlockDamageMP", "field_78770_f", "e");
            cPacketPlayer_onGround = getField(CPacketPlayer.class, "onGround", "field_149474_g", "f");
            parentScreen = getField(GuiDisconnected.class, "parentScreen", "field_146307_h", "h");
            DEFAULT_RESOURCE_DOMAINS = getField(DefaultResourcePack.class, "DEFAULT_RESOURCE_DOMAINS", "field_110608_a", "a");
            cPacketVehicleMove_y = getField(CPacketVehicleMove.class, "y", "field_187008_b", "b");
            currentPlayerItem = getField(PlayerControllerMP.class, "currentPlayerItem", "field_78777_l", "j");
            languageMap_instance = getField(LanguageMap.class, "instance", "field_74817_a", "c");
            languageMap_languageList = getField(LanguageMap.class, "languageList", "field_74816_c", "d");

            updateFallState = getMethod(Entity.class, new String[]{"updateFallState", "func_184231_a", "a"}, double.class, boolean.class, IBlockState.class, BlockPos.class);
            rightClickMouse = getMethod(Minecraft.class, new String[]{"rightClickMouse", "func_147121_ag", "aB"});

            setDEFAULT_RESOURCE_DOMAINS(ImmutableSet.<String>builder().addAll(DefaultResourcePack.DEFAULT_RESOURCE_DOMAINS).add("wdl").build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, String> getLanguageMapMap() {
        try {
            return (Map<String, String>) languageMap_languageList.get(languageMap_instance.get(null));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static LanguageMap getLanguageMap() {
        try {
            return (LanguageMap) languageMap_instance.get(null);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static void setCurrentPlayerItem(int i) {
        try {
            currentPlayerItem.setInt(mc.playerController, i);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static double getCPacketVehicleMove_y(CPacketVehicleMove n) {
        try {
            return cPacketVehicleMove_y.getDouble(n);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static void setcPacketVehicleMove_y(CPacketVehicleMove n, double y) {
        try {
            cPacketVehicleMove_y.setDouble(n, y);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static void setDEFAULT_RESOURCE_DOMAINS(Set<String> n) {
        try {
            DEFAULT_RESOURCE_DOMAINS.set(null, n);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static GuiScreen getParentScreen(GuiDisconnected disconnected) {
        try {
            return (GuiScreen) parentScreen.get(disconnected);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static void rightClickMouse() {
        try {
            rightClickMouse.invoke(mc);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setcPacketPlayer_onGround(CPacketPlayer packet, boolean onGround) {
        try {
            cPacketPlayer_onGround.setBoolean(packet, onGround);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static boolean getPressed(KeyBinding binding) {
        try {
            return pressed.getBoolean(binding);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static float getCurBlockDamageMP() {
        try {
            return curBlockDamageMP.getFloat(mc.playerController);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setCurBlockDamageMP(float val) {
        try {
            curBlockDamageMP.setFloat(mc.playerController, val);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static int getBlockHitDelay() {
        try {
            return blockHitDelay.getInt(mc.playerController);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setBlockHitDelay(int val) {
        try {
            blockHitDelay.setInt(mc.playerController, val);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setRightClickDelayTimer(int val) {
        try {
            rightClickDelayTimer.setInt(mc, val);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setInWater(Entity entity, boolean y) {
        try {
            inWater.setBoolean(entity, y);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setLandMovementFactor(EntityLivingBase entity, float y) {
        try {
            landMovementFactor.setFloat(entity, y);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setCPacketPlayer_x(CPacketPlayer packet, double x) {
        try {
            cPacketPlayer_x.setDouble(packet, x);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setCPacketPlayer_y(CPacketPlayer packet, double y) {
        try {
            cPacketPlayer_y.setDouble(packet, y);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setCPacketPlayer_z(CPacketPlayer packet, double z) {
        try {
            cPacketPlayer_z.setDouble(packet, z);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setHorseJumpPower(float value) {
        try {
            horseJumpPower.setFloat(mc.player, value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void updateEntityFallState(Entity e, double d, boolean b, IBlockState state, BlockPos pos) {
        try {
            updateFallState.invoke(e, d, b, state, pos);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new IllegalStateException(exception);
        }
    }

    public static Entity getRidingEntity(Entity toGetFrom) {
        try {
            return (Entity) ridingEntity.get(toGetFrom);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setPressed(KeyBinding keyBinding, boolean state) {
        try {
            pressed.setBoolean(keyBinding, state);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static RenderItem getItemRenderer() {
        try {
            return (RenderItem) itemRenderer.get(mc.getItemRenderer());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static int getDebugFps() {
        try {
            return debugFps.getInt(null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static AxisAlignedBB getBoundingBox(Entity entity) {
        try {
            return (AxisAlignedBB) boundingBox.get(entity);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static Timer getTimer() {
        try {
            return (Timer) timer.get(mc);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setY_vec3d(Vec3d vec, double val) {
        try {
            y_vec3d.setDouble(vec, val);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static double getMinX(AxisAlignedBB bb) {
        try {
            return minX.getDouble(bb);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static double getMinY(AxisAlignedBB bb) {
        try {
            return minY.getDouble(bb);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static double getMinZ(AxisAlignedBB bb) {
        try {
            return minZ.getDouble(bb);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static double getMaxX(AxisAlignedBB bb) {
        try {
            return maxX.getDouble(bb);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static double getMaxY(AxisAlignedBB bb) {
        try {
            return maxY.getDouble(bb);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static double getMaxZ(AxisAlignedBB bb) {
        try {
            return maxZ.getDouble(bb);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setMinX(AxisAlignedBB bb, double val) {
        try {
            minX.setDouble(bb, val);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setMinY(AxisAlignedBB bb, double val) {
        try {
            minY.setDouble(bb, val);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setMinZ(AxisAlignedBB bb, double val) {
        try {
            minZ.setDouble(bb, val);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setMaxX(AxisAlignedBB bb, double val) {
        try {
            maxX.setDouble(bb, val);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setMaxY(AxisAlignedBB bb, double val) {
        try {
            maxY.setDouble(bb, val);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static void setMaxZ(AxisAlignedBB bb, double val) {
        try {
            maxZ.setDouble(bb, val);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static DataParameter<Byte> getPLAYER_MODEL_FLAG() {
        try {
            return (DataParameter<Byte>) PLAYER_MODEL_FLAG.get(null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static double getRenderPosX(RenderManager mgr) {
        try {
            return renderPosX.getDouble(mgr);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static double getRenderPosY(RenderManager mgr) {
        try {
            return renderPosY.getDouble(mgr);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static double getRenderPosZ(RenderManager mgr) {
        try {
            return renderPosZ.getDouble(mgr);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static double getRenderPosX() {
        try {
            return renderPosX.getDouble(mc.getRenderManager());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static double getRenderPosY() {
        try {
            return renderPosY.getDouble(mc.getRenderManager());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static double getRenderPosZ() {
        try {
            return renderPosZ.getDouble(mc.getRenderManager());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }

    public static boolean getSleeping(EntityPlayer mgr) {
        try {
            return sleeping.getBoolean(mgr);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(e);
        }
    }
}
