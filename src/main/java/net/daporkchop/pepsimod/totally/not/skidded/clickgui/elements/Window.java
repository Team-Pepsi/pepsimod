package net.daporkchop.pepsimod.totally.not.skidded.clickgui.elements;

import java.io.IOException;
import java.util.ArrayList;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.gui.clickgui.WindowRender;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.totally.not.skidded.RenderUtilsXdolf;
import net.daporkchop.pepsimod.totally.not.skidded.clickgui.GuiClick;
import net.daporkchop.pepsimod.totally.not.skidded.clickgui.IHoverable;
import org.lwjgl.opengl.GL11;

public class Window implements IHoverable {
    public static void loadClass()  {}

    private String title;
    private int x, y;
    public int dragX;
    public int dragY;
    private int lastDragX;
    private int lastDragY;
    private boolean isOpen, isPinned;
    protected boolean dragging;
    public int nextRenderY = 0;

    private ArrayList<Button> buttonList = new ArrayList<Button>();
    private ArrayList<Slider> sliderList = new ArrayList<Slider>();

    public static final WindowRender renderWindow = new WindowRender();

    public void drag(int x, int y) {
        dragX = x - lastDragX;
        dragY = y - lastDragY;
        changeallYs();
    }

    public Window(String title, int x, int y) {
        this.title = title;
        this.x = x;
        this.y = y;
        GuiClick.windowList.add(this);
        GuiClick.unFocusedWindows.add(this);
    }

    public void draw(int x, int y) {
        GL11.glPushMatrix();
        GL11.glPushAttrib(8256);
        int toAdd = 0;
        if(dragging) {
            drag(x, y);
        }

        RenderUtilsXdolf.drawBorderedRect(getXAndDrag(), getYAndDrag(), getXAndDrag() + 100, getYAndDrag() + 13 + (isOpen ? (12 * buttonList.size() + 0.5F) + (19 * sliderList.size() + (sliderList.size() != 0 ? 2.5F : 0)) : 0) + toAdd, 0.5F, 0xFF000000, 0x80000000);
        PepsiMod.INSTANCE.mc.fontRenderer.drawStringWithShadow(title, getXAndDrag() + 3, getYAndDrag()  + 1, 0xFFFFFF);
        //TODO: clickgui rainbow text
        if(PepsiMod.INSTANCE.mc.currentScreen instanceof GuiClick) {
            //RenderUtilsXdolf.drawBorderedRect(getXAndDrag() + 79, getYAndDrag() + 2, getXAndDrag() + 88, getYAndDrag() + 11, 0.5F, 0xFF000000, isPinned ? 0xFFFF0000 : 0xFF383b42);
            //RenderUtilsXdolf.drawBorderedRect(getXAndDrag() + 89, getYAndDrag() + 2, getXAndDrag() + 98, getYAndDrag() + 11, 0.5F, 0xFF000000, isOpen ? 0xFFFF0000 : 0xFF383b42);
        }

        if(isOpen) {
            nextRenderY = 0;
            for(Button b : buttonList) {
                if(b.isMouseHovered(x, y)) {
                    b.overButton = true;
                } else {
                    b.overButton = false;
                }
                b.draw();

                for (SubButton subButton : b.subButtons)    {
                    if (subButton.isMouseHovered(x, y)) {
                        subButton.overButton = true;
                    } else {
                        subButton.overButton = false;
                    }
                    subButton.draw();
                }
            }
            for(Slider s : sliderList) {
                s.draw(x);
            }
        }
        //GL11.glEnable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    public void mouseClicked(int x, int y, int button) throws IOException {
        //button 0: left
        // 1: right
        // 2: middle

        for(Button b : buttonList) {
            b.mouseClicked(x, y, button);
        }
        for(Slider s : sliderList) {
            s.mouseClicked(x, y, button);
        }

        if (isMouseHovered(x, y)) {
            if (button == 0) {
                GuiClick.sendPanelToFront(this);
                if (isPinned) {
                    dragging = true;
                    lastDragX = x - dragX;
                    lastDragY = y - dragY;
                }
            } else if (button == 1) {
                isOpen = !isOpen;
                changeallYs();
            } else if (button == 2) {
                isPinned = !isPinned;
            }
        }
    }

    public void mouseReleased(int x, int y, int state) {
        for(Slider s : sliderList) {
            s.mouseReleased(x, y, state);
        }
        if(state == 0) {
            dragging = false;
        }
    }

    public Button addButton(Module module) {
        Button button = new Button(this, module, x + 2, y + 12 + (12 * buttonList.size()));
        buttonList.add(button);

        return button;
    }

    public Slider addSlider(ModuleOption value, float minValue, float maxValue, boolean shouldRound) {
        Slider slider = new Slider(this, value, getX() + 2, getY() + (19 * sliderList.size()) + 16, minValue, maxValue, shouldRound);
        sliderList.add(slider);

        return slider;
    }

    public SubButton addSubButton(Button parent, String text)    {
        SubButton subButton = new SubButton(parent, text, null, parent.x + 2);
        parent.subButtons.add(subButton);

        return subButton;
    }

    public String getTitle() {
        return title;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDragX() {
        return dragX;
    }

    public int getDragY() {
        return dragY + 12;
    }

    public int getXAndDrag() {
        return x + dragX;
    }

    public int getYAndDrag() {
        return y + dragY;
    }

    public void setTitle(String s) {
        this.title = s;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public boolean isPinned() {
        return isPinned;
    }

    public void setOpen(boolean b) {
        this.isOpen = b;
    }

    public void setPinned(boolean b) {
        this.isPinned = b;
    }

    public void setDragX(int x) {
        this.dragX = x;
    }

    public void setDragY(int y) {
        this.dragY = y;
    }

    public ArrayList<Button> getButtonList() {
        return buttonList;
    }

    public int getNextRenderY() {
        int i = nextRenderY;
        nextRenderY += 12;
        return i;
    }

    public boolean isMouseHovered(int x, int y) {
        return x >= getXAndDrag() && y >= getYAndDrag() && x <= getXAndDrag() + 100 && y <= getYAndDrag() + 13;
    }

    public void changeallYs()   {
        for (Button button : buttonList)    {
            button.updateY();
            for (SubButton subButton : button.subButtons)   {
                subButton.updateY();
            }
        }
    }
}
