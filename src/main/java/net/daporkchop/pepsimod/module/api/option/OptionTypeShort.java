package net.daporkchop.pepsimod.module.api.option;

import net.daporkchop.pepsimod.module.api.ModuleOption;

public class OptionTypeShort extends ModuleOption<Short> {
    public OptionTypeShort(String name)   {
        super(name);
    }

    public OptionTypeShort(Object defaultValue, String name)  {
        super((Short) defaultValue, name);
    }
}
