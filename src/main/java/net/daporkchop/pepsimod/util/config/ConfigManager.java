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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.UtilityClass;
import net.daporkchop.lib.common.function.VoidFunction;
import net.daporkchop.lib.unsafe.PUnsafe;
import net.daporkchop.pepsimod.util.PepsiConstants;
import net.daporkchop.pepsimod.util.config.annotation.Option;
import net.daporkchop.pepsimod.util.config.annotation.OptionListener;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author DaPorkchop_
 */
@UtilityClass
public class ConfigManager implements PepsiConstants {
    private final Map<Class<?>, Map<String, OptionContainer>> GLOBAL_OPTIONS = new HashMap<>();
    private final Map<Class<?>, Map<String, OptionContainer>> ALL_OPTIONS    = new HashMap<>();
    private       boolean                                     INITIALIZED    = false;

    /**
     * Initializes the config manager.
     * <p>
     * This causes it to use the given {@link ASMDataTable} to find all valid option annotations, and prepare the
     * config system for loading them later.
     *
     * @param table the {@link ASMDataTable} to use
     */
    public void init(@NonNull ASMDataTable table) {
        synchronized (ConfigManager.class) {
            if (INITIALIZED) {
                throw new IllegalStateException("ConfigManager already initialized!");
            } else {
                INITIALIZED = true;
            }
        }

        //load options themselves
        for (ASMData data : table.getAll(Option.class.getName())) {
            try {
                Class<?> clazz = Class.forName(data.getClassName());
                Field field = clazz.getField(data.getObjectName());
                OptionContainer container = (field.getModifiers() & Modifier.STATIC) != 0
                        ? new OptionContainer(PUnsafe.staticFieldOffset(field), clazz, PUnsafe.staticFieldBase(field))
                        : new OptionContainer(PUnsafe.objectFieldOffset(field), clazz, null);
                container.option.loadFromMap(data.getAnnotationInfo());
                if ((field.getModifiers() & Modifier.STATIC) != 0) {
                    GLOBAL_OPTIONS.computeIfAbsent(clazz, c -> new HashMap<>()).put(container.option.id, container);
                }
                ALL_OPTIONS.computeIfAbsent(clazz, c -> new HashMap<>()).put(container.option.id, container);
            } catch (Exception e) {
                throw new RuntimeException(String.format("Exception while parsing ASM data: className=%s,objectName=%s", data.getClassName(), data.getObjectName()), e);
            }
        }

        //TODO: load option constraints (min/max)

        //find option listeners
        for (ASMData data : table.getAll(OptionListener.class.getName()))   {
            try {
                Class<?> clazz = Class.forName(data.getClassName());
                Method method = clazz.getMethod(data.getObjectName());
            } catch (Exception e) {
                throw new RuntimeException(String.format("Exception while parsing ASM data: className=%s,objectName=%s", data.getClassName(), data.getObjectName()), e);
            }
        }

        log.info("Found %d option fields (%d of which are global), and %d option listeners.", ALL_OPTIONS.size(), GLOBAL_OPTIONS.size(), ALL_OPTIONS.values().stream().flatMap(m -> m.values().stream()).mapToLong(oc -> oc.listeners.size()).sum());
    }

    @Getter
    @Setter
    private final class OptionImpl implements Option {
        private static final String[] EMPTY_STRING_ARRAY = new String[0];

        public String id;
        public String[] comment;
        public Input input;

        public void loadFromMap(@NonNull Map<String, Object> map)   {
            this.id = (String) map.get("id");
            this.comment = (String[]) map.getOrDefault("comment", EMPTY_STRING_ARRAY);
            this.input = (Input) map.getOrDefault("input", Input.AUTO);
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return Option.class;
        }
    }

    @RequiredArgsConstructor
    private final class OptionContainer {
        public final long       offset;
        @NonNull
        public final Class<?>   holder;
        public final Object     staticFieldBase;
        public final OptionImpl option = new OptionImpl();
        public final Collection<VoidFunction> listeners = Collections.newSetFromMap(new IdentityHashMap<>());

        public boolean global() {
            return this.staticFieldBase != null;
        }
    }
}
