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

package net.daporkchop.pepsimod.util.config;

import lombok.NonNull;

/**
 * A key => value store for settings, using strings as keys.
 * <p>
 * Note that many instances of this may store only some (or none) of the settings, delegating requests to other configuration instances. This system allows
 * for e.g. server-, world- or even dimension-specific configurations, where only the differences between the current configuration and the base are
 * stored, enabling much more fine-grained control over config.
 *
 * @author DaPorkchop_
 */
public interface Configuration {
    /**
     * Gets a configuration object.
     *
     * @param qualifiedName the qualified name of the object, separated with periods
     * @return the configuration object
     */
    Configuration getObj(@NonNull String qualifiedName);

    /**
     * Gets a configuration field.
     *
     * @param qualifiedName the qualified name of the field, separated with periods
     * @return the field's value
     */
    int getInt(@NonNull String qualifiedName);
}
