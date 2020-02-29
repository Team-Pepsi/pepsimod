/*
 * Adapted from The MIT License (MIT)
 *
 * Copyright (c) 2016-2020 DaPorkchop_
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * Any persons and/or organizations using this software must include the above copyright notice and this permission notice,
 * provide sufficient credit to the original authors of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.daporkchop.pepsimod.gui.clickgui;

import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.impl.misc.ClickGuiMod;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class ClickGUI extends GuiScreen {
    public static ClickGUI INSTANCE;
    public Window[] windows;

    {
        INSTANCE = this;
    }

    public void setWindows(Window... windows) {
        this.windows = windows;
    }

    public void initWindows() {
        for (Window window : this.windows) {
            window.init(window.category);
        }
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

    public synchronized void sendToFront(Window window) {
        if (this.containsWindow(window)) {
            int panelIndex = 0;
            for (int i = 0; i < this.windows.length; i++) {
                if (this.windows[i] == window) {
                    panelIndex = i;
                    break;
                }
            }
            Window t = this.windows[0];
            this.windows[0] = this.windows[panelIndex];
            this.windows[panelIndex] = t;
        }
    }

    public void mouseClicked(int x, int y, int b) throws IOException {
        for (Window window : this.windows) {
            window.processMouseClick(x, y, b);
        }

        super.mouseClicked(x, y, b);
    }

    public void mouseReleased(int x, int y, int state) {
        for (Window window : this.windows) {
            window.processMouseRelease(x, y, state);
        }

        super.mouseReleased(x, y, state);
    }

    public void drawScreen(int x, int y, float ticks) {
        for (Window window : this.windows) {
            window.draw(x, y);
        }

        super.drawScreen(x, y, ticks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public boolean containsWindow(Window window) {
        for (Window window1 : this.windows) {
            if (window == window1) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int dWheel = MathHelper.clamp(Mouse.getEventDWheel(), -1, 1);
        if (dWheel != 0) {
            dWheel *= -1;
            int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
            int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
            for (Window window : this.windows) {
                window.handleScroll(dWheel, x, y);
            }
        }
    }
}
