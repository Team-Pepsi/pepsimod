package net.daporkchop.pepsimod.module.api.option;

import net.daporkchop.pepsimod.module.api.ModuleOption;

public class OptionTypeByte extends ModuleOption<Byte> {
    public OptionTypeByte(String name)   {
        super(name);
    }

    public OptionTypeByte(Object defaultValue, String name)  {
        super((Byte) defaultValue, name);
    }

    public Byte getDefaultValue() {
        return 0;
    }
}
