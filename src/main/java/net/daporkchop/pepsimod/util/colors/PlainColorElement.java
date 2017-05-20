package net.daporkchop.pepsimod.util.colors;

import net.daporkchop.pepsimod.PepsiMod;

public class PlainColorElement extends ColorizedElement {
    public PlainColorElement(String text) {
        this.text = text;
        this.width = PepsiMod.INSTANCE.mc.fontRenderer.getStringWidth(text);
    }
}
