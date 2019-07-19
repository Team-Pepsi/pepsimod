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
    private final Map<Class<? extends Event>, List<CachedData>> CACHE = new ConcurrentHashMap<>();

    List<CachedData> getCache(@NonNull Class<? extends Event> clazz) {
        return CACHE.computeIfAbsent(clazz, EventUtil::computeCache);
    }

    private List<CachedData> computeCache(@NonNull Class<? extends Event> clazz) {
        return computeAllHandlers(clazz).stream()
                .map(interfaz -> {
                    Method implementation = findImplementationOf(clazz, interfaz);
                    PepsiEvent pepsiEvent = implementation.getAnnotation(PepsiEvent.class);
                    return pepsiEvent == null ? new CachedData(clazz, EventPriority.NORMAL, true) : new CachedData(clazz, pepsiEvent.priority(), pepsiEvent.addByDefault());
                })
                .collect(ImmutableList.toImmutableList());
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
