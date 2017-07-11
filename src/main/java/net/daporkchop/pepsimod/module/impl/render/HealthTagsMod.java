package net.daporkchop.pepsimod.module.impl.render;

import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;

public class HealthTagsMod extends Module {
    public static HealthTagsMod INSTANCE;

    public HealthTagsMod(boolean isEnabled, int key, boolean hide) {
        super(isEnabled, "HealthTags", key, hide);
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
