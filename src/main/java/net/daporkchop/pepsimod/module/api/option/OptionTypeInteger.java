package net.daporkchop.pepsimod.module.api.option;

import net.daporkchop.pepsimod.module.api.ModuleOption;

public class OptionTypeInteger extends ModuleOption<Integer> {
    public static final String[] DEFAULT_COMPLETIONS = new String[]{"0"};

    public OptionTypeInteger(Object defaultValue, String name)  {
        super((Integer) defaultValue, name);
    }

    public Integer getDefaultValue() {
        return 0;
    }

    public String[] defaultCompletions() {
        return DEFAULT_COMPLETIONS;
    }
}
