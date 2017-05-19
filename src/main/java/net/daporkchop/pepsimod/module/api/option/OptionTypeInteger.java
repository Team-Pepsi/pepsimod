package net.daporkchop.pepsimod.module.api.option;

import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.util.PepsiUtils;

public class OptionTypeInteger extends ModuleOption<Integer> {
    public OptionTypeInteger(String name)   {
        super(name);
    }

    public OptionTypeInteger(Object defaultValue, String name)  {
        super((Integer) defaultValue, name);
    }
}
