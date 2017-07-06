package net.daporkchop.pepsimod.util;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.util.colors.ColorizedText;
import net.daporkchop.pepsimod.util.colors.FixedColorElement;
import net.daporkchop.pepsimod.util.colors.GradientText;
import net.daporkchop.pepsimod.util.colors.rainbow.ColorChangeType;
import net.daporkchop.pepsimod.util.colors.rainbow.RainbowCycle;
import net.daporkchop.pepsimod.util.colors.rainbow.RainbowText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.ArrayUtils;

import java.awt.*;
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
        vec3d.yCoord = getTargetHeight(entity, bone);
        return vec3d;
    }
}
