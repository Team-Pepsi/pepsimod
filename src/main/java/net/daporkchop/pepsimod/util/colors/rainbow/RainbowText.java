package net.daporkchop.pepsimod.util.colors.rainbow;

import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.colors.ColorizedText;
import net.daporkchop.pepsimod.util.colors.PlainColorElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

public class RainbowText extends ColorizedText {
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
