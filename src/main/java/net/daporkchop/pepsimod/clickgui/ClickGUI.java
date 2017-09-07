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

package net.daporkchop.pepsimod.clickgui;

import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.impl.misc.ClickGuiMod;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;

public class ClickGUI extends GuiScreen {
    public static ClickGUI INSTANCE;
    public ArrayList<Window> windows = new ArrayList<>();

    {
        INSTANCE = this;
    }

    protected void keyTyped(char eventChar, int eventKey) {
        if (eventKey == 1 || eventKey == ClickGuiMod.INSTANCE.keybind.getKeyCode()) {
            ModuleManager.disableModule(ClickGuiMod.INSTANCE);

            this.mc.displayGuiScreen(null);

            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }
    }

    public void sendToFront(Window window) {
        if (windows.contains(window)) {
            int panelIndex = windows.indexOf(window);
            windows.remove(panelIndex);
            windows.add(windows.size(), window);
        }
    }

    public void mouseClicked(int x, int y, int b) throws IOException {
        for (Window w : windows) {
            w.processMouseClick(x, y, b);
        }
        super.mouseClicked(x, y, b);
    }

    public void mouseReleased(int x, int y, int state) {
        for (Window w : windows) {
            w.processMouseRelease(x, y, state);
        }
        super.mouseReleased(x, y, state);
    }

    public void drawScreen(int x, int y, float ticks) {
        for (Window w : windows) {
            w.draw(x, y);
        }

        super.drawScreen(x, y, ticks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
