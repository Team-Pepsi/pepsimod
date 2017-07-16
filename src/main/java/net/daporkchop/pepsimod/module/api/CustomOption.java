package net.daporkchop.pepsimod.module.api;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * ever wanted to store your options somewhere else
 * now you can
 * :P
 *
 * @param <T> the type of the option
 */
public class CustomOption<T> extends ModuleOption<T> {
    /**
     * this returns a boolean because if there's an invalid value sent you might want to cancel the value set
     * if the return value is true, execution will continue as usual
     * if it's false, then the value will not be sent
     */
    public final Function<T, Boolean> SET;
    public final Supplier<T> GET;
    private final String[] DEFAULT_COMPLETIONS;
    private final T DEFAULT_VALUE;

    public CustomOption(T defaultValue, String name, String[] defaultCompletions, Function<T, Boolean> set, Supplier<T> get) {
        super(defaultValue, name);
        DEFAULT_COMPLETIONS = defaultCompletions;
        DEFAULT_VALUE = defaultValue;
        SET = set;
        GET = get;
    }

    public CustomOption(ModuleOption<T> option, Function<T, Boolean> set, Supplier<T> get) {
        super(option.getDefaultValue(), option.getName());
        DEFAULT_VALUE = option.getDefaultValue();
        DEFAULT_COMPLETIONS = option.defaultCompletions();
        SET = set;
        GET = get;
    }

    @Override
    public boolean setValue(T value) {
        return SET.apply(value);
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
