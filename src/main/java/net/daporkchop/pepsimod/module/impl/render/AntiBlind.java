package net.daporkchop.pepsimod.module.impl.render;

import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;

public class AntiBlind extends Module {
    public static AntiBlind INSTANCE;

    public AntiBlind(boolean isEnabled, int key, boolean hide) {
        super(isEnabled, "AntiBlind", key, hide);
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
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[0];
    }
}
