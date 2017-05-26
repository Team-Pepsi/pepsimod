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
        this.offset = offset;
        String[] split = text.split(PepsiUtils.COLOR_ESCAPE + "custom");
        String[] split2 = split[0].split("");
        elements = new PlainColorElement[split2.length + (split.length > 1 ? 1 : 0)];
        for (int i = 0; i < split2.length; i++) {
            elements[i] = new PlainColorElement(split2[i]);
        }
        if (split.length > 1) {
            elements[elements.length - 1] = new FixedColorElement(Integer.parseInt(split[1].substring(0, Math.min(split[1].length(), 6)), 16), split[1].substring(6));
        }
        this.fontRenderer = PepsiMod.INSTANCE.mc.fontRenderer;
        this.width = this.fontRenderer.getStringWidth(text);
        this.text = text;
    }

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
            screen.drawString(Minecraft.getMinecraft().fontRenderer, element.text, x + i, y, color.getRGB());
            i += element.width;
        }
    }

    public void drawWithEndAtPos(Gui screen, int x, int y) {
        int i = 0;
        for (ColorizedElement element : elements) {
            i -= element.width;
        }
        RainbowCycle cycle = PepsiUtils.rainbowCycleBackwards(this.text.length(), PepsiUtils.rainbowCycle(offset, PepsiUtils.rainbowCycle.clone()));
        for (int j = 0; j < elements.length; j++) {
            ColorizedElement element = elements[j];
            if (element instanceof FixedColorElement) {
                screen.drawString(Minecraft.getMinecraft().fontRenderer, element.text, x + i, y, ((FixedColorElement) element).color);
                return;
            }
            cycle = PepsiUtils.rainbowCycle(1, cycle);
            Color color = new Color(PepsiUtils.ensureRange(cycle.r, 0, 255), PepsiUtils.ensureRange(cycle.g, 0, 255), PepsiUtils.ensureRange(cycle.b, 0, 255));
            screen.drawString(Minecraft.getMinecraft().fontRenderer, element.text, x + i, y, color.getRGB());
            i += element.width;
        }
        //new, broken version
        /*int i = 0;
        RainbowCycle cycle = PepsiUtils.rainbowCycle(offset, PepsiUtils.rainbowCycle.clone());
        for (int j = elements.length - 1; j >= 0; j--) {
            ColorizedElement element = elements[j];
            if (element instanceof FixedColorElement) {
                screen.drawString(PepsiMod.INSTANCE.mc.fontRenderer, element.text, x + i, y, ((FixedColorElement) element).color);
                return;
            }
            cycle = PepsiUtils.rainbowCycle(1, cycle);
            Color color = new Color(PepsiUtils.ensureRange(cycle.r, 0, 255), PepsiUtils.ensureRange(cycle.g, 0, 255), PepsiUtils.ensureRange(cycle.b, 0, 255));
            screen.drawString(PepsiMod.INSTANCE.mc.fontRenderer, element.text, x + i, y, color.getRGB());
            i -= element.width;
            System.out.println(i + " " + element.width);
        }*/
    }

    public void drawAtPos(Gui screen, int x, int y, int offset) {
        int tempOffset = this.offset;
        this.offset = offset;
        this.drawAtPos(screen, x, y);
        this.offset = tempOffset;
    }

    public void drawWithEndAtPos(Gui screen, int x, int y, int offset) {
        int tempOffset = this.offset;
        this.offset = offset;
        this.drawWithEndAtPos(screen, x, y);
        this.offset = tempOffset;
    }

    public int width() {
        return width;
    }
}
