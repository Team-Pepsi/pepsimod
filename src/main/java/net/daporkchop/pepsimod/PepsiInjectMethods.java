/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017 Team Pepsi
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from Team Pepsi.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: Team Pepsi), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
