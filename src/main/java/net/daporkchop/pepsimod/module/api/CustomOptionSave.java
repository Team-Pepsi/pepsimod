package net.daporkchop.pepsimod.module.api;

/**
 * used to save CustomOptions
 * because Consumers and Suppliers cannot be serialized
 */
public class CustomOptionSave<T> extends ModuleOption<T> {
    private final String[] DEFAULT_COMPLETIONS;
    private final T DEFAULT_VALUE;

    public CustomOptionSave(CustomOption<T> option) {
        super(option.getDefaultValue(), option.getName());
        DEFAULT_COMPLETIONS = option.defaultCompletions();
        DEFAULT_VALUE = option.getDefaultValue();
    }

    @Override
    public boolean setValue(T value) {
        return true;
    }

    @Override
    public T getValue() {
        return null;
    }

    public T getDefaultValue() {
        return DEFAULT_VALUE;
    }

    public String[] defaultCompletions() {
        return DEFAULT_COMPLETIONS;
    }
}
