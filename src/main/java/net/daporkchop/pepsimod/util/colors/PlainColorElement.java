package net.daporkchop.pepsimod.util.colors;

import net.minecraft.client.Minecraft;

public class PlainColorElement extends ColorizedElement {
    public PlainColorElement(String text) {
        this.text = text;
        this.width = Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
    }
}
