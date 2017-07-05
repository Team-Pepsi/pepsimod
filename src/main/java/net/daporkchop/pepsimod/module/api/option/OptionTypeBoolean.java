package net.daporkchop.pepsimod.module.api.option;

import net.daporkchop.pepsimod.module.api.ModuleOption;

public class OptionTypeBoolean extends ModuleOption<Boolean> {
    public static final String[] DEFAULT_COMPLETIONS = new String[]{"true", "false"};

    public OptionTypeBoolean(Object defaultValue, String name)  {
        super((Boolean) defaultValue, name);
    }

    public Boolean getDefaultValue() {
        return Boolean.FALSE;
    }

    public String[] defaultCompletions() {
        return DEFAULT_COMPLETIONS;
    }
}
