package net.daporkchop.pepsimod.module.api;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * ever wanted to store your options somewhere else
 * now you can
 * :P
 *
 * @param <T> the type of the option
 */
public class CustomOption<T> extends ModuleOption<T> {
    public final Consumer<T> SET;
    public final Supplier<T> GET;
    private final String[] DEFAULT_COMPLETIONS;
    private final T DEFAULT_VALUE;

    public CustomOption(T defaultValue, String name, String[] defaultCompletions, Consumer<T> set, Supplier<T> get) {
        super(defaultValue, name);
        DEFAULT_COMPLETIONS = defaultCompletions;
        DEFAULT_VALUE = defaultValue;
        SET = set;
        GET = get;
    }

    public CustomOption(ModuleOption<T> option, Consumer<T> set, Supplier<T> get) {
        super(option.getDefaultValue(), option.getName());
        DEFAULT_VALUE = option.getDefaultValue();
        DEFAULT_COMPLETIONS = option.defaultCompletions();
        SET = set;
        GET = get;
    }

    @Override
    public T setValue(T value) {
        SET.accept(value);
        return value;
    }

    @Override
    public T getValue() {
        return GET.get();
    }

    public T getDefaultValue() {
        return DEFAULT_VALUE;
    }

    public String[] defaultCompletions() {
        return DEFAULT_COMPLETIONS;
    }
}
