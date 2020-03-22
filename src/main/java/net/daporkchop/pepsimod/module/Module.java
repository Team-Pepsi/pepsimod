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

package net.daporkchop.pepsimod.module;

import net.daporkchop.pepsimod.util.event.impl.Event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The root of most pepsimod utilities are modules. These are individual, toggleable utilities with a distinct function that
 * <p>
 * i need to come up with a definition for this
 * although in all likelihood i'll forget and this useless javadoc will still be sitting here in three years
 *
 * @author DaPorkchop_
 */
public interface Module extends Event, AutoCloseable {
    /**
     * Initializes a newly created instance of this module.
     * <p>
     * This is guaranteed to be the first method that is called after the instance is created.
     */
    default void init() {
    }

    /**
     * Called before this module instance is discarded to release any additional resources.
     * <p>
     * This method will be called every time the module manager is reset, which will happen every time we disconnect from a server (dedicated or internal)
     * or change dimensions.
     * <p>
     * No guarantees are made as to what state the module will be in when this is called, as the player can disconnect, get kicked or be teleported to
     * another dimension at any moment.
     */
    @Override
    default void close() {
    }

    /**
     * Called when this module is enabled.
     * <p>
     * Any events registered by this module will be registered automatically before this method is called.
     * <p>
     * {@link #init()} is guaranteed to be called before this method.
     */
    default void enabled() {
    }

    /**
     * Called when this module is disabled.
     * <p>
     * Any events registered by this module will be deregistered automatically before this method is called.
     */
    default void disabled() {
    }

    /**
     * Required annotation for all implementations of {@link Module}. Provides additional static information about the module.
     *
     * @author DaPorkchop_
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Info {
        /**
         * The unique ID of the module.
         * <p>
         * This is never displayed directly to the user, but rather is used internally for things such as serialization.
         */
        String id();

        /**
         * An array of module classes that this module requires to be enabled before it may be enabled itself.
         * <p>
         * Currently unused.
         */
        Class<? extends Module>[] requires() default {};

        /**
         * An array of module classes that this module is incompatible with.
         * <p>
         * Currently unused.
         */
        Class<? extends Module>[] incompatible() default {};
    }
}
