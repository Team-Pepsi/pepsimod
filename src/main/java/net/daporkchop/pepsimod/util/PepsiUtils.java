package net.daporkchop.pepsimod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;
import org.apache.commons.lang3.ArrayUtils;

import java.awt.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class PepsiUtils {
    private static final Random random = new Random(System.currentTimeMillis());

    public static final char COLOR_ESCAPE = '\u00A7';
    public static String buttonPrefix = COLOR_ESCAPE + "c";
    public static final String[] colorCodes = new String[] {"c", "9", "f", "1", "4"};

    public static final Timer timer = new Timer();

    public static final ServerData TOOBEETOOTEE_DATA = new ServerData("toobeetootee", "2b2t.org", false);

    public static final GradientText PEPSIMOD_TEXT_GRADIENT = getGradientFromStringThroughColor("PepsiMod 11.0 for Minecraft 1.11.2", new Color(255, 0, 0), new Color(0, 0, 255), new Color(255, 255, 255));
    public static final GradientText PEPSIMOD_AUTHOR_GRADIENT = getGradientFromStringThroughColor("Made by DaPorkchop_ and LeafHacker", new Color(255, 0, 0), new Color(255, 0, 0), new Color(0, 0, 255));

    static {
        //TODO: rainbow filter hehehehehehhehehe
        // #RainbowsAreNotGay
        TOOBEETOOTEE_DATA.setResourceMode(ServerData.ServerResourceMode.PROMPT);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {// random colors
                PepsiUtils.buttonPrefix = PepsiUtils.COLOR_ESCAPE + colorCodes[PepsiUtils.rand(0, PepsiUtils.colorCodes.length)];
            }
        }, 1000, 1000);
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
     * @return a boolean random value either <code>true</code> or <code>false</code>
     */
    public static boolean rand() {
        return random.nextBoolean();
    }

    /**
     * Makes a gradient! Cache this, as it's quite resource-intensive
     * @param text the text to gradient-ify
     * @param color1 the starting color
     * @param color2 the ending color
     * @param through the color in the middle
     * @return a filled GradientText
     */
    public static GradientText getGradientFromStringThroughColor(String text, Color color1, Color color2, Color through)   {
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
        GradientElement[] elements = new GradientElement[charCount];
        Color[] merged = (Color[]) ArrayUtils.addAll(colorsPart1, colorsPart2);
        for (int i = 0; i < charCount; i++) {
            elements[i] = new GradientElement(merged[i].getRGB(), letters[i]);
        }
        return new GradientText(elements, Minecraft.getMinecraft().fontRenderer.getStringWidth(text));
    }

    public static int ceilDiv(int x, int y) {
        return Math.floorDiv(x, y) + (x % y == 0 ? 0 : 1);
    }

    public static int ensureRange(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }
}
