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
         * The display name of the module.
         * <p>
         * If empty (which is the default), the name of the class will be used instead.
         */
        String name() default "";

        /**
         * An array of module classes that this module requires to be enabled before it may be enabled itself.
         * <p>
         * Currently unused.
         */
        Class<? extends Module>[] requires() default {};
    }
}
