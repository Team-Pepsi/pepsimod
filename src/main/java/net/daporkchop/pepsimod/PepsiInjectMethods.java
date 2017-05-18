package net.daporkchop.pepsimod;

import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.util.colors.ColorizedText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class PepsiInjectMethods {
    public static void drawPepsiStuffToMainMenu(int mouseX, int mouseY, float partialTicks, GuiScreen screen) {
        //draw stuff to menu
        //nothing here for now
    }

    public static void drawPepsiStuffToIngame(Gui gui, GuiNewChat chat, int counter, ColorizedText title, Minecraft minecraft) {
        GlStateManager.enableBlend();
        GlStateManager.enableAlpha();

        title.drawAtPos(gui, 2, 2);

        for (int i = 0; i < ModuleManager.ENABLED_MODULES.size(); i++) {
            Module module = ModuleManager.ENABLED_MODULES.get(i);
            module.text.drawAtPos(gui, 2, 22 + i * 10);
        }
        chat.drawChat(counter);

        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
    }
}
