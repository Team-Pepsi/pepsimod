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

    public T setValue(T newValue)   {
        return this.value = newValue;
    }

    public T getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }
}
