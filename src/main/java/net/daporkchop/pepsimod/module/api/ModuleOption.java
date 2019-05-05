/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2019 DaPorkchop_
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from DaPorkchop_.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original author of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
