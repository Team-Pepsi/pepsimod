package net.daporkchop.pepsimod.module.api.option;

import net.daporkchop.pepsimod.module.api.ModuleOption;

public class OptionTypeBoolean extends ModuleOption<Boolean> {
    public OptionTypeBoolean(String name)   {
        super(name);
    }

    public OptionTypeBoolean(Object defaultValue, String name)  {
        super((Boolean) defaultValue, name);
    }

    public Boolean getDefaultValue() {
        return Boolean.FALSE;
    }
}
