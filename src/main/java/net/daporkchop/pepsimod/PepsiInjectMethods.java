package net.daporkchop.pepsimod;

import net.daporkchop.pepsimod.util.PepsiUtils;
import net.minecraft.client.gui.GuiScreen;

public class PepsiInjectMethods {
    public static void drawPepsiStuffToMainMenu(int mouseX, int mouseY, float partialTicks, GuiScreen screen)   {
        PepsiUtils.PEPSIMOD_TEXT_GRADIENT.drawAtPos(screen, 2, screen.height - 20);
        PepsiUtils.PEPSIMOD_AUTHOR_GRADIENT.drawAtPos(screen, 2, screen.height - 10);
        screen.drawString(screen.fontRenderer, "§4Copyright Mojang AB. Do not distribute!", screen.width - screen.fontRenderer.getStringWidth("Copyright Mojang AB. Do not distribute!") - 2, screen.height - 10, -1);
    }
}
