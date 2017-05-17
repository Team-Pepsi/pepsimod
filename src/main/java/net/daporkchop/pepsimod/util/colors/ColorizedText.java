package net.daporkchop.pepsimod.util.colors;

import net.minecraft.client.gui.GuiScreen;

public abstract class ColorizedText {
    public abstract void drawAtPos(GuiScreen screen, int x, int y);

    public abstract void drawWithEndAtPos(GuiScreen screen, int x, int y);
}
