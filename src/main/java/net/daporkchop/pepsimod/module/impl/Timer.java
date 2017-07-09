package net.daporkchop.pepsimod.module.impl;

import net.daporkchop.pepsimod.module.api.CustomOption;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;

public class Timer extends Module {
    public static float PROCENT = 1.0f;

    public static Timer INSTANCE;

    {
        INSTANCE = this;
    }

    public Timer(boolean isEnabled, int key, boolean hide) {
        super(isEnabled, "Timer", key, hide);
    }

    @Override
    public void onEnable() {
        INSTANCE = this;//adding this a bunch because it always seems to be null idk y
    }

    @Override
    public void onDisable() {
        INSTANCE = this;//adding this a bunch because it always seems to be null idk y
    }

    @Override
    public void tick() {

    }

    @Override
    public void init() {
        PROCENT = (float) getOptionByName("multiplier").getValue();
        INSTANCE = this; //adding this a bunch because it always seems to be null idk y
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new CustomOption<>(1.0f, "multiplier", new String[]{"1.0", "0.0"},
                        (value) -> {
                            if (value <= 0.0f) {
                                clientMessage("Multiplier cannot be negative or 0!");
                                return;
                            }
                            Timer.PROCENT = value;
                            updateName();
                        },
                        () -> {
                            return Timer.PROCENT;
                        })
        };
    }

    @Override
    public boolean hasModeInName() {
        return true;
    }

    @Override
    public String getModeForName() {
        return String.valueOf((float) getOptionByName("multiplier").getValue());
    }

    public float getMultiplier() {
        if (this.isEnabled) {
            return PROCENT;
        } else {
            return 1.0f;
        }
    }
}
