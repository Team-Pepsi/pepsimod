package net.daporkchop.pepsimod.module.impl;

import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;

public class NoFall extends Module {
    public static boolean NO_FALL = false;

    public NoFall(boolean isEnabled, int key, boolean hide) {
        super(isEnabled, "NoFall", key, hide);
    }

    @Override
    public void onEnable() {
        NO_FALL = true;
    }

    @Override
    public void onDisable() {
        NO_FALL = false;
    }

    @Override
    public void tick() {

    }

    @Override
    public void init() {

    }

    @Override
    public ModuleOption[] getDefaultOptions()   {
        return new ModuleOption[0];
    }
}
