/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2019 DaPorkchop_
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from DaPorkchop_.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original author of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.daporkchop.pepsimod.gui.clickgui.entry;

import net.daporkchop.pepsimod.gui.clickgui.Window;
import net.daporkchop.pepsimod.gui.clickgui.api.EntryImplBase;
import net.daporkchop.pepsimod.gui.clickgui.api.IEntry;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.colors.ColorUtils;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Button extends EntryImplBase {
    public List<IEntry> subEntries = Collections.synchronizedList(new ArrayList<IEntry>());
    public boolean isOpen = false;
    public Window window;
    public Module module;

    public Button(Window window, Module module) {
        super(window.getX() + 2, window.getY() + 2, window.getWidth() - 6, 12);
        this.window = window;
        this.module = module;
    }

    public void processMouseClick(int mouseX, int mouseY, int button) {
        this.updateIsMouseHovered(mouseX, mouseY);
        if (this.isMouseHovered()) {
            if (button == 0) {
                ModuleManager.toggleModule(this.module);
            } else if (button == 1) {
                this.isOpen = !this.isOpen;
            }
        }
    }

    public void processMouseRelease(int mouseX, int mouseY, int button) {
        this.updateIsMouseHovered(mouseX, mouseY);
    }

    public void draw(int mouseX, int mouseY) {
        this.y = this.window.getRenderYButton();
        this.x = this.window.getX() + 2;
        this.updateIsMouseHovered(mouseX, mouseY);
        PepsiUtils.drawRect(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.height, this.getColor());
        GL11.glColor3f(0f, 0f, 0f);
        drawString(this.getX() + 2, this.getY() + 2, this.module.nameFull, Color.BLACK.getRGB());
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        int i = this.height;
        if (this.isOpen) {
            i += 13 * this.subEntries.size(); //13 px for padding
        }
        return i;
    }

    public int getWidth() {
        return this.width;
    }

    public int getColor() {
        return ColorUtils.getColorForGuiEntry(ColorUtils.TYPE_BUTTON, this.isMouseHovered(), this.module.state.enabled);
    }

    public boolean shouldRender() {
        return this.window.isOpen;
    }

    public void openGui() {

    }

    public String getName() {
        return this.module.name;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public void setOpen(boolean val) {
        this.isOpen = val;
    }
}
