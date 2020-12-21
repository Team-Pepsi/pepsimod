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

import net.daporkchop.pepsimod.Pepsimod;
import net.daporkchop.pepsimod.optimization.BlockID;
import net.daporkchop.pepsimod.util.colors.ColorizedText;
import net.daporkchop.pepsimod.util.colors.FixedColorElement;
import net.daporkchop.pepsimod.util.colors.GradientText;
import net.daporkchop.pepsimod.util.colors.rainbow.RainbowText;
import net.daporkchop.pepsimod.util.config.impl.GeneralTranslator;
import net.daporkchop.pepsimod.util.config.impl.TargettingTranslator;
import net.daporkchop.pepsimod.util.misc.IWurstRenderListener;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.Vector3d;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemLingeringPotion;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemSplashPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import static net.daporkchop.lib.common.util.PorkUtil.*;

public class PepsiUtils extends PepsiConstants {
    public static final char COLOR_ESCAPE = '\u00A7';
    public static final String[] colorCodes = {"c", "9", "f", "1", "4"};
    public static final Timer timer = new Timer();
    public static final ServerData TOOBEETOOTEE_DATA = new ServerData("toobeetootee", "2b2t.org", false);
    public static final KeyBinding[] controls = {
            mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack,
            mc.gameSettings.keyBindRight, mc.gameSettings.keyBindLeft,
            mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSneak
    };
    public static final BufferedImage PEPSI_LOGO;
    public static String buttonPrefix = COLOR_ESCAPE + "c";
    public static RainbowText PEPSI_NAME = new RainbowText(Pepsimod.NAME_VERSION);
    public static ArrayList<IWurstRenderListener> wurstRenderListeners = new ArrayList<>();
    public static ArrayList<IWurstRenderListener> toRemoveWurstRenderListeners = new ArrayList<>();
    public static GuiButton reconnectButton;
    public static GuiButton autoReconnectButton;
    public static int autoReconnectWaitTime = 5;
    public static String lastIp;
    public static int lastPort;

    static {
        TOOBEETOOTEE_DATA.setResourceMode(ServerData.ServerResourceMode.PROMPT);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {// random colors
                PepsiUtils.buttonPrefix = PepsiUtils.COLOR_ESCAPE + colorCodes[ThreadLocalRandom.current().nextInt(PepsiUtils.colorCodes.length)];
            }
        }, 1000, 1000);

        timer.schedule(new TimerTask() { //autoreconnect
            @Override
            public void run() {
                if (mc.currentScreen != null && mc.currentScreen instanceof GuiDisconnected && autoReconnectButton != null && GeneralTranslator.INSTANCE.autoReconnect) {
                    autoReconnectButton.displayString = "AutoReconnect (\u00A7a" + --autoReconnectWaitTime + "\u00A7r)";
                    if (autoReconnectWaitTime <= 0) {
                        ServerData data = new ServerData("", lastIp + ':' + lastPort, false);
                        data.setResourceMode(ServerData.ServerResourceMode.PROMPT);
                        mc.addScheduledTask(() -> FMLClientHandler.instance().connectToServer(mc.currentScreen, data));
                        autoReconnectWaitTime = 5;
                    }
                }
            }
        }, 1000, 1000);

        BufferedImage pepsiLogo = null;
        try (InputStream in = PepsiUtils.class.getResourceAsStream("/pepsilogo.png")) {
            pepsiLogo = ImageIO.read(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            PEPSI_LOGO = pepsiLogo;
        }
    }

    /**
     * Makes a gradient! Cache this, as it's quite resource-intensive
     *
     * @param text    the text to gradient-ify
     * @param color1  the starting color
     * @param color2  the ending color
     * @param through the color in the middle
     * @return a filled GradientText
     */
    public static ColorizedText getGradientFromStringThroughColor(String text, Color color1, Color color2, Color through) {
        int charCount = text.length();
        String[] letters = text.split("");
        int colorCountPart1 = Math.floorDiv(charCount, 2);
        int colorCountPart2 = ceilDiv(charCount, 2);
        Color[] colorsPart1 = new Color[colorCountPart1];
        Color[] colorsPart2 = new Color[colorCountPart2];
        int rDiffStep = (color1.getRed() - through.getRed()) / colorCountPart1;
        int gDiffStep = (color1.getGreen() - through.getGreen()) / colorCountPart1;
        int bDiffStep = (color1.getBlue() - through.getBlue()) / colorCountPart1;
        for (int i = 0; i < colorCountPart1; i++) { //first step
            colorsPart1[i] = new Color(ensureRange(color1.getRed() + i * rDiffStep * -1, 0, 255), ensureRange(color1.getGreen() + i * gDiffStep * -1, 0, 255), ensureRange(color1.getBlue() + i * bDiffStep * -1, 0, 255));
        }
        rDiffStep = (through.getRed() - color2.getRed()) / colorCountPart2;
        gDiffStep = (through.getGreen() - color2.getGreen()) / colorCountPart2;
        bDiffStep = (through.getBlue() - color2.getBlue()) / colorCountPart2;
        for (int i = 0; i < colorCountPart2; i++) { //second step
            colorsPart2[i] = new Color(ensureRange(through.getRed() + i * rDiffStep * -1, 0, 255), ensureRange(through.getGreen() + i * gDiffStep * -1, 0, 255), ensureRange(through.getBlue() + i * bDiffStep * -1, 0, 255));
        }
        FixedColorElement[] elements = new FixedColorElement[charCount];
        Color[] merged = ArrayUtils.addAll(colorsPart1, colorsPart2);
        for (int i = 0; i < charCount; i++) {
            elements[i] = new FixedColorElement(merged[i].getRGB(), letters[i]);
        }
        return new GradientText(elements, Minecraft.getMinecraft().fontRenderer.getStringWidth(text));
    }

    public static int ceilDiv(int x, int y) {
        return Math.floorDiv(x, y) + (x % y == 0 ? 0 : 1);
    }

    public static int ensureRange(int value, int min, int max) {
        int toReturn = Math.min(Math.max(value, min), max);
        return toReturn;
    }

    public static boolean canEntityBeSeen(Entity entityIn, EntityPlayer player, TargettingTranslator.TargetBone bone) {
        return entityIn.world.rayTraceBlocks(new Vec3d(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ), new Vec3d(entityIn.posX, getTargetHeight(entityIn, bone), entityIn.posZ), false, true, false) == null;
    }

    public static double getTargetHeight(Entity entity, TargettingTranslator.TargetBone bone) {
        double targetHeight = entity.posY;
        if (bone == TargettingTranslator.TargetBone.HEAD) {
            targetHeight = entity.getEyeHeight();
        } else if (bone == TargettingTranslator.TargetBone.MIDDLE) {
            targetHeight = entity.getEyeHeight() / 2;
        }
        return targetHeight;
    }

    public static Vec3d adjustVectorForBone(Vec3d vec3d, Entity entity, TargettingTranslator.TargetBone bone) {
        ReflectionStuff.setY_vec3d(vec3d, getTargetHeight(entity, bone));
        return vec3d;
    }

    public static void setBlockIdFields() {
        Block.REGISTRY.forEach(block -> ((BlockID) block).internal_setBlockId(Block.REGISTRY.getIDForObject(block)));
    }

    public static AxisAlignedBB cloneBB(AxisAlignedBB bb) {
        return new AxisAlignedBB(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
    }

    public static AxisAlignedBB offsetBB(AxisAlignedBB bb, BlockPos pos) {
        ReflectionStuff.setMinX(bb, ReflectionStuff.getMinX(bb) + pos.getX());
        ReflectionStuff.setMinY(bb, ReflectionStuff.getMinY(bb) + pos.getY());
        ReflectionStuff.setMinZ(bb, ReflectionStuff.getMinZ(bb) + pos.getZ());
        ReflectionStuff.setMaxX(bb, ReflectionStuff.getMaxX(bb) + pos.getX());
        ReflectionStuff.setMaxY(bb, ReflectionStuff.getMaxY(bb) + pos.getY());
        ReflectionStuff.setMaxZ(bb, ReflectionStuff.getMaxZ(bb) + pos.getZ());
        return bb;
    }

    public static AxisAlignedBB unionBB(AxisAlignedBB bb1, AxisAlignedBB bb2) {
        ReflectionStuff.setMinX(bb1, Math.min(ReflectionStuff.getMinX(bb1), ReflectionStuff.getMinX(bb2)));
        ReflectionStuff.setMinY(bb1, Math.min(ReflectionStuff.getMinY(bb1), ReflectionStuff.getMinY(bb2)));
        ReflectionStuff.setMinZ(bb1, Math.min(ReflectionStuff.getMinZ(bb1), ReflectionStuff.getMinZ(bb2)));
        ReflectionStuff.setMaxX(bb1, Math.min(ReflectionStuff.getMaxX(bb1), ReflectionStuff.getMaxX(bb2)));
        ReflectionStuff.setMaxY(bb1, Math.min(ReflectionStuff.getMaxY(bb1), ReflectionStuff.getMaxY(bb2)));
        ReflectionStuff.setMaxZ(bb1, Math.min(ReflectionStuff.getMaxZ(bb1), ReflectionStuff.getMaxZ(bb2)));
        return bb1;
    }

    public static Vector3d sub(Vector3d in, Vector3d with) {
        in.x -= with.x;
        in.y -= with.y;
        in.z -= with.z;
        return in;
    }

    public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
        return new Vec3d(
                (entity.posX - entity.lastTickPosX) * x,
                (entity.posY - entity.lastTickPosY) * y,
                (entity.posZ - entity.lastTickPosZ) * z
        );
    }

    public static Vec3d getInterpolatedAmount(Entity entity, double ticks) {
        return getInterpolatedAmount(entity, ticks, ticks, ticks);
    }

    public static void copyPlayerModel(EntityPlayer from, EntityPlayer to) {
        to.getDataManager().set(ReflectionStuff.getPLAYER_MODEL_FLAG(), from.getDataManager().get(ReflectionStuff.getPLAYER_MODEL_FLAG()));
    }

    public static void glColor(RenderColor color) {
        GL11.glColor4b(color.r, color.g, color.b, color.a);
    }

    public static void glColor(Color color) {
        RenderColor.glColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static boolean isThrowable(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof ItemBow || item instanceof ItemSnowball || item instanceof ItemEgg || item instanceof ItemEnderPearl || item instanceof ItemSplashPotion || item instanceof ItemLingeringPotion || item instanceof ItemFishingRod;
    }

    public static float round(float input, float step) {
        return ((Math.round(input / step)) * step);
    }

    public static float ensureRange(float value, float min, float max) {
        float toReturn = Math.min(Math.max(value, min), max);
        return toReturn;
    }

    public static String roundFloatForSlider(float f) {
        return String.format("%.2f", f);
    }

    public static String roundCoords(double d) {
        return String.format("%.2f", d);
    }

    public static String getFacing() {
        Entity entity = mc.getRenderViewEntity();
        EnumFacing enumfacing = entity.getHorizontalFacing();
        String s = "Invalid";

        switch (enumfacing) {
            case NORTH:
                s = "-Z";
                break;
            case SOUTH:
                s = "+Z";
                break;
            case WEST:
                s = "-X";
                break;
            case EAST:
                s = "+X";
        }

        return s;
    }

    public static void renderItem(int x, int y, float partialTicks, EntityPlayer player, ItemStack stack) {
        if (!stack.isEmpty()) {
            GlStateManager.pushMatrix();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            try {
                GlStateManager.translate(0.0F, 0.0F, 32.0F);
                mc.getRenderItem().zLevel = 200F;
                mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
                mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRenderer, stack, x, y, "");
                mc.getRenderItem().zLevel = 0F;
            } catch (Exception e) {
                e.printStackTrace();
            }
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popMatrix();
        }
    }

    public static ItemStack getWearingArmor(int armorType) {
        return mc.player.inventoryContainer.getSlot(5 + armorType).getStack();
    }

    public static void drawNameplateNoScale(FontRenderer fontRendererIn, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, float offset, float size) {
        GlStateManager.pushMatrix();

        double dist = new Vec3d(x, y + offset, z).length();
        GlStateManager.translate(x, y + offset, z);

        GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-viewerYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float) (isThirdPersonFrontal ? -1 : 1) * viewerPitch, 1.0F, 0.0F, 0.0F);
        size *= dist * 0.3d;
        GlStateManager.scale(-0.025F * size, -0.025F * size, 0.025F * size);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);

        GlStateManager.disableDepth();

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        int i = fontRendererIn.getStringWidth(str) / 2;
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double) (-i - 1), (double) (-8 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        bufferbuilder.pos((double) (-i - 1), (double) (1 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        bufferbuilder.pos((double) (i + 1), (double) (1 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        bufferbuilder.pos((double) (i + 1), (double) (-8 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();

        int color = 0xFFFFFFFF;
        GlStateManager.enableDepth();

        GlStateManager.depthMask(true);
        fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift - 7, color);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();

        //TODO: draw items in name tag
    }

    public static int getBestTool(Block block) {
        float best = -1.0F;
        int index = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                float str = itemStack.getItem().getDestroySpeed(itemStack, block.getDefaultState());
                if (str > best) {
                    best = str;
                    index = i;
                }
            }
        }
        return index;
    }

    public static double getDimensionCoord(double coord) {
        return mc.player.dimension == 0 ? coord / 8 : coord * 8;
    }

    public static int getArmorType(ItemArmor armor) {
        return armor.armorType.ordinal() - 2;
    }

    public static double[] interpolate(Entity entity) {
        double partialTicks = ReflectionStuff.getTimer().renderPartialTicks;
        double[] pos = {entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks, entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks, entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks};

        return pos;
    }

    public static boolean isAttackable(EntityLivingBase entity) {
        return entity != null && entity != mc.player && entity.isEntityAlive();
    }

    public static EntityLivingBase getClosestEntityWithoutReachFactor() {
        EntityLivingBase closestEntity = null;
        double distance = 9999.0D;
        for (Object object : mc.world.loadedEntityList) {
            if ((object instanceof EntityLivingBase)) {
                EntityLivingBase entity = (EntityLivingBase) object;
                if (isAttackable(entity)) {
                    double newDistance = mc.player.getDistanceSq(entity);
                    if (closestEntity != null) {
                        if (distance > newDistance) {
                            closestEntity = entity;
                            distance = newDistance;
                        }
                    } else {
                        closestEntity = entity;
                        distance = newDistance;
                    }
                }
            }
        }
        return closestEntity;
    }

    public static boolean isControlsPressed() {
        for (KeyBinding keyBinding : controls) {
            if (ReflectionStuff.getPressed(keyBinding)) {
                return true;
            }
        }
        return false;
    }

    public static void drawRect(float paramXStart, float paramYStart, float paramXEnd, float paramYEnd, int paramColor) {
        float alpha = (float) (paramColor >> 24 & 0xFF) / 255F;
        float red = (float) (paramColor >> 16 & 0xFF) / 255F;
        float green = (float) (paramColor >> 8 & 0xFF) / 255F;
        float blue = (float) (paramColor & 0xFF) / 255F;
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);

        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(paramXEnd, paramYStart);
        GL11.glVertex2d(paramXStart, paramYStart);
        GL11.glVertex2d(paramXStart, paramYEnd);
        GL11.glVertex2d(paramXEnd, paramYEnd);
        GL11.glEnd();

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glPopMatrix();
    }

    public static Vec3d getPlayerPos(float partialTicks) {
        return getEntityPos(partialTicks, mc.player);
    }

    public static Vec3d getEntityPos(float partialTicks, Entity entity) {
        if (partialTicks == 1.0F) {
            return new Vec3d(entity.posX, entity.posY, entity.posZ);
        } else {
            double x = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
            double y = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks;
            double z = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
            return new Vec3d(x, y, z);
        }
    }

}
