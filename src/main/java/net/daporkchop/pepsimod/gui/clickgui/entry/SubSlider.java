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

package net.daporkchop.pepsimod.gui.clickgui.entry;

import net.daporkchop.pepsimod.gui.clickgui.Window;
import net.daporkchop.pepsimod.gui.clickgui.api.EntryImplBase;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.option.ExtensionSlider;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.colors.ColorUtils;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

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
        if (this.isFloat = this.slider.dataType == ExtensionType.VALUE_FLOAT) {
            this.floatValue = (float) option.getValue();
        } else {
            this.intValue = (int) (Object) option.getValue();
        }
        this.option = option;
    }

    public void processMouseClick(int mouseX, int mouseY, int button) {
        this.updateIsMouseHovered(mouseX, mouseY);
        if (this.isMouseHovered()) {
            if (button == 0) {
                this.dragging = true;
            }
        }
    }

    public void processMouseRelease(int mouseX, int mouseY, int button) {
        this.updateIsMouseHovered(mouseX, mouseY);
        if (this.dragging && button == 0) {
            this.dragging = false;
            this.getWidthFromValue();
        }
    }

    public void draw(int mouseX, int mouseY) {
        if (this.dragging) {
            this.currentWidth = mouseX - this.getX();
            if (this.currentWidth < 0) {
                this.currentWidth = 0;
            } else if (this.currentWidth > 92) {
                this.currentWidth = 92;
            }
            this.updateValueFromWidth();
        }
        this.y = this.window.getRenderYButton();
        this.x = this.window.getX() + 4;
        this.updateIsMouseHovered(mouseX, mouseY);
        PepsiUtils.drawRect(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.height, ColorUtils.BACKGROUND);
        PepsiUtils.drawRect(this.getX(), this.getY(), this.getX() + this.currentWidth, this.getY() + this.height, this.getColor());
        GL11.glColor3f(0f, 0f, 0f);
        mc.fontRenderer.drawString(this.option.displayName + ": " + (this.isFloat ? PepsiUtils.roundFloatForSlider(this.floatValue) : this.intValue), this.getX() + 2, this.getY() + 2, Color.BLACK.getRGB());
    }

    public void updateValueFromWidth() {
        float val = (this.currentWidth / 92f);
        val *= (this.getMax() - this.getMin());
        val += this.getMin();
        val = PepsiUtils.round(val, this.getStep());
        val = PepsiUtils.ensureRange(val, this.getMin(), this.getMax());
        if (this.isFloat) {
            this.floatValue = val;
            this.option.setValue(val);
        } else {
            this.intValue = (int) val;
            this.option.setValue((int) val);
        }
    }

    public float getMax() {
        float val = this.isFloat ? (float) this.slider.max : ((int) this.slider.max) + 0.0f;
        return val;
    }

    public float getMin() {
        float val = this.isFloat ? (float) this.slider.min : ((int) this.slider.min) + 0.0f;
        return val;
    }

    public float getStep() {
        float val = this.isFloat ? (float) this.slider.step : ((int) this.slider.step) + 0.0f;
        return val;
    }

    public int getWidthFromValue() {
        float val = this.isFloat ? this.floatValue : this.intValue + 0.0f;
        val -= this.getMin();
        val /= (this.getMax() - this.getMin());
        val *= 92;
        return this.currentWidth = PepsiUtils.ensureRange((int) val, 0, 92);
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
        return ColorUtils.getColorForGuiEntry(ColorUtils.TYPE_SLIDER, this.isMouseHovered(), false);
    }

    public boolean shouldRender() {
        return this.parent.isOpen && this.parent.shouldRender();
    }

    public void openGui() {
        if (this.isFloat) {
            this.floatValue = (float) this.option.getValue();
        } else {
            this.intValue = (int) (Object) this.option.getValue();
        }
        this.getWidthFromValue();
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
