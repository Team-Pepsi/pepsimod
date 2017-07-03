package net.daporkchop.pepsimod.util.colors;

import net.minecraft.client.gui.Gui;

public abstract class ColorizedText {
    public abstract int width();

    public abstract void drawAtPos(Gui screen, int x, int y);
}
