/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2016-2019 DaPorkchop_
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
@Accessors(fluent = true)
public class Mod<M extends Module> {
    protected       M           instance; //global module instance reference
    protected final Class<M>    clazz; //the module's class
    protected final Supplier<M> factory; //supplies new instances for use after module manager reloads

    protected final String id;
    protected final String name;

    protected boolean enabled;
    protected boolean visible;

    public Mod(@NonNull Class<M> clazz, @NonNull Supplier<M> factory) {
        this.clazz = clazz;
        this.factory = factory;

        Module.Info info = clazz.getAnnotation(Module.Info.class);
        if (info != null) {
            this.id = info.id();
            this.name = info.name().isEmpty() ? clazz.getSimpleName() : info.name();
        } else {
            throw new IllegalArgumentException(String.format("Class %s is missing @Module.Info annotation!", clazz.getCanonicalName()));
        }
    }
}
