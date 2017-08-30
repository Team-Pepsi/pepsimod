package net.daporkchop.pepsimod.module.impl.render;

import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.totally.not.skidded.clickgui.GuiClick;
import net.daporkchop.pepsimod.totally.not.skidded.clickgui.elements.Window;

public class NoOverlayMod extends Module {
    public static NoOverlayMod INSTANCE;

    public NoOverlayMod(boolean isEnabled, int key, boolean hide) {
        super(isEnabled, "NoOverlay", key, hide);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void tick() {

    }

    @Override
    public void init() {
        INSTANCE = this;
        Window.loadClass();
        GuiClick.loadClass();
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[0];
    }
}
