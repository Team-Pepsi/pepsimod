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

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import net.daporkchop.pepsimod.clickgui.api.EntryImplBase;
import net.daporkchop.pepsimod.clickgui.api.IEntry;
import net.daporkchop.pepsimod.clickgui.entry.Button;
import net.daporkchop.pepsimod.clickgui.entry.SubButton;
import net.daporkchop.pepsimod.clickgui.entry.SubSlider;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;
import net.daporkchop.pepsimod.totally.not.skidded.RenderUtilsXdolf;
import net.daporkchop.pepsimod.util.colors.ColorUtils;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Window extends EntryImplBase {
    public final String text;
    public List<IEntry> entries = Collections.synchronizedList(new ArrayList<IEntry>());
    public boolean isOpen = false;
    private int renderYButton = 0;
    private boolean isDragging = false;
    private int dragX = 0, dragY = 0;

    public Window(int x, int y, String name) {
        super(x, y, 100, 12);
        text = name;
    }

    public void processMouseClick(int mouseX, int mouseY, int button) {
        updateIsMouseHovered(mouseX, mouseY);
        if (isMouseHovered()) {
            ClickGUI.INSTANCE.sendToFront(this);
            if (button == 0) {
                //drag
                isDragging = true;
                dragX = mouseX - getX();
                dragY = mouseY - getY();
            } else if (button == 1) {
                isOpen = !isOpen;
            } else if (button == 2) {
                //anything with a middle mouse button (unimplemented)
            }
        }

        for (IEntry entry : entries) {
            if (entry.shouldRender()) {
                entry.processMouseClick(mouseX, mouseY, button);
            }
        }
    }

    public void processMouseRelease(int mouseX, int mouseY, int button) {
        updateIsMouseHovered(mouseX, mouseY);
        if (isDragging) {
            isDragging = false;
        }

        for (IEntry entry : entries) {
            if (entry.shouldRender()) {
                entry.processMouseRelease(mouseX, mouseY, button);
            }
        }
    }

    public void draw(int mouseX, int mouseY) {
        if (isDragging) {
            setX(mouseX - dragX);
            setY(mouseY - dragY);
        }
        GL11.glPushMatrix();
        GL11.glPushAttrib(8256);

        updateIsMouseHovered(mouseX, mouseY);
        renderYButton = getY();
        RenderUtilsXdolf.drawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), getColor());
        GL11.glColor3f(0f, 0f, 0f);
        drawString(getX() + 2, getY() + 2, text, Color.BLACK.getRGB());
        for (IEntry entry : entries) {
            if (entry.shouldRender()) {
                entry.draw(mouseX, mouseY);
            }
        }

        GL11.glPopMatrix();
        GL11.glPopAttrib();
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
        int i = height + 1; //+
        for (IEntry entry : entries) {
            if (entry.shouldRender()) {
                i += 13;
            }
        }
        return i;
    }

    public int getWidth() {
        return width;
    }

    public int getColor() {
        return ColorUtils.getColorForGuiEntry(ColorUtils.TYPE_WINDOW, isMouseHovered(), false);
    }

    public Button addButton(Button b) {
        entries.add(b);
        return b;
    }

    public SubButton addSubButton(SubButton b) {
        entries.add(entries.indexOf(b.parent) + 1, b);
        b.parent.subEntries.add(b);
        return b;
    }

    public SubSlider addSubSlider(SubSlider slider) {
        entries.add(entries.indexOf(slider.parent) + 1, slider);
        slider.parent.subEntries.add(slider);
        return slider;
    }

    public int getRenderYButton() {
        return renderYButton += 13;
    }

    public boolean shouldRender() {
        return true;
    }

    public void openGui() {
        for (IEntry entry : entries) {
            entry.openGui();
        }
    }

    protected void init(String pkg) {
        try {
            ImmutableSet<ClassPath.ClassInfo> classes = ClassPath.from(getClass().getClassLoader()).getTopLevelClasses("net.daporkchop.pepsimod.module.impl." + pkg);
            for (ClassPath.ClassInfo info : classes) {
                Module module = ModuleManager.CLASS_NAME_TO_MODULE.get(info.getName());
                Button b = this.addButton(new Button(this, module));
                for (ModuleOption option : module.options) {
                    if (option.makeButton && !(option.getName().equals("enabled") || option.getName().equals("hidden"))) {
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
        } catch (IOException e) {
            e.printStackTrace();
            FMLCommonHandler.instance().exitJava(59743, true);
        }
    }

    public String getName() {
        return text;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean val) {
        isOpen = val;
    }
}
