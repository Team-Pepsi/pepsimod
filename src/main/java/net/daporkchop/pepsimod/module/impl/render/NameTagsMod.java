package net.daporkchop.pepsimod.module.impl.render;

import net.daporkchop.pepsimod.module.api.CustomOption;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.option.OptionTypeFloat;

public class NameTagsMod extends Module {
    public static NameTagsMod INSTANCE;

    public static float scale;

    public NameTagsMod(boolean isEnabled, int key, boolean hide) {
        super(isEnabled, "NameTags", key, hide);
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
        return new ModuleOption[]{
                new CustomOption<>(1.0f, "scale", OptionTypeFloat.DEFAULT_COMPLETIONS,
                        (value) -> {
                            scale = value;
                            return true;
                        },
                        () -> {
                            return scale;
                        })
        };
    }

    public float getScale() {
        if (isEnabled) {
            return scale;
        } else {
            return 1.0f;
        }
    }
}
