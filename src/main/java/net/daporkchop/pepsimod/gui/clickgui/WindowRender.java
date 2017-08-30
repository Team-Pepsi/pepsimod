package net.daporkchop.pepsimod.gui.clickgui;

import net.daporkchop.pepsimod.module.impl.render.FullbrightMod;
import net.daporkchop.pepsimod.module.impl.render.NoOverlayMod;
import net.daporkchop.pepsimod.totally.not.skidded.clickgui.GuiClick;
import net.daporkchop.pepsimod.totally.not.skidded.clickgui.elements.Button;
import net.daporkchop.pepsimod.totally.not.skidded.clickgui.elements.Window;

public class WindowRender extends Window {
    /*
    2 17 32 47 62 77
    window x positions
     */

    public WindowRender()   {
        super("Render", 2, 2);
        Button b = this.addButton(NoOverlayMod.INSTANCE);
        this.addSubButton(b, "test");
        this.addSubButton(b, "other");
        System.out.println("Made window, window count:" + GuiClick.windowList.size());
    }
}