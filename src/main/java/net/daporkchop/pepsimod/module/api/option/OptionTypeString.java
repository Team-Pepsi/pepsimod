package net.daporkchop.pepsimod.module.api.option;

import net.daporkchop.pepsimod.module.api.ModuleOption;

public class OptionTypeString extends ModuleOption<String> {
    private static final String[] DEFAULT_COMPLETIONS = new String[]{"0"};

    public OptionTypeString(String name)   {
        super(name);
    }

    public OptionTypeString(Object defaultValue, String name)  {
        super((String) defaultValue, name);
    }

    public String getDefaultValue() {
        return "";
    }

    public String[] defaultCompletions() {
        return DEFAULT_COMPLETIONS;
    }
}
