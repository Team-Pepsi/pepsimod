package net.daporkchop.pepsimod.util.colors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class GradientText extends ColorizedText {
    public final FixedColorElement[] elements;
    public final int width;

    public GradientText(FixedColorElement[] elements, int width) {
        this.elements = elements;
        this.width = width;
    }

    public void drawAtPos(Gui screen, int x, int y) {
        int i = 0;
        for (FixedColorElement element : elements) {
            screen.drawString(Minecraft.getMinecraft().fontRenderer, element.text, x + i, y, element.color);
            i += element.width;
        }
    }

    public void drawWithEndAtPos(Gui screen, int x, int y) {
        int i = 0;
        for (FixedColorElement element : elements) {
            i -= element.width;
        }
        for (FixedColorElement element : elements) {
            screen.drawString(Minecraft.getMinecraft().fontRenderer, element.text, x + i, y, element.color);
            i += element.width;
        }
    }

    public int width() {
        return width;
    }
}
