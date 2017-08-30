package net.daporkchop.pepsimod.totally.not.skidded.clickgui.elements;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.totally.not.skidded.RenderUtilsXdolf;
import net.daporkchop.pepsimod.totally.not.skidded.clickgui.GuiClick;
import net.daporkchop.pepsimod.totally.not.skidded.clickgui.IHoverable;
import net.daporkchop.pepsimod.totally.not.skidded.clickgui.IMenuItem;

import java.util.function.Function;
import java.util.function.Supplier;

public class SubButton implements IHoverable, IMenuItem {
    public Button parentButton;
    public String text;
    public int x, y;
    public Function<SubButton, Boolean> isOn = (subButton) -> {return subButton.overButton;};
    public boolean overButton;

    public SubButton(Button parent, String text, Function<SubButton, Boolean> isOn, int x) {
        parentButton = parent;
        this.text = text;
        if (isOn != null)   {
            this.isOn = isOn;
        }
        this.x = x;
    }

    public void draw() {
        if (parentButton.isOpen) {
            RenderUtilsXdolf.drawBorderedRect(x + parentButton.window.getDragX(), y, x + 94 + parentButton.window.getDragX(), y + 11, 0.5F, 0xFF000000, isOn.apply(this) ? overButton ? 0xFF44AAFF : 0xFFFF0000 : overButton ? 0xFF888888 : 0xFF33363d);
            RenderUtilsXdolf.drawBorderedRect(x + parentButton.window.getDragX(), y, x + 94 + parentButton.window.getDragX(), y + 11, 0.5F, 0xFF000000, 0x00000000);

            PepsiMod.INSTANCE.mc.fontRenderer.drawString(text, x + 6 + parentButton.window.getDragX(), y + 1, 0xFFFFFF);
        }
    }

    public void mouseClicked(int x, int y, int button) {
        if (button == 0 && parentButton.isOpen) {
            if (overButton) {
                GuiClick.sendPanelToFront(parentButton.window);
                //TODO: logic here
            }
        }
    }

    public Module getModule() {
        return parentButton.mod;
    }

    public float getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public boolean isMouseHovered(int x, int y) {
        return parentButton.window.isOpen() && x >= this.x + parentButton.window.getDragX() && y >= this.y && x <= this.x + 94 + parentButton.window.getDragX() && y <= this.y + 11;
    }

    public void updateY()   {
        int newY = parentButton.window.getDragY();
        MAINLOOP: for (Button b : parentButton.window.getButtonList())    {
            newY += 12;
            if (b.isOpen)   {
                for (SubButton subButton : b.subButtons)    {
                    if (subButton == this)  {
                        break MAINLOOP;
                    }
                    newY += 12;
                }
            }
        }
        y = newY;
    }
}