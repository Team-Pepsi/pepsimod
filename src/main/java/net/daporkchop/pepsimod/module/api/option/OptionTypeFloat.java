package net.daporkchop.pepsimod.module.api.option;

import net.daporkchop.pepsimod.module.api.ModuleOption;

public class OptionTypeFloat extends ModuleOption<Float> {
    private static final String[] DEFAULT_COMPLETIONS = new String[]{"0.0"};

    public OptionTypeFloat(String name)   {
        super(name);
    }

    public OptionTypeFloat(Object defaultValue, String name)  {
        super((Float) defaultValue, name);
    }

    public Float getDefaultValue() {
        return 0.0f;
    }

    public String[] defaultCompletions() {
        return DEFAULT_COMPLETIONS;
    }
}
