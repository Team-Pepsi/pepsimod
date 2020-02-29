/*
 * Adapted from The MIT License (MIT)
 *
 * Copyright (c) 2016-2020 DaPorkchop_
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * Any persons and/or organizations using this software must include the above copyright notice and this permission notice,
 * provide sufficient credit to the original authors of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.daporkchop.pepsimod.module.api;

import net.daporkchop.pepsimod.module.api.option.OptionExtended;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModuleOption<T> {
    public final Function<T, Boolean> SET;
    public final Supplier<T> GET;
    private final String[] DEFAULT_COMPLETIONS;
    private final T DEFAULT_VALUE;
    public String displayName;
    public boolean makeButton = true;
    public OptionExtended extended = null;
    private T value;
    private String name;

    public ModuleOption(T defaultValue, String name, String[] defaultCompletions, Function<T, Boolean> set, Supplier<T> get, String displayName, OptionExtended extended, boolean makeWindow) {
        this(defaultValue, name, defaultCompletions, set, get, displayName, makeWindow);
        this.extended = extended;
    }

    public ModuleOption(T defaultValue, String name, String[] defaultCompletions, Function<T, Boolean> set, Supplier<T> get, String displayName, OptionExtended extended) {
        this(defaultValue, name, defaultCompletions, set, get, displayName);
        this.extended = extended;
    }

    public ModuleOption(T defaultValue, String name, String[] defaultCompletions, Function<T, Boolean> set, Supplier<T> get, String displayName, boolean makeButton) {
        this(defaultValue, name, defaultCompletions, set, get, displayName);
        this.makeButton = makeButton;
    }

    public ModuleOption(T defaultValue, String name, String[] defaultCompletions, Function<T, Boolean> set, Supplier<T> get, String displayName) {
        this.DEFAULT_COMPLETIONS = defaultCompletions;
        this.DEFAULT_VALUE = defaultValue;
        this.SET = set;
        this.GET = get;
        this.value = defaultValue;
        this.name = name;
        this.displayName = displayName;
    }

    public String getName() {
        return this.name == null ? this.displayName.toLowerCase() : this.name;
    }

    public boolean setValue(T value) {
        return this.SET.apply(value);
    }

    public T getValue() {
        T toReturn = this.GET.get();
        return toReturn == null ? this.getDefaultValue() : toReturn;
    }

    public T getDefaultValue() {
        return this.DEFAULT_VALUE;
    }

    public String[] defaultCompletions() {
        return this.DEFAULT_COMPLETIONS;
    }
}
