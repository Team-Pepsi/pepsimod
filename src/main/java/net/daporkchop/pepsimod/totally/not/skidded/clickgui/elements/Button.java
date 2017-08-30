package net.daporkchop.pepsimod.totally.not.skidded.clickgui.elements;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.totally.not.skidded.RenderUtilsXdolf;
import net.daporkchop.pepsimod.totally.not.skidded.clickgui.GuiClick;
import net.daporkchop.pepsimod.totally.not.skidded.clickgui.IHoverable;
import net.daporkchop.pepsimod.totally.not.skidded.clickgui.IMenuItem;

import java.util.ArrayList;

public class Button implements IHoverable, IMenuItem {
    public Window window;
    public Module mod;
    public int x, y;
    public boolean overButton;
    public boolean isOpen = false;
    public ArrayList<SubButton> subButtons = new ArrayList<>();

    public Button(Window window, Module mod, int x, int y) {
        this.window = window;
        this.mod = mod;
        this.x = x;
        this.y = y;
    }

    public void draw() {
        RenderUtilsXdolf.drawBorderedRect(x + window.getDragX(), y, x + 96 + window.getDragX(), y + 11, 0.5F, 0xFF000000, mod.isEnabled ? overButton ? 0xFF44AAFF : 0xFFFF0000 : overButton ? 0xFF888888 : 0xFF33363d);
        RenderUtilsXdolf.drawBorderedRect(x + window.getDragX(), y, x + 96 + window.getDragX(), y + 11, 0.5F, 0xFF000000, 0x00000000);

        PepsiMod.INSTANCE.mc.fontRenderer.drawString(mod.nameFull, x + window.getDragX() + 3, y + 1, 0xFFFFFF);
    }

    public void mouseClicked(int x, int y, int button) {
        if(overButton) {
            if (button == 0) {
                GuiClick.sendPanelToFront(window);
                ModuleManager.toggleModule(mod);
            } else if (button == 1) {
                isOpen = !isOpen;
                window.changeallYs();
            }
        }
    }

    public Module getModule() {
        return mod;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isMouseHovered(int x, int y) {
        return x >= getX() + window.getDragX() && y >= this.y && x <= getX() + 96 + window.getDragX() && y <= this.y + 11;
    }

    public void updateY()   {
        int newY = window.getDragY();
        MAINLOOP: for (Button b : window.getButtonList())    {
            if (b == this)  {
                break MAINLOOP;
            }
            newY += 12;
            if (b.isOpen)   {
                for (SubButton subButton : b.subButtons)    {
                    newY += 12;
                }
            }
        }
        y = newY;
    }
}