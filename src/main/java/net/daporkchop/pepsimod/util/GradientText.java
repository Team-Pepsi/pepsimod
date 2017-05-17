package net.daporkchop.pepsimod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class GradientText {
    public final GradientElement[] elements;
    public final int width;

    public GradientText(GradientElement[] elements, int width)  {
        this.elements = elements;
        this.width = width;
    }

    public void drawAtPos(GuiScreen screen, int x, int y)   {
        int i = 0;
        for (GradientElement element : elements)    {
            screen.drawString(screen.fontRenderer, element.text, x + i, y, element.color);
            i += element.width;
        }
    }

    public void drawWithEndAtPos(GuiScreen screen, int x, int y) {
        int i = 0;
        for (GradientElement element : elements)    {
            i -= element.width;
        }
        for (GradientElement element : elements) {
            screen.drawString(screen.fontRenderer, element.text, x + i, y, element.color);
            i += element.width;
        }
    }
}

class GradientElement {
    public final int color, width;
    public final String text;

    public GradientElement(int color, String text) {
        this.color = color;
        this.text = text;
        this.width = Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
    }
}
