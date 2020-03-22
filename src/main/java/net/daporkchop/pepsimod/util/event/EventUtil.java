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

package net.daporkchop.pepsimod.util.event;

import com.google.common.collect.ImmutableList;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import net.daporkchop.pepsimod.util.event.annotation.PepsiEvent;
import net.daporkchop.pepsimod.util.event.impl.Event;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static net.daporkchop.pepsimod.util.event.EventManager.EVENT_CLASSES;

/**
 * @author DaPorkchop_
 */
@UtilityClass
class EventUtil {
    private final Map<Class<? extends Event>, CachedData[]> CACHE = new ConcurrentHashMap<>();

    CachedData[] getCache(@NonNull Class<? extends Event> clazz) {
        return CACHE.computeIfAbsent(clazz, EventUtil::computeCache);
    }

    private CachedData[] computeCache(@NonNull Class<? extends Event> clazz) {
        return computeAllHandlers(clazz).stream()
                .map(interfaz -> {
                    Method implementation = findImplementationOf(clazz, interfaz);
                    PepsiEvent pepsiEvent = implementation.getAnnotation(PepsiEvent.class);
                    return pepsiEvent == null ? new CachedData(clazz, EventPriority.NORMAL, true) : new CachedData(clazz, pepsiEvent.priority(), pepsiEvent.addByDefault());
                })
                .toArray(CachedData[]::new);
    }

    private Method findImplementationOf(@NonNull Class<? extends Event> clazz, @NonNull Class<? extends Event> interfaz) {
        try {
            Method method = interfaz.getDeclaredMethods()[0];
            return clazz.getMethod(method.getName(), method.getParameterTypes());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private List<Class<? extends Event>> computeAllHandlers(@NonNull Class<? extends Event> clazz) {
        return (List<Class<? extends Event>>) (Object) findClassHeirachy(clazz).stream().filter(EVENT_CLASSES::contains).collect(Collectors.toList());
    }

    private Collection<Class<?>> findClassHeirachy(@NonNull Class<? extends Event> clazz) {
        Collection<Class<?>> heirachy = new HashSet<>();
        do_findClassHeirachy(clazz, heirachy);
        return heirachy;
    }

    private void do_findClassHeirachy(Class<?> clazz, @NonNull Collection<Class<?>> heirachy) {
        if (clazz != null && clazz != Object.class && heirachy.add(clazz)) {
            if (heirachy.add(clazz.getSuperclass())) {
                do_findClassHeirachy(clazz.getSuperclass(), heirachy);
            }
            for (Class<?> interfaz : clazz.getInterfaces()) {
                do_findClassHeirachy(interfaz, heirachy);
            }
        }
    }

    @RequiredArgsConstructor
    class CachedData {
        @NonNull
        protected final Class<? extends Event> clazz;
        @NonNull
        protected final EventPriority priority;
        protected final boolean       def;
    }
}
