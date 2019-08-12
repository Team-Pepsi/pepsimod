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
