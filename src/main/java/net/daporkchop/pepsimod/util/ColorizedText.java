package net.daporkchop.pepsimod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

public abstract class ColorizedText {
    public abstract void drawAtPos(GuiScreen screen, int x, int y);

    public abstract void drawWithEndAtPos(GuiScreen screen, int x, int y);
}

abstract class ColorizedElement {
    public int width;
    public String text;
}

class GradientText extends ColorizedText {
    public final FixedColorElement[] elements;
    public final int width;

    public GradientText(FixedColorElement[] elements, int width) {
        this.elements = elements;
        this.width = width;
    }

    public void drawAtPos(GuiScreen screen, int x, int y) {
        int i = 0;
        for (FixedColorElement element : elements) {
            screen.drawString(screen.fontRenderer, element.text, x + i, y, element.color);
            i += element.width;
        }
    }

    public void drawWithEndAtPos(GuiScreen screen, int x, int y) {
        int i = 0;
        for (FixedColorElement element : elements) {
            i -= element.width;
        }
        for (FixedColorElement element : elements) {
            screen.drawString(screen.fontRenderer, element.text, x + i, y, element.color);
            i += element.width;
        }
    }
}

class RainbowText extends ColorizedText {
    private final PlainColorElement[] elements;
    private final int width;
    private final FontRenderer fontRenderer;
    private int offset;

    public RainbowText(String text) {
        this(text, 0);
    }

    public RainbowText(String text, int offset) {
        this.offset = offset;
        String[] split = text.split("");
        elements = new PlainColorElement[split.length];
        for (int i = 0; i < split.length; i++) {
            elements[i] = new PlainColorElement(split[i]);
        }
        this.fontRenderer = Minecraft.getMinecraft().fontRenderer;
        this.width = this.fontRenderer.getStringWidth(text);
    }

    public void drawAtPos(GuiScreen screen, int x, int y) {
        int i = 0;
        RainbowCycle cycle = PepsiUtils.rainbowCycle(offset,PepsiUtils.rainbowCycle.clone());
        for (int j = 0; j < elements.length; j++) {
            PlainColorElement element = elements[j];
            cycle = PepsiUtils.rainbowCycle(1, cycle);
            Color color = new Color(PepsiUtils.ensureRange(cycle.r, 0, 255), PepsiUtils.ensureRange(cycle.g, 0, 255), PepsiUtils.ensureRange(cycle.b, 0, 255));
            screen.drawString(screen.fontRenderer, element.text, x + i, y, color.getRGB());
            i += element.width;
        }
    }

    public void drawWithEndAtPos(GuiScreen screen, int x, int y) {
        int i = 0;
        for (PlainColorElement element : elements) {
            i -= element.width;
        }
        RainbowCycle cycle = PepsiUtils.rainbowCycle(offset,PepsiUtils.rainbowCycle.clone());
        for (int j = 0; j < elements.length; j++) {
            PlainColorElement element = elements[j];
            cycle = PepsiUtils.rainbowCycle(1, cycle);
            Color color = new Color(PepsiUtils.ensureRange(cycle.r, 0, 255), PepsiUtils.ensureRange(cycle.g, 0, 255), PepsiUtils.ensureRange(cycle.b, 0, 255));
            screen.drawString(screen.fontRenderer, element.text, x + i, y, color.getRGB());
            i += element.width;
        }
    }
}

class FixedColorElement extends ColorizedElement {
    public final int color;

    public FixedColorElement(int color, String text) {
        this.color = color;
        this.text = text;
        this.width = Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
    }
}

class PlainColorElement extends ColorizedElement {
    public PlainColorElement(String text) {
        this.text = text;
        this.width = Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
    }
}
