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

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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
import java.util.Objects;

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
                OptionContainer container = new OptionContainer(
                        (field.getModifiers() & Modifier.STATIC) != 0 ? PUnsafe.staticFieldOffset(field) : PUnsafe.objectFieldOffset(field),
                        clazz,
                        (field.getModifiers() & Modifier.STATIC) != 0 ? PUnsafe.staticFieldBase(field) : null,
                        field.getType(),
                        Type.fromFieldType(field.getType())
                );
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
        for (ASMData data : table.getAll(OptionListener.class.getName())) {
            try {
                Class<?> clazz = Class.forName(data.getClassName());
                Method method = clazz.getMethod(data.getObjectName());
            } catch (Exception e) {
                throw new RuntimeException(String.format("Exception while parsing ASM data: className=%s,objectName=%s", data.getClassName(), data.getObjectName()), e);
            }
        }

        log.info("Found %d option fields (%d of which are global), and %d option listeners.", ALL_OPTIONS.size(), GLOBAL_OPTIONS.size(), ALL_OPTIONS.values().stream().flatMap(m -> m.values().stream()).mapToLong(oc -> oc.listeners.size()).sum());
    }

    @RequiredArgsConstructor
    @Getter
    private enum Type {
        INT(0) {
            @Override
            public void set(@NonNull OptionContainer container, Object instance, @NonNull Object value) {
                PUnsafe.putInt(
                        container.global() ? container.staticFieldBase : Objects.requireNonNull(instance),
                        container.offset,
                        ((Number) value).intValue()
                );
            }

            @Override
            public Object get(@NonNull OptionContainer container, Object instance) {
                return PUnsafe.getInt(
                        container.global() ? container.staticFieldBase : Objects.requireNonNull(instance),
                        container.offset
                );
            }
        },
        LONG(0L) {
            @Override
            public void set(@NonNull OptionContainer container, Object instance, @NonNull Object value) {
                PUnsafe.putLong(
                        container.global() ? container.staticFieldBase : Objects.requireNonNull(instance),
                        container.offset,
                        ((Number) value).longValue()
                );
            }

            @Override
            public Object get(@NonNull OptionContainer container, Object instance) {
                return PUnsafe.getLong(
                        container.global() ? container.staticFieldBase : Objects.requireNonNull(instance),
                        container.offset
                );
            }
        },
        FLOAT(0.0f) {
            @Override
            public void set(@NonNull OptionContainer container, Object instance, @NonNull Object value) {
                PUnsafe.putFloat(
                        container.global() ? container.staticFieldBase : Objects.requireNonNull(instance),
                        container.offset,
                        ((Number) value).floatValue()
                );
            }

            @Override
            public Object get(@NonNull OptionContainer container, Object instance) {
                return PUnsafe.getFloat(
                        container.global() ? container.staticFieldBase : Objects.requireNonNull(instance),
                        container.offset
                );
            }
        },
        DOUBLE(0.0d) {
            @Override
            public void set(@NonNull OptionContainer container, Object instance, @NonNull Object value) {
                PUnsafe.putDouble(
                        container.global() ? container.staticFieldBase : Objects.requireNonNull(instance),
                        container.offset,
                        ((Number) value).doubleValue()
                );
            }

            @Override
            public Object get(@NonNull OptionContainer container, Object instance) {
                return PUnsafe.getDouble(
                        container.global() ? container.staticFieldBase : Objects.requireNonNull(instance),
                        container.offset
                );
            }
        },
        TEXT("") {
            @Override
            public void set(@NonNull OptionContainer container, Object instance, @NonNull Object value) {
                PUnsafe.putObject(
                        container.global() ? container.staticFieldBase : Objects.requireNonNull(instance),
                        container.offset,
                        String.class.cast(value) //prevent the cast from being inlined so we can assert the type
                );
            }

            @Override
            public Object get(@NonNull OptionContainer container, Object instance) {
                return String.class.cast(PUnsafe.getObject(
                        container.global() ? container.staticFieldBase : Objects.requireNonNull(instance),
                        container.offset
                ));
            }
        },
        ENUM("") {
            @Override
            public void set(@NonNull OptionContainer container, Object instance, @NonNull Object value) {
                PUnsafe.putObject(
                        container.global() ? container.staticFieldBase : Objects.requireNonNull(instance),
                        container.offset,
                        String.class.cast(value) //prevent the cast from being inlined so we can assert the type
                );
            }

            @Override
            public Object get(@NonNull OptionContainer container, Object instance) {
                return String.class.cast(PUnsafe.getObject(
                        container.global() ? container.staticFieldBase : Objects.requireNonNull(instance),
                        container.offset
                ));
            }
        };

        public static Type fromFieldType(@NonNull Class<?> clazz) {
            if (clazz == int.class) {
                return INT;
            } else if (clazz == long.class) {
                return LONG;
            } else if (clazz == float.class) {
                return FLOAT;
            } else if (clazz == double.class) {
                return DOUBLE;
            } else if (clazz == String.class) {
                return TEXT;
            } else if (clazz != Enum.class && Enum.class.isAssignableFrom(clazz)) {
                return ENUM;
            } else {
                throw new IllegalArgumentException(String.format("Invalid option type: %s", clazz.getName()));
            }
        }

        protected final Object fallbackDefault;

        public Object decodeValue(@NonNull OptionContainer container, @NonNull Object valueObj)    {
            return valueObj;
        }

        public Object encodeValue(@NonNull OptionContainer container, @NonNull Object valueObj)    {
            return valueObj;
        }

        public abstract void set(@NonNull OptionContainer container, Object instance, @NonNull Object value);

        public abstract Object get(@NonNull OptionContainer container, Object instance);
    }

    @Getter
    @Setter
    private final class OptionImpl implements Option {
        private static final String[] EMPTY_STRING_ARRAY = new String[0];

        public String   id;
        public String[] comment;
        public Input    input;

        public void loadFromMap(@NonNull Map<String, Object> map) {
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
        public final long     offset;
        @NonNull
        public final Class<?> holder;
        public final Object   staticFieldBase;
        public final Class<?> typeClass;
        public final Type     type;
        public final OptionImpl               option    = new OptionImpl();
        public final Collection<VoidFunction> listeners = Collections.newSetFromMap(new IdentityHashMap<>()); //TODO: this is dumb

        public boolean global() {
            return this.staticFieldBase != null;
        }
    }
}
