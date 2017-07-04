package net.daporkchop.pepsimod.module.api.option;

import net.daporkchop.pepsimod.module.api.ModuleOption;

public class OptionTypeShort extends ModuleOption<Short> {
    private static final String[] DEFAULT_COMPLETIONS = new String[]{"0"};

    public OptionTypeShort(String name)   {
        super(name);
    }

    public OptionTypeShort(Object defaultValue, String name)  {
        super((Short) defaultValue, name);
    }

    public Short getDefaultValue() {
        return 0;
    }

    public String[] defaultCompletions() {
        return DEFAULT_COMPLETIONS;
    }
}
