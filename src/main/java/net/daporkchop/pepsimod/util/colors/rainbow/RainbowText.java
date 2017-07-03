package net.daporkchop.pepsimod.util.colors.rainbow;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.colors.ColorizedElement;
import net.daporkchop.pepsimod.util.colors.ColorizedText;
import net.daporkchop.pepsimod.util.colors.FixedColorElement;
import net.daporkchop.pepsimod.util.colors.PlainColorElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

import java.awt.*;

public class RainbowText extends ColorizedText {
    private final ColorizedElement[] elements;
    private final int width;
    private final FontRenderer fontRenderer;
    public String text;
    private int offset;

    public RainbowText(String text) {
        this(text, 0);
    }

    public RainbowText(String text, int offset) {
        this.fontRenderer = PepsiMod.INSTANCE.mc.fontRenderer;
        this.offset = offset;
        String[] split = text.split(PepsiUtils.COLOR_ESCAPE + "custom");
        String[] split2 = split[0].split("");
        elements = new ColorizedElement[split2.length + (split.length > 1 ? 1 : 0)];
        for (int i = 0; i < split2.length; i++) {
            elements[i] = new PlainColorElement(split2[i]);
        }
        if (split.length > 1) {
            elements[elements.length - 1] = new FixedColorElement(Integer.parseInt(split[1].substring(0, Math.min(split[1].length(), 6)), 16), split[1].substring(6));
            int i = 0;
            for (ColorizedElement element : elements) {
                i += element.width;
            }
            this.width = i;
            this.text = split[0] + split[1].substring(6);
        } else {
            this.width = this.fontRenderer.getStringWidth(text);
        }
        this.text = text;
    }

    //int debug = 0;
    public void drawAtPos(Gui screen, int x, int y) {
        int i = 0;
        RainbowCycle cycle = PepsiUtils.rainbowCycle(offset, PepsiUtils.rainbowCycle.clone());
        for (int j = 0; j < elements.length; j++) {
            ColorizedElement element = elements[j];
            if (element instanceof FixedColorElement) {
                screen.drawString(Minecraft.getMinecraft().fontRenderer, element.text, x + i, y, ((FixedColorElement) element).color);
                return;
            }
            cycle = PepsiUtils.rainbowCycle(1, cycle);
            Color color = new Color(PepsiUtils.ensureRange(cycle.r, 0, 255), PepsiUtils.ensureRange(cycle.g, 0, 255), PepsiUtils.ensureRange(cycle.b, 0, 255));
            /*if (color.getRed() != cycle.r)  {
                System.out.println("had to ensure range! original color " + cycle.r + ", actual " + color.getRed());
            }*/
            /*if (Math.abs(debug - color.getRed()) > 4)   {
                System.out.println("difference in one color tick was " + Math.abs(debug - color.getRed()));
            }
            debug = color.getRed();*/
            screen.drawString(Minecraft.getMinecraft().fontRenderer, element.text, x + i, y, color.getRGB());
            i += element.width;
        }
    }

    public void drawAtPos(Gui screen, int x, int y, int offset) {
        int tempOffset = this.offset;
        this.offset = offset;
        this.drawAtPos(screen, x, y);
        this.offset = tempOffset;
    }

    public int width() {
        return width;
    }
}
