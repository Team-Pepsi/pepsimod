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

package net.daporkchop.pepsimod.clickgui.entry;

import net.daporkchop.pepsimod.clickgui.Window;
import net.daporkchop.pepsimod.clickgui.api.EntryImplBase;
import net.daporkchop.pepsimod.clickgui.api.IEntry;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.totally.not.skidded.RenderUtilsXdolf;
import net.daporkchop.pepsimod.util.colors.ColorUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Button extends EntryImplBase {
    public List<IEntry> subEntries = Collections.synchronizedList(new ArrayList<IEntry>());
    public boolean isOpen = false;
    public Window window;
    public Module module;

    public Button(Window window, Module module) {
        super(window.getX() + 2, window.getY() + 2, window.getWidth() - 4, 12);
        this.window = window;
        this.module = module;
    }

    public void processMouseClick(int mouseX, int mouseY, int button) {
        updateIsMouseHovered(mouseX, mouseY);
        if (isMouseHovered()) {
            if (button == 0) {
                ModuleManager.toggleModule(module);
            } else if (button == 1) {
                isOpen = !isOpen;
            }
        }
    }

    public void processMouseRelease(int mouseX, int mouseY, int button) {
        updateIsMouseHovered(mouseX, mouseY);
    }

    public void draw(int mouseX, int mouseY) {
        y = window.getRenderYButton();
        x = window.getX() + 2;
        updateIsMouseHovered(mouseX, mouseY);
        RenderUtilsXdolf.drawRect(getX(), getY(), getX() + getWidth(), getY() + height, getColor());
        GL11.glColor3f(0f, 0f, 0f);
        drawString(getX() + 2, getY() + 2, module.nameFull, Color.BLACK.getRGB());
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        int i = height;
        if (isOpen) {
            i += 13 * subEntries.size(); //13 px for padding
        }
        return i;
    }

    public int getWidth() {
        return width;
    }

    public int getColor() {
        return ColorUtils.getColorForGuiEntry(ColorUtils.TYPE_BUTTON, isMouseHovered(), module.isEnabled);
    }

    public boolean shouldRender() {
        return window.isOpen;
    }

    public void openGui() {

    }

    public String getName() {
        return module.name;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean val) {
        isOpen = val;
    }
}
