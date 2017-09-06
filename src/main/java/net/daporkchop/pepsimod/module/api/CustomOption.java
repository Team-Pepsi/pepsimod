/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017 Team Pepsi
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from Team Pepsi.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: Team Pepsi), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
