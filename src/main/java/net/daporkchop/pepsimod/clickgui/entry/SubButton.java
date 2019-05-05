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

package net.daporkchop.pepsimod.clickgui.entry;

import net.daporkchop.pepsimod.clickgui.Window;
import net.daporkchop.pepsimod.clickgui.api.EntryImplBase;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.colors.ColorUtils;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

public class SubButton extends EntryImplBase {
    public final Button parent;
    public Window window;
    public ModuleOption option;

    public SubButton(Button parent, ModuleOption option) {
        super(parent.window.getX() + 4, parent.getY() + 4, parent.window.getWidth() - 8, 12);
        this.parent = parent;
        this.window = parent.window;
        this.option = option;
    }

    public void processMouseClick(int mouseX, int mouseY, int button) {
        this.updateIsMouseHovered(mouseX, mouseY);
        if (this.isMouseHovered()) {
            if (button == 0) {
                this.option.setValue(!((boolean) this.option.getValue()));
            }
        }
    }

    public void processMouseRelease(int mouseX, int mouseY, int button) {
        this.updateIsMouseHovered(mouseX, mouseY);
    }

    public void draw(int mouseX, int mouseY) {
        this.y = this.window.getRenderYButton();
        this.x = this.window.getX() + 4;
        this.updateIsMouseHovered(mouseX, mouseY);
        PepsiUtils.drawRect(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.height, this.getColor());
        GL11.glColor3f(0f, 0f, 0f);
        mc.fontRenderer.drawString(this.option.displayName, this.getX() + 2, this.getY() + 2, Color.BLACK.getRGB());
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
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getColor() {
        return ColorUtils.getColorForGuiEntry(ColorUtils.TYPE_BUTTON, this.isMouseHovered(), (boolean) this.option.getValue());
    }

    public boolean shouldRender() {
        return this.parent.isOpen && this.parent.shouldRender();
    }

    public void openGui() {

    }

    public String getName() {
        return this.option.getName();
    }

    public boolean isOpen() {
        return false;
    }

    public void setOpen(boolean val) {
    }
}
