package net.daporkchop.pepsimod.module.api.option;

import net.daporkchop.pepsimod.module.api.ModuleOption;

public class OptionTypeByte extends ModuleOption<Byte> {
    public static final String[] DEFAULT_COMPLETIONS = new String[]{"0"};

    public OptionTypeByte(Object defaultValue, String name)  {
        super((Byte) defaultValue, name);
    }

    public Byte getDefaultValue() {
        return 0;
    }


    public String[] defaultCompletions() {
        return DEFAULT_COMPLETIONS;
    }
}
