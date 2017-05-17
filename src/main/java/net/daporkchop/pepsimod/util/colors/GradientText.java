package net.daporkchop.pepsimod.util.colors;

import net.minecraft.client.gui.GuiScreen;

public class GradientText extends ColorizedText {
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
