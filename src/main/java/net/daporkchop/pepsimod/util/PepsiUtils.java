package net.daporkchop.pepsimod.util;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.util.colors.ColorizedText;
import net.daporkchop.pepsimod.util.colors.FixedColorElement;
import net.daporkchop.pepsimod.util.colors.GradientText;
import net.daporkchop.pepsimod.util.colors.rainbow.ColorChangeType;
import net.daporkchop.pepsimod.util.colors.rainbow.RainbowCycle;
import net.daporkchop.pepsimod.util.colors.rainbow.RainbowText;
import net.daporkchop.pepsimod.util.module.TargetBone;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.renderer.Vector3d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class PepsiUtils {
    public static final char COLOR_ESCAPE = '\u00A7';
    public static final String[] colorCodes = new String[]{"c", "9", "f", "1", "4"};
    public static final Timer timer = new Timer();
    public static final ServerData TOOBEETOOTEE_DATA = new ServerData("toobeetootee", "2b2t.org", false);
    private static final Random random = new Random(System.currentTimeMillis());
    public static String buttonPrefix = COLOR_ESCAPE + "c";
    public static RainbowCycle rainbowCycle = new RainbowCycle();
    public static Color RAINBOW_COLOR = new Color(0, 0, 0);
    public static ColorizedText PEPSI_NAME = new RainbowText("PepsiMod " + PepsiMod.VERSION);
    public static Field block_pepsimod_id = null;
    public static int protocolVersion = 335, versionIndex = 0;
    public static int[] protocols = new int[]{
            335, // 1.12
            338  // 1.12.1
    };
    public static GuiButton protocolSwitchButton = new GuiButton(11, 32, 6, 70, 20, "v" + protocolVersion);

    static {
        TOOBEETOOTEE_DATA.setResourceMode(ServerData.ServerResourceMode.PROMPT);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {// random colors
                PepsiUtils.buttonPrefix = PepsiUtils.COLOR_ESCAPE + colorCodes[PepsiUtils.rand(0, PepsiUtils.colorCodes.length)];
            }
        }, 1000, 1000);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
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
        /*if (toReturn != value)  {
            System.out.println("Changed value, old: " + value + ", new: " + toReturn);
        }*/
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

    public static boolean canEntityBeSeen(Entity entityIn, EntityPlayer player, TargetBone bone) {
        return entityIn.world.rayTraceBlocks(new Vec3d(player.posX, player.posY + (double) player.getEyeHeight(), player.posZ), new Vec3d(entityIn.posX, entityIn.posY + getTargetHeight(entityIn, bone), entityIn.posZ), false, true, false) == null;
    }

    public static double getTargetHeight(Entity entity, TargetBone bone) {
        double targetHeight = 0;
        if (bone == TargetBone.HEAD) {
            targetHeight = entity.getEyeHeight();
        } else if (bone == TargetBone.MIDDLE) {
            targetHeight = entity.getEyeHeight() / 2;
        }
        return targetHeight;
    }

    public static Vec3d adjustVectorForBone(Vec3d vec3d, Entity entity, TargetBone bone) {
        vec3d.y = getTargetHeight(entity, bone);
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

    public static AxisAlignedBB getBoundingBox(World world, BlockPos pos) {
        return world.getBlockState(pos).getBoundingBox(world, pos);
    }

    public static AxisAlignedBB offsetBB(AxisAlignedBB bb, BlockPos pos) {
        bb.minX += pos.getX();
        bb.maxX += pos.getX();
        bb.minY += pos.getY();
        bb.maxY += pos.getY();
        bb.minZ += pos.getZ();
        bb.maxZ += pos.getZ();
        return bb;
    }

    public static AxisAlignedBB unionBB(AxisAlignedBB bb1, AxisAlignedBB bb2) {
        bb1.minX = Math.min(bb1.minX, bb2.minX);
        bb1.minY = Math.min(bb1.minY, bb2.minY);
        bb1.minZ = Math.min(bb1.minZ, bb2.minZ);
        bb1.maxX = Math.max(bb1.maxX, bb2.maxX);
        bb1.maxY = Math.max(bb1.maxY, bb2.maxY);
        bb1.maxZ = Math.max(bb1.maxZ, bb2.maxZ);
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
        to.getDataManager().set(EntityPlayer.PLAYER_MODEL_FLAG, from.getDataManager().get(EntityPlayer.PLAYER_MODEL_FLAG));
    }

    public static void glColor(RenderColor color) {
        GL11.glColor4b(color.r, color.g, color.b, color.a);
    }

    public static boolean isThrowable(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof ItemBow || item instanceof ItemSnowball || item instanceof ItemEgg || item instanceof ItemEnderPearl || item instanceof ItemSplashPotion || item instanceof ItemLingeringPotion || item instanceof ItemFishingRod;
    }

    public static void updateProtocolButton() {
        protocolSwitchButton.displayString = "v" + protocolVersion;
    }
}
