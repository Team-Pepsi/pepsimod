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

package net.daporkchop.pepsimod.module.util;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import net.daporkchop.pepsimod.module.Module;

import java.util.function.Supplier;

/**
 * A container for a module.
 * <p>
 * Note that the actual module instance referenced by this instance can change between module manager init cycles, or even be {@code null} if the module
 * manager is not currently active.
 *
 * @author DaPorkchop_
 */
@Getter
public class Mod<M extends Module> {
    protected       M           instance; //global module instance reference
    protected final Class<M>    clazz; //the module's class
    protected final Supplier<M> factory; //supplies new instances for use after module manager reloads

    protected final String id;

    protected boolean enabled;
    protected boolean visible;

    public Mod(@NonNull Class<M> clazz, @NonNull Supplier<M> factory) {
        this.clazz = clazz;
        this.factory = factory;

        Module.Info info = clazz.getAnnotation(Module.Info.class);
        if (info != null) {
            this.id = info.id();
        } else {
            throw new IllegalArgumentException(String.format("Class %s is missing @Module.Info annotation!", clazz.getCanonicalName()));
        }
    }
}
