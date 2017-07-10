package net.daporkchop.pepsimod.module.impl.misc;

import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;

public class NoFallMod extends Module {
    public static boolean NO_FALL = false;

    public NoFallMod(boolean isEnabled, int key, boolean hide) {
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
