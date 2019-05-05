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

package net.daporkchop.pepsimod.gui.clickgui;

import net.daporkchop.pepsimod.gui.clickgui.api.EntryImplBase;
import net.daporkchop.pepsimod.gui.clickgui.api.IEntry;
import net.daporkchop.pepsimod.gui.clickgui.entry.Button;
import net.daporkchop.pepsimod.gui.clickgui.entry.SubButton;
import net.daporkchop.pepsimod.gui.clickgui.entry.SubSlider;
import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;
import net.daporkchop.pepsimod.util.BetterScaledResolution;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.colors.ColorUtils;
import net.daporkchop.pepsimod.util.config.impl.HUDTranslator;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Window extends EntryImplBase {
    public final String text;
    public List<IEntry> entries = Collections.synchronizedList(new ArrayList<IEntry>());
    public boolean isOpen = false;
    public int modulesCounted = 0;
    public int scroll = 0;
    public ModuleCategory category;
    private int renderYButton = 0;
    private boolean isDragging = false;
    private int dragX = 0, dragY = 0;

    public Window(int x, int y, String name, ModuleCategory category) {
        super(x, y, 100, 12);
        this.text = name;
        this.category = category;
    }

    public void processMouseClick(int mouseX, int mouseY, int button) {
        this.updateIsMouseHovered(mouseX, mouseY);
        if (this.isMouseHovered()) {
            ClickGUI.INSTANCE.sendToFront(this);
            if (button == 0) {
                //drag
                this.isDragging = true;
                this.dragX = mouseX - this.getX();
                this.dragY = mouseY - this.getY();
            } else if (button == 1) {
                this.isOpen = !this.isOpen;
            } else if (button == 2) {
                //anything with a middle mouse button (unimplemented)
            }
        }

        for (IEntry entry : this.entries) {
            if (entry.shouldRender()) {
                entry.processMouseClick(mouseX, mouseY, button);
            }
        }
    }

    public void processMouseRelease(int mouseX, int mouseY, int button) {
        this.updateIsMouseHovered(mouseX, mouseY);
        if (this.isDragging) {
            this.isDragging = false;
        }

        for (IEntry entry : this.entries) {
            if (entry.shouldRender()) {
                entry.processMouseRelease(mouseX, mouseY, button);
            }
        }
    }

    public void draw(int mouseX, int mouseY) {
        if (this.isDragging) {
            this.setX(mouseX - this.dragX);
            this.setY(mouseY - this.dragY);
        }
        GL11.glPushMatrix();
        GL11.glPushAttrib(8256);

        this.scroll = Math.max(0, this.scroll);
        this.scroll = Math.min(this.getDisplayableCount() - this.getModulesToDisplay(), this.scroll);

        this.updateIsMouseHovered(mouseX, mouseY);
        this.renderYButton = this.getY();
        PepsiUtils.drawRect(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getDisplayedHeight(), this.getColor());
        GL11.glColor3f(0f, 0f, 0f);
        drawString(this.getX() + 2, this.getY() + 2, this.text, Color.BLACK.getRGB());
        if (this.isOpen) {
            if (this.shouldScroll()) {
                int barHeight = this.getScrollbarHeight();
                int barY = this.getScrollbarY();
                barY = Math.min(barY, this.getScrollingModuleCount() * 13 - 1 - barHeight);
                PepsiUtils.drawRect(this.getX() + 97, this.getY() + 13 + barY, this.getX() + 99, Math.min(this.getY() + 13 + barY + barHeight, this.getY() + this.getDisplayedHeight() - 1), HUDTranslator.INSTANCE.getColor());
            } else {
                PepsiUtils.drawRect(this.getX() + 97, this.getY() + 13, this.getX() + 99, this.getDisplayedHeight() - 1, HUDTranslator.INSTANCE.getColor());
            }
            this.modulesCounted = 0;
            for (int i = this.getScroll(); i < this.getModulesToDisplay() + this.getScroll(); i++) {
                IEntry entry = this.getNextEntry();
                this.modulesCounted++;
                entry.draw(mouseX, mouseY);
            }
        }

        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    public int getScrollbarHeight() {
        double maxHeight = this.maxDisplayHeight();
        double maxAllowedModules = this.getScrollingModuleCount();
        double displayable = this.getDisplayableCount();
        int result = (int) Math.floor(maxHeight * (maxAllowedModules / displayable));
        return result;
    }

    public int getScrollbarY() {
        int displayable = this.getDisplayableCount();
        int rest = displayable - this.scroll;
        int resultRaw = displayable - rest;
        return resultRaw * 13;
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
        int i = this.height + 1; //+
        for (IEntry entry : this.entries) {
            if (entry.shouldRender()) {
                i += 13;
            }
        }
        return i;
    }

    public int getDisplayableCount() {
        int i = 0;
        for (IEntry entry : this.entries) {
            if (entry.shouldRender()) {
                i++;
            }
        }
        return i;
    }

    public int getWidth() {
        return this.width;
    }

    public int getColor() {
        return ColorUtils.getColorForGuiEntry(ColorUtils.TYPE_WINDOW, this.isMouseHovered(), false);
    }

    public Button addButton(Button b) {
        this.entries.add(b);
        return b;
    }

    public SubButton addSubButton(SubButton b) {
        this.entries.add(this.entries.indexOf(b.parent) + 1, b);
        b.parent.subEntries.add(b);
        return b;
    }

    public SubSlider addSubSlider(SubSlider slider) {
        this.entries.add(this.entries.indexOf(slider.parent) + 1, slider);
        slider.parent.subEntries.add(slider);
        return slider;
    }

    public int getRenderYButton() {
        return this.renderYButton += 13;
    }

    public boolean shouldRender() {
        return true;
    }

    public void openGui() {
        for (IEntry entry : this.entries) {
            entry.openGui();
        }
    }

    public int getScroll() {
        if (this.shouldScroll()) {
            return this.scroll;
        } else {
            return 0;
        }
    }

    public void init(ModuleCategory category) {
        for (Module module : ModuleManager.AVALIBLE_MODULES) {
            if (module.getCategory() != category) {
                continue;
            }
            Button b = this.addButton(new Button(this, module));
            for (ModuleOption option : module.options) {
                if (option.makeButton) {
                    if (option.extended == null) {
                        this.addSubButton(new SubButton(b, option));
                    } else if (option.extended.getType() == ExtensionType.TYPE_SLIDER) {
                        this.addSubSlider(new SubSlider(b, option));
                    } else {
                        throw new IllegalStateException("Option " + option.getName() + " uses an unsupported extension type!");
                    }
                }
            }
        }
    }

    public String getName() {
        return this.text;
    }

    public boolean isOpen() {
        return this.isOpen;
    }

    public void setOpen(boolean val) {
        this.isOpen = val;
    }

    public int maxDisplayHeight() {
        int height = BetterScaledResolution.INSTANCE.scaledHeight;
        height = Math.floorDiv(height, 13);
        height -= 1;
        height *= 13;
        return height;
    }

    public int getScrollingModuleCount() {
        int height = BetterScaledResolution.INSTANCE.scaledHeight;
        height = Math.floorDiv(height, 13);
        height -= 2;
        return height;
    }

    public int getModulesToDisplay() {
        if (this.shouldScroll()) {
            return this.getScrollingModuleCount();
        } else {
            return this.getDisplayableCount();
        }
    }

    public boolean shouldScroll() {
        boolean val = this.getScrollingModuleCount() < this.getDisplayableCount();
        return val;
    }

    public int getDisplayedHeight() {
        int max = this.maxDisplayHeight();
        int normal = this.getHeight();
        int toReturn = Math.min(max, normal);
        return toReturn;
    }

    public IEntry getNextEntry() {
        int a = 0;
        int i = this.scroll;
        for (; ; i++) {
            IEntry entry = this.entries.get(i);
            if (entry.shouldRender()) {
                if (this.modulesCounted != 0) {
                    if (a < this.modulesCounted) {
                        a++;
                        continue;
                    }
                }
                return entry;
            }
        }
    }

    public void handleScroll(int dWheel, int x, int y) {
        this.updateIsMouseHoveredFull(x, y);
        if (this.isMouseHovered() && this.shouldScroll()) {
            this.scroll += dWheel;
        }
    }

    protected void updateIsMouseHoveredFull(int mouseX, int mouseY) {
        int x = this.getX(), y = this.getY();
        int maxX = x + this.width, maxY = y + this.getDisplayedHeight();
        this.isHoveredCached = (x <= mouseX && mouseX <= maxX && y <= mouseY && mouseY <= maxY);
    }
}
