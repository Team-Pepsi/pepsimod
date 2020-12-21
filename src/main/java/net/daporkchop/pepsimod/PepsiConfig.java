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

package net.daporkchop.pepsimod;

import net.daporkchop.pepsimod.module.Modules;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Consumer;

import static net.daporkchop.lib.common.util.PorkUtil.*;

/**
 * Forge configuration root for pepsimod.
 *
 * @author DaPorkchop_
 */
@Mod.EventBusSubscriber(modid = Pepsimod.MODID)
@Config(modid = Pepsimod.MODID)
public final class PepsiConfig {
    private static final Class[] CONFIG_ROOTS = {
            PepsiConfig.class,
            Modules.class
    };

    public static void sync() {
        for (Class root : CONFIG_ROOTS) {
            forEachFieldTyped(root, null, ConfigNode.class, ConfigNode::save);
        }

        ConfigManager.sync(Pepsimod.MODID, Config.Type.INSTANCE);

        load();
    }

    public static void load() {
        for (Class root : CONFIG_ROOTS) {
            forEachFieldTyped(root, null, ConfigNode.class, ConfigNode::load);
        }
    }

    /**
     * @deprecated internal API, do not touch!
     */
    @Deprecated
    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(Pepsimod.MODID)) {
            sync();
        }
    }

    private static <I, T> void forEachFieldTyped(Class<I> clazz, I instance, Class<T> type, Consumer<T> callback) {
        int checkFlag = instance == null ? Modifier.STATIC : 0;
        for (Field field : clazz.getFields()) {
            if ((field.getModifiers() & Modifier.STATIC) == checkFlag && type.isAssignableFrom(field.getType())) {
                try {
                    callback.accept(uncheckedCast(field.get(instance)));
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException("unable to access field: " + field, e);
                }
            }
        }
    }

    private PepsiConfig() {
        throw new UnsupportedOperationException();
    }

    /**
     * A type that contains complex configuration values that cannot be serialized by the Forge configuration system.
     *
     * @author DaPorkchop_
     */
    public interface ConfigNode {
        /**
         * Saves complex values from their parsed state into a format serializable by Forge.
         */
        default void save() {
            forEachFieldTyped(uncheckedCast(this.getClass()), this, ConfigNode.class, ConfigNode::save);
        }

        /**
         * Loads complex values from a format serializable by Forge into their parsed state.
         */
        default void load() {
            forEachFieldTyped(uncheckedCast(this.getClass()), this, ConfigNode.class, ConfigNode::load);
        }
    }
}
