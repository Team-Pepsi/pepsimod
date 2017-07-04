package net.daporkchop.pepsimod.module.api.option;

import net.daporkchop.pepsimod.module.api.ModuleOption;

public class OptionTypeDouble extends ModuleOption<Double> {
    private static final String[] DEFAULT_COMPLETIONS = new String[]{"0.0"};

    public OptionTypeDouble(String name)   {
        super(name);
    }

    public OptionTypeDouble(Object defaultValue, String name)  {
        super((Double) defaultValue, name);
    }

    public Double getDefaultValue() {
        return 0.0d;
    }

    public String[] defaultCompletions() {
        return DEFAULT_COMPLETIONS;
    }
}
