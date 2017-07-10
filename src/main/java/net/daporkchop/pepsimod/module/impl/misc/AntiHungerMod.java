package net.daporkchop.pepsimod.module.impl.misc;

import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;

public class AntiHungerMod extends Module {
    public static boolean ANTI_HUNGER = false;

    public AntiHungerMod(boolean isEnabled, int key, boolean hide) {
        super(isEnabled, "AntiHunger", key, hide);
    }

    @Override
    public void onEnable() {
        ANTI_HUNGER = true;
    }

    @Override
    public void onDisable() {
        ANTI_HUNGER = false;
    }

    @Override
    public void tick() {

    }

    @Override
    public void init() {

    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[0];
    }
}
