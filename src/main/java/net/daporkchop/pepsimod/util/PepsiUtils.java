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
import net.daporkchop.pepsimod.util.colors.ColorizedText;
import net.daporkchop.pepsimod.util.colors.FixedColorElement;
import net.daporkchop.pepsimod.util.colors.GradientText;
import net.daporkchop.pepsimod.util.colors.rainbow.ColorChangeType;
import net.daporkchop.pepsimod.util.colors.rainbow.RainbowCycle;
import net.daporkchop.pepsimod.util.colors.rainbow.RainbowText;
import net.daporkchop.pepsimod.util.config.impl.GeneralTranslator;
import net.daporkchop.pepsimod.util.config.impl.TargettingTranslator;
import net.daporkchop.pepsimod.util.misc.Default;
import net.daporkchop.pepsimod.util.misc.ITickListener;
import net.daporkchop.pepsimod.util.misc.IWurstRenderListener;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class PepsiUtils extends Default {
    public static final char COLOR_ESCAPE = '\u00A7';
    public static final String[] colorCodes = new String[]{"c", "9", "f", "1", "4"};
    public static final Timer timer = new Timer();
    public static final ServerData TOOBEETOOTEE_DATA = new ServerData("toobeetootee", "2b2t.org", false);
    public static final String[] PEPSI_MEMBERS = new String[]{
            "8f8cef60-1f3a-4778-849d-5dab58c46639",
            "a4f77739-d15e-4dc2-b957-219a2f6f9244",
            "2f8731ca-c2a7-454e-85b6-6d072ed199c1",
            "4c8e844e-43ab-4d39-a62c-56fc02dda031",
            "4f27dd06-b5e1-44ff-8edf-2c5135b74489",
            "65f815b5-17b0-4c68-9aa3-91b68379fc6d",
            "266cae9f-b230-4fa6-b4d3-66c17755e3e5",
            "2d9d43d8-cb19-48a1-85c1-c7ccb2676d85",
            "02a8e07d-ce7f-46e2-a7ab-e994a940ae73",
            "8c7f2df9-48df-460c-853f-26b98bfd160f",
            "d16acb1b-8fb2-46dd-a561-4e9b9b557523",
            "95ddfd3a-d40e-4fc8-a408-d3ee6995706d",
            "a3166d0a-bcfd-406c-9ca8-4693e771d7e2",
            "26d7502f-fa48-4e0c-a3fd-637433f42f80",
            "475b83d1-5189-4bb8-88d4-f2922c0c8d58",
            "71832324-a62f-4ed4-a86e-4c4b8a3226dd",
            "90c66ec2-d931-4f1f-b2da-328afc9fe854",
            "69427358-99e6-4e4e-94ed-4a669ba6a8da",
            "0c3959df-4667-4bc5-b47c-a756689764f4",
            "a3b69979-9248-4a2e-979f-bc992b29a9f6",
            "773e431a-cc3a-41d4-90db-e749f579fff8",
            "b372c514-1d6a-4f86-b1c9-f06bac38690a",
            "c88f6974-a324-4e85-bd54-975a6aa03e75",
            "4b052543-9f20-4636-939e-d8dc05a53b3f",
            "4fd69819-b260-4ff4-af8a-172073fa7d5f",
            "fdee323e-7f0c-4c15-8d1c-0f277442342a",
            "8c7f2df9-48df-460c-853f-26b98bfd160f",
            "49f99d0a-cd48-4faa-a72e-d3b1552e95f1",
            "8034d01d-bc3b-49e2-a6f6-29455d0a5f24",
            "104f192a-0f3e-41e3-b574-919cc931559a",
            "6a711553-4287-478b-9b84-9ec1e01715a2",
            "4495eebb-7a4e-43aa-9784-02ea86f705ed",
            "1e8c7d13-9118-41e2-b334-fdb213970135"
    };
    public static final KeyBinding[] controls = new KeyBinding[]{
            mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack,
            mc.gameSettings.keyBindRight, mc.gameSettings.keyBindLeft,
            mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSneak};
    private static final Random random = new Random(System.currentTimeMillis());
    public static String buttonPrefix = COLOR_ESCAPE + "c";
    public static RainbowCycle rainbowCycle = new RainbowCycle();
    public static Color RAINBOW_COLOR = new Color(0, 0, 0);
    public static RainbowText PEPSI_NAME = new RainbowText("PepsiMod " + PepsiMod.VERSION);
    public static Field block_pepsimod_id = null;
    public static ArrayList<ITickListener> tickListeners = new ArrayList<>();
    public static ArrayList<ITickListener> toRemoveTickListeners = new ArrayList<>();
    public static ArrayList<IWurstRenderListener> wurstRenderListeners = new ArrayList<>();
    public static ArrayList<IWurstRenderListener> toRemoveWurstRenderListeners = new ArrayList<>();
    public static GuiButton reconnectButton, autoReconnectButton;
    public static int autoReconnectWaitTime = 5;
    public static String lastIp;
    public static int lastPort;

    static {
        TOOBEETOOTEE_DATA.setResourceMode(ServerData.ServerResourceMode.PROMPT);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {// random colors
                PepsiUtils.buttonPrefix = PepsiUtils.COLOR_ESCAPE + colorCodes[PepsiUtils.rand(0, PepsiUtils.colorCodes.length)];
            }
        }, 1000, 1000);

        timer.schedule(new TimerTask() { //autoreconnect
            @Override
            public void run() {
                if (mc.currentScreen != null && mc.currentScreen instanceof GuiDisconnected && autoReconnectButton != null && GeneralTranslator.INSTANCE.autoReconnect) {
                    autoReconnectButton.displayString = "AutoReconnect (\u00A7a" + --autoReconnectWaitTime + "\u00A7r)";
                    if (autoReconnectWaitTime == 0) {
                        ServerData data = new ServerData("", lastIp + ":" + lastPort, false);
                        data.setResourceMode(ServerData.ServerResourceMode.PROMPT);
                        FMLClientHandler.instance().connectToServer(mc.currentScreen, data);
                        autoReconnectWaitTime = 5;
                    }
                }
            }
        }, 1000, 1000);

        timer.schedule(new TimerTask() {
            @Override
            public void run() { //rainbow
                //red
                if (rainbowCycle.red == ColorChangeType.INCREASE) {
                    rainbowCycle.r += 4;
                    if (rainbowCycle.r > 255) {
                        rainbowCycle.red = ColorChangeType.DECRASE;
                        rainbowCycle.green = ColorChangeType.INCREASE;
                    }
                } else if (rainbowCycle.red == ColorChangeType.DECRASE) {
                    rainbowCycle.r -= 4;
                    if (rainbowCycle.r == 0) {
                        rainbowCycle.red = ColorChangeType.NONE;
                    }
                }
                //green
                if (rainbowCycle.green == ColorChangeType.INCREASE) {
                    rainbowCycle.g += 4;
                    if (rainbowCycle.g > 255) {
                        rainbowCycle.green = ColorChangeType.DECRASE;
                        rainbowCycle.blue = ColorChangeType.INCREASE;
                    }
                } else if (rainbowCycle.green == ColorChangeType.DECRASE) {
                    rainbowCycle.g -= 4;
                    if (rainbowCycle.g == 0) {
                        rainbowCycle.green = ColorChangeType.NONE;
                    }
                }
                //blue
                if (rainbowCycle.blue == ColorChangeType.INCREASE) {
                    rainbowCycle.b += 4;
                    if (rainbowCycle.b > 255) {
                        rainbowCycle.blue = ColorChangeType.DECRASE;
                        rainbowCycle.red = ColorChangeType.INCREASE;
                    }
                } else if (rainbowCycle.blue == ColorChangeType.DECRASE) {
                    rainbowCycle.b -= 4;
                    if (rainbowCycle.b == 0) {
                        rainbowCycle.blue = ColorChangeType.NONE;
                    }
                }
                RAINBOW_COLOR = new Color(ensureRange(rainbowCycle.r, 0, 255), ensureRange(rainbowCycle.g, 0, 255), ensureRange(rainbowCycle.b, 0, 255));
            }
        }, 0, 50);
    }

    /**
     * Returns a random number between min (inkl.) and max (excl.) If you want a number between 1 and 4 (inkl) you need to call rand (1, 5)
     *
     * @param min min inklusive value
     * @param max max exclusive value
     * @return
     */
    public static int rand(int min, int max) {
        if (min == max) {
            return max;
        }
        return min + random.nextInt(max - min);
    }

    /**
     * Returns random boolean
     *
     * @return a boolean random value either <code>true</code> or <code>false</code>
     */
    public static boolean rand() {
        return random.nextBoolean();
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
        int colorCountPart1 = Math.floorDiv(charCount, 2), colorCountPart2 = ceilDiv(charCount, 2);
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

    public static RainbowCycle rainbowCycle(int count, RainbowCycle toRunOn) {
        for (int i = 0; i < count; i++) {
            //red
            if (toRunOn.red == ColorChangeType.INCREASE) {
                toRunOn.r += 4;
                if (toRunOn.r > 255) {
                    toRunOn.red = ColorChangeType.DECRASE;
                    toRunOn.green = ColorChangeType.INCREASE;
                }
            } else if (toRunOn.red == ColorChangeType.DECRASE) {
                toRunOn.r -= 4;
                if (toRunOn.r == 0) {
                    toRunOn.red = ColorChangeType.NONE;
                }
            }
            //green
            if (toRunOn.green == ColorChangeType.INCREASE) {
                toRunOn.g += 4;
                if (toRunOn.g > 255) {
                    toRunOn.green = ColorChangeType.DECRASE;
                    toRunOn.blue = ColorChangeType.INCREASE;
                }
            } else if (toRunOn.green == ColorChangeType.DECRASE) {
                toRunOn.g -= 4;
                if (toRunOn.g == 0) {
                    toRunOn.green = ColorChangeType.NONE;
                }
            }
            //blue
            if (toRunOn.blue == ColorChangeType.INCREASE) {
                toRunOn.b += 4;
                if (toRunOn.b > 255) {
                    toRunOn.blue = ColorChangeType.DECRASE;
                    toRunOn.red = ColorChangeType.INCREASE;
                }
            } else if (toRunOn.blue == ColorChangeType.DECRASE) {
                toRunOn.b -= 4;
                if (toRunOn.b == 0) {
                    toRunOn.blue = ColorChangeType.NONE;
                }
            }
        }
        return toRunOn;
    }

    public static RainbowCycle rainbowCycleBackwards(int count, RainbowCycle toRunOn) {
        for (int i = 0; i < count; i++) {
            //red
            if (toRunOn.red == ColorChangeType.INCREASE) { //decrease value
                toRunOn.r -= 8;
                if (toRunOn.r == 0) {
                    toRunOn.red = ColorChangeType.NONE;
                }
            } else if (toRunOn.red == ColorChangeType.DECRASE) {
                toRunOn.r += 8;
                if (toRunOn.r > 255) {
                    toRunOn.red = ColorChangeType.INCREASE;
                    toRunOn.green = ColorChangeType.DECRASE;
                }
            }

            //green
            if (toRunOn.green == ColorChangeType.INCREASE) { //decrease value
                toRunOn.g -= 8;
                if (toRunOn.g == 0) {
                    toRunOn.green = ColorChangeType.NONE;
                }
            } else if (toRunOn.green == ColorChangeType.DECRASE) {
                toRunOn.g += 8;
                if (toRunOn.g > 255) {
                    toRunOn.green = ColorChangeType.INCREASE;
                    toRunOn.blue = ColorChangeType.DECRASE;
                }
            }

            //blue
            if (toRunOn.blue == ColorChangeType.INCREASE) { //decrease value
                toRunOn.b -= 8;
                if (toRunOn.b == 0) {
                    toRunOn.blue = ColorChangeType.NONE;
                }
            } else if (toRunOn.blue == ColorChangeType.DECRASE) {
                toRunOn.b += 8;
                if (toRunOn.b > 255) {
                    toRunOn.blue = ColorChangeType.INCREASE;
                    toRunOn.red = ColorChangeType.DECRASE;
                }
            }
        }
        return toRunOn;
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
        try {
            if (block_pepsimod_id != null) {
                return;
            }
            Class clazz = Block.class;
            block_pepsimod_id = clazz.getDeclaredField("pepsimod_id");
            Method setPepsimod_id = clazz.getDeclaredMethod("setPepsimod_id");
            Block.REGISTRY.forEach((block -> {
                try {
                    setPepsimod_id.invoke(block);
                } catch (Throwable e) {
                    e.printStackTrace();
                    Runtime.getRuntime().exit(8742043);
                }
            }));
        } catch (Throwable e) {
            e.printStackTrace();
            Runtime.getRuntime().exit(2349573);
        }
    }

    /**
     * this is important because getting a block id normally involves iterating through every object in the block registry
     * in a modded environment there might be 1000s of entrys there
     * so if we can get the id like this, it saves us lots of Time™
     */
    public static int getBlockId(Block block) {
        try {
            return (int) block_pepsimod_id.get(block);
        } catch (Throwable e) {
            e.printStackTrace();
            Runtime.getRuntime().exit(97348562);
        }
        return -1; //the code will NEVER get here!
        // thanks java for forcing me to add this
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

    public static Vector3d vector3d(BlockPos pos) {
        Vector3d v3d = new Vector3d();
        v3d.x = pos.getX();
        v3d.y = pos.getY();
        v3d.z = pos.getZ();
        return v3d;
    }

    public static Vector3d sub(Vector3d in, Vector3d with) {
        in.x -= with.x;
        in.y -= with.y;
        in.z -= with.z;
        return in;
    }

    public static int toRGBA(int r, int g, int b, int a) {
        return (r << 16) + (g << 8) + (b << 0) + (a << 24);
    }

    public static Vec3d getInterpolatedPos(Entity entity, float ticks) {
        return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(getInterpolatedAmount(entity, ticks));
    }

    public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
        return new Vec3d(
                (entity.posX - entity.lastTickPosX) * x,
                (entity.posY - entity.lastTickPosY) * y,
                (entity.posZ - entity.lastTickPosZ) * z
        );
    }

    public static Vec3d getInterpolatedAmount(Entity entity, Vec3d vec) {
        return getInterpolatedAmount(entity, vec.x, vec.y, vec.z);
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

    public static boolean isPepsimodPlayer(String uuid) {
        for (String s : PEPSI_MEMBERS) {
            if (s.equals(uuid)) {
                return true;
            }
        }

        return false;
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

    public static void drawNameplateNoScale(FontRenderer fontRendererIn, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, boolean isSneaking) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-viewerYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float) (isThirdPersonFrontal ? -1 : 1) * viewerPitch, 1.0F, 0.0F, 0.0F);

        isSneaking = false;

        double distance = Math.max(1.6, mc.getRenderViewEntity().getDistanceToEntity(mc.player) / 4);
        distance /= 100;
        GlStateManager.scale(-distance, -distance, distance);

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
        bufferbuilder.pos((double) (-i - 1), (double) (-1 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        bufferbuilder.pos((double) (-i - 1), (double) (8 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        bufferbuilder.pos((double) (i + 1), (double) (8 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        bufferbuilder.pos((double) (i + 1), (double) (-1 + verticalShift), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();

        fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift, 553648127);
        GlStateManager.enableDepth();

        GlStateManager.depthMask(true);
        fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2, verticalShift, isSneaking ? 553648127 : -1);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
        //TODO: draw items in name tag
    }

    /**
     * Renders floating lines of text in the 3D world at a specific position.
     *
     * @param text                  The string array of text to render
     * @param x                     X coordinate in the game world
     * @param y                     Y coordinate in the game world
     * @param z                     Z coordinate in the game world
     * @param color                 0xRRGGBB text color
     * @param renderBlackBackground render a pretty black border behind the text?
     * @param partialTickTime       Usually taken from RenderWorldLastEvent.partialTicks variable
     */
    public static void renderFloatingText(String text, float x, float y, float z, int color, boolean renderBlackBackground, float partialTickTime) {
        RenderManager renderManager = mc.getRenderManager();

        float playerX = (float) (mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * partialTickTime);
        float playerY = (float) (mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * partialTickTime);
        float playerZ = (float) (mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * partialTickTime);

        float dx = x - playerX;
        float dy = y - playerY;
        float dz = z - playerZ;
        float distanceRatio = (float) (5 / mc.player.getDistance(x, y, z));
        dx *= distanceRatio;
        dy *= distanceRatio;
        dz *= distanceRatio;
        float scale = 0.03f;

        GL11.glColor4f(1f, 1f, 1f, 0.5f);
        GL11.glPushMatrix();
        GL11.glTranslatef(dx, dy, dz);
        GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

        GL11.glScalef(-scale, -scale, scale);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        int textWidth = mc.fontRenderer.getStringWidth(text);

        int lineHeight = 10;

        if (renderBlackBackground) {
            int stringMiddle = textWidth / 2;

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder buffer = tessellator.getBuffer();

            GlStateManager.disableTexture2D();
            buffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            buffer.pos(-stringMiddle - 1, -1, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            buffer.pos(-stringMiddle - 1, 8 + lineHeight - lineHeight, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            buffer.pos(stringMiddle + 1, 8 + lineHeight - lineHeight, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            buffer.pos(stringMiddle + 1, -1, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();

            tessellator.draw();
            GlStateManager.enableTexture2D();
        }

        mc.fontRenderer.drawString(text, -textWidth / 2, 0, color);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPopMatrix();
    }

    /**
     * Renders an Item icon in the 3D world at the specified coordinates
     *
     * @param item
     * @param x
     * @param y
     * @param z
     * @param partialTickTime
     */
    public static void renderFloatingItemIcon(float x, float y, float z, Item item, float partialTickTime) {
        RenderManager renderManager = mc.getRenderManager();

        float playerX = (float) (mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * partialTickTime);
        float playerY = (float) (mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * partialTickTime);
        float playerZ = (float) (mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * partialTickTime);

        float dx = x - playerX;
        float dy = y - playerY;
        float dz = z - playerZ;
        float scale = 0.025f;

        GL11.glColor4f(1f, 1f, 1f, 0.75f);
        GL11.glPushMatrix();
        GL11.glTranslatef(dx, dy, dz);
        GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-scale, -scale, scale);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        renderItemTexture(-8, -8, item, 16, 16);

        GL11.glColor4f(1f, 1f, 1f, 1f);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPopMatrix();
    }

    public static void renderItemTexture(int x, int y, Item item, int width, int height) {
        IBakedModel iBakedModel = mc.getRenderItem().getItemModelMesher().getItemModel(new ItemStack(item));
        TextureAtlasSprite textureAtlasSprite = mc.getTextureMapBlocks().getAtlasSprite(iBakedModel.getParticleTexture().getIconName());
        mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        renderTexture(x, y, textureAtlasSprite, width, height, 0);
    }

    /**
     * Renders a previously bound texture (with mc.getTextureManager().bindTexture())
     *
     * @param x
     * @param y
     * @param textureAtlasSprite
     * @param width
     * @param height
     * @param zLevel
     */
    private static void renderTexture(int x, int y, TextureAtlasSprite textureAtlasSprite, int width, int height, double zLevel) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldrenderer = tessellator.getBuffer();

        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);

        worldrenderer.pos((double) (x), (double) (y + height), zLevel).tex((double) textureAtlasSprite.getMaxU(), (double) textureAtlasSprite.getMaxV()).endVertex();
        worldrenderer.pos((double) (x + width), (double) (y + height), zLevel).tex((double) textureAtlasSprite.getMinU(), (double) textureAtlasSprite.getMaxV()).endVertex();
        worldrenderer.pos((double) (x + width), (double) (y), zLevel).tex((double) textureAtlasSprite.getMinU(), (double) textureAtlasSprite.getMinV()).endVertex();
        worldrenderer.pos((double) (x), (double) (y), zLevel).tex((double) textureAtlasSprite.getMaxU(), (double) textureAtlasSprite.getMinV()).endVertex();

        tessellator.draw();
    }

    public static int getBestTool(Block block) {
        float best = -1.0F;
        int index = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack != null) {
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
        if (mc.player.dimension == 0) {
            return coord / 8;
        } else {
            return coord * 8;
        }
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
                    double newDistance = mc.player.getDistanceSqToEntity(entity);
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
}
