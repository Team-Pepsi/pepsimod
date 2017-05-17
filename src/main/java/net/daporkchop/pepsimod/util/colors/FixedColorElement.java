package net.daporkchop.pepsimod.util.colors;

import net.minecraft.client.Minecraft;

public class FixedColorElement extends ColorizedElement {
    public final int color;

    public FixedColorElement(int color, String text) {
        this.color = color;
        this.text = text;
        this.width = Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
    }
}
