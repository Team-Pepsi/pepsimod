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
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.option.ExtensionSlider;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;
import net.daporkchop.pepsimod.totally.not.skidded.RenderUtilsXdolf;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.colors.ColorUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class SubSlider extends EntryImplBase {
    public final Button parent;
    public Window window;
    public ModuleOption option;
    public ExtensionSlider slider;
    public int intValue;
    public float floatValue;
    public int currentWidth;
    public boolean isFloat;
    public boolean dragging = false;

    public SubSlider(Button parent, ModuleOption option) {
        super(parent.window.getX() + 4, parent.getY() + 4, parent.window.getWidth() - 8, 12);
        this.parent = parent;
        this.window = parent.window;
        this.slider = (ExtensionSlider) option.extended;
        if (isFloat = slider.dataType == ExtensionType.VALUE_FLOAT) {
            floatValue = (float) option.getValue();
        } else {
            intValue = (int) (Object) option.getValue();
        }
        this.option = option;
    }

    public void processMouseClick(int mouseX, int mouseY, int button) {
        updateIsMouseHovered(mouseX, mouseY);
        if (isMouseHovered()) {
            if (button == 0) {
                dragging = true;
            }
        }
    }

    public void processMouseRelease(int mouseX, int mouseY, int button) {
        updateIsMouseHovered(mouseX, mouseY);
        if (dragging && button == 0) {
            dragging = false;
            getWidthFromValue();
        }
    }

    public void draw(int mouseX, int mouseY) {
        if (dragging) {
            currentWidth = mouseX - getX();
            if (currentWidth < 0) {
                currentWidth = 0;
            } else if (currentWidth > 92) {
                currentWidth = 92;
            }
            updateValueFromWidth();
        }
        y = window.getRenderYButton();
        x = window.getX() + 4;
        updateIsMouseHovered(mouseX, mouseY);
        RenderUtilsXdolf.drawRect(getX(), getY(), getX() + getWidth(), getY() + height, ColorUtils.BACKGROUND);
        RenderUtilsXdolf.drawRect(getX(), getY(), getX() + currentWidth, getY() + height, getColor());
        GL11.glColor3f(0f, 0f, 0f);
        mc.fontRenderer.drawString(option.displayName + ": " + (isFloat ? PepsiUtils.roundFloatForSlider(floatValue) : intValue), getX() + 2, getY() + 2, Color.BLACK.getRGB());
    }

    public void updateValueFromWidth() {
        float val = (currentWidth / 92f);
        val *= (getMax() - getMin());
        val += getMin();
        val = PepsiUtils.round(val, getStep());
        val = PepsiUtils.ensureRange(val, getMin(), getMax());
        if (isFloat) {
            floatValue = val;
            option.setValue(val);
        } else {
            intValue = (int) val;
            option.setValue((int) val);
        }
    }

    public float getMax() {
        float val = isFloat ? (float) slider.max : ((int) slider.max) + 0.0f;
        return val;
    }

    public float getMin() {
        float val = isFloat ? (float) slider.min : ((int) slider.min) + 0.0f;
        return val;
    }

    public float getStep() {
        float val = isFloat ? (float) slider.step : ((int) slider.step) + 0.0f;
        return val;
    }

    public int getWidthFromValue() {
        float val = isFloat ? floatValue : intValue + 0.0f;
        val -= getMin();
        val /= (getMax() - getMin());
        val *= 92;
        return currentWidth = PepsiUtils.ensureRange((int) val, 0, 92);
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
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getColor() {
        return ColorUtils.getColorForGuiEntry(ColorUtils.TYPE_SLIDER, isMouseHovered(), false);
    }

    public boolean shouldRender() {
        return parent.isOpen && parent.shouldRender();
    }

    public void openGui() {
        if (isFloat) {
            floatValue = (float) option.getValue();
        } else {
            intValue = (int) (Object) option.getValue();
        }
        getWidthFromValue();
    }

    public String getName() {
        return option.getName();
    }

    public boolean isOpen() {
        return false;
    }

    public void setOpen(boolean val) {
    }
}
