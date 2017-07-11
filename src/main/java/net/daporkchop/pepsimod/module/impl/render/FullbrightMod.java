package net.daporkchop.pepsimod.module.impl.render;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;

public class FullbrightMod extends Module {

    public FullbrightMod(boolean isEnabled, int key, boolean hide) {
        super(isEnabled, "Fullbright", key, hide);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void tick() {
        if (this.isEnabled || XrayMod.INSTANCE.isEnabled) {
            if (PepsiMod.INSTANCE.mc.gameSettings.gammaSetting < 16f) {
                PepsiMod.INSTANCE.mc.gameSettings.gammaSetting += 0.5f;
            }
        } else if (PepsiMod.INSTANCE.mc.gameSettings.gammaSetting > 0.5f) {
            if (PepsiMod.INSTANCE.mc.gameSettings.gammaSetting < 1F) {
                PepsiMod.INSTANCE.mc.gameSettings.gammaSetting = 0.5F;
            } else {
                PepsiMod.INSTANCE.mc.gameSettings.gammaSetting -= 0.5F;
            }
        }
    }

    @Override
    public void init() {

    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[0];
    }

    @Override
    public boolean shouldTick() {
        return true;
    }
}
