package net.daporkchop.pepsimod.module.impl.movement;

import net.daporkchop.pepsimod.module.api.CustomOption;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;

public class Velocity extends Module {
    public static float PROCENT = 1.0f;

    public static Velocity INSTANCE;

    {
        INSTANCE = this;
    }

    public Velocity(boolean isEnabled, int key, boolean hide) {
        super(isEnabled, "Velocity", key, hide);
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
        PROCENT = (float) getOptionByName("strength").getValue();
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new CustomOption<>(1.0f, "strength", new String[]{"1.0", "0.0"},
                        (value) -> {
                            Velocity.PROCENT = value;
                            updateName();
                        },
                        () -> {
                            return Velocity.PROCENT;
                        })
        };
    }

    @Override
    public boolean hasModeInName() {
        return true;
    }

    @Override
    public String getModeForName() {
        return String.valueOf((float) getOptionByName("strength").getValue());
    }

    public float getVelocity() {
        if (this.isEnabled) {
            return PROCENT;
        } else {
            return 1.0f;
        }
    }
}
