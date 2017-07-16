package net.daporkchop.pepsimod.module.api;

import java.io.Serializable;

public abstract class ModuleOption<T> implements Serializable {
    private T value;
    private String name;

    public ModuleOption(String name)   {
        this(null, name);
    }

    public ModuleOption(T defaultValue, String name) {
        this.value = defaultValue;
        this.name = name;
    }

    /**
     * @return true if value could be set, false otherwise
     */
    public boolean setValue(T newValue)   {
        this.value = newValue;
        return true;
    }

    public T getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    public abstract T getDefaultValue();

    public abstract String[] defaultCompletions();
}
