package net.daporkchop.pepsimod.totally.not.skidded.clickgui;

import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.impl.misc.ClickGuiMod;
import net.daporkchop.pepsimod.totally.not.skidded.clickgui.elements.Window;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;
import java.util.ArrayList;

public class GuiClick extends GuiScreen {
    public static void loadClass()  {}

    public static ArrayList<Window> windowList = new ArrayList<Window>();
    public static ArrayList<Window> unFocusedWindows = new ArrayList<Window>();

    /*public static WindowPlayer player = new WindowPlayer();
    public static WindowRender render = new WindowRender();
    public static WindowValues values = new WindowValues();
    public static WindowInfo info = new WindowInfo();
    public static WindowRadar radar = new WindowRadar();
    public static WindowAura aura = new WindowAura();
    public static WindowWorld world = new WindowWorld();*/

    public static void sendPanelToFront(Window window) {
        if (windowList.contains(window)) {
            int panelIndex = windowList.indexOf(window);
            windowList.remove(panelIndex);
            windowList.add(windowList.size(), window);
        }
    }

    public static Window getFocusedPanel() {
        return windowList.get(windowList.size() - 1);
    }

    public void onGuiClosed() {
    }

    @Override
    protected void keyTyped(char eventChar, int eventKey) {
        if (eventKey == 1 || eventKey == ClickGuiMod.INSTANCE.keybind.getKeyCode()) {
            ModuleManager.disableModule(ClickGuiMod.INSTANCE);

            this.mc.displayGuiScreen((GuiScreen) null);

            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }
    }

    public void mouseClicked(int x, int y, int b) throws IOException {
        try {
            for (Window w : windowList) {
                w.mouseClicked(x, y, b);
            }
            super.mouseClicked(x, y, b);
        } catch (Exception e) {
        }
    }

    public void mouseReleased(int x, int y, int state) {
        try {
            for (Window w : windowList) {
                w.mouseReleased(x, y, state);
            }
            super.mouseReleased(x, y, state);
        } catch (Exception e) {
        }
    }

    public void drawScreen(int x, int y, float ticks) {
        drawRect(0, 0, width, height, 0x8F000000);
        for (Window w : windowList) {
            w.draw(x, y);
        }
        super.drawScreen(x, y, ticks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}