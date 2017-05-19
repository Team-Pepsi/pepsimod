package net.daporkchop.pepsimod.module.api.option;

import net.daporkchop.pepsimod.module.api.ModuleOption;

public class OptionTypeString extends ModuleOption<String> {
    public OptionTypeString(String name)   {
        super(name);
    }

    public OptionTypeString(Object defaultValue, String name)  {
        super((String) defaultValue, name);
    }
}
