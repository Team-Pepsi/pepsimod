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

import com.google.common.collect.ImmutableSet;
import lombok.NonNull;
import net.daporkchop.pepsimod.util.PepsiConstants;
import net.daporkchop.pepsimod.util.event.annotation.PepsiEvent;
import net.daporkchop.pepsimod.util.event.impl.AllEvents;
import net.daporkchop.pepsimod.util.event.impl.Event;
import net.daporkchop.pepsimod.util.event.impl.render.PreRenderEvent;
import net.daporkchop.pepsimod.util.event.impl.render.RenderHUDEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * pepsimod's event manager.
 * <p>
 * This class is used to actually fire events and add/remove handlers, and is accessed via {@link PepsiConstants#EVENT_MANAGER}. It's optimized to cause
 * as few object allocations as reasonably possible, with quality of code being marginal as a result.
 * <p>
 * This is superior to other annotation+class-based event handlers as it doesn't require object allocations every time an event is fired, however
 * it requires a method to be added for every event, and all event fields must be passed as method parameters.
 *
 * @author DaPorkchop_
 */
public final class EventManager implements AllEvents, PepsiConstants {
    protected static final Constructor<List<? extends Event>> LIST_CONSTRUCTOR;
    public static final    Collection<Class<? extends Event>> EVENT_CLASSES;

    static {
        Constructor<List<? extends Event>> listConstructor = null;
        Collection<Class<? extends Event>> eventClasses = new HashSet<>();
        try {
            {
                //i hate java
                @SuppressWarnings("unchecked")
                Constructor<List<? extends Event>> listConstructor_butINeedItToBeUnchecked
                        = (Constructor<List<? extends Event>>) Class.forName("sun.awt.util.IdentityArrayList").getConstructor();
                listConstructor = listConstructor_butINeedItToBeUnchecked;
            }
            @SuppressWarnings("unchecked")
            Class<? extends Event>[] interfaces = (Class<? extends Event>[]) AllEvents.class.getInterfaces();
            for (Class<? extends Event> clazz : interfaces) {
                if (Event.class.isAssignableFrom(clazz)) {
                    eventClasses.add(clazz);
                } else {
                    log.warn("%s holds non-event interface: %s", AllEvents.class, clazz);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            LIST_CONSTRUCTOR = listConstructor;
            EVENT_CLASSES = ImmutableSet.copyOf(eventClasses);
        }
    }

    /**
     * I need to reflect to get access to {@link sun.awt.util.IdentityArrayList}, since using any classes in the {@link sun} package cause un-suppressable
     * compile-time warnings.
     *
     * @return a new instance of {@link sun.awt.util.IdentityArrayList}
     */
    @SuppressWarnings("unchecked")
    protected static <E extends Event> List<E> createList() {
        try {
            return (List<E>) LIST_CONSTRUCTOR.newInstance();
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    protected final Map<Class<? extends Event>, List<? extends Event>[]> activeHandlers = new IdentityHashMap<>();
    protected final Lock readLock;
    protected final Lock writeLock;

    public EventManager() {
        if (EVENT_MANAGER != null) {
            throw new IllegalStateException("Event manager already instantiated!");
        }

        @SuppressWarnings("unchecked")
        List<? extends Event>[] templateArray = new List[EventPriority.VALUES.length];
        for (int i = templateArray.length - 1; i >= 0; i--) {
            templateArray[i] = Collections.emptyList();
        }

        //intialize active handlers map
        for (Class<? extends Event> clazz : EVENT_CLASSES) {
            this.activeHandlers.put(clazz, templateArray.clone());
        }

        //eliminate pointer chasing by directly referencing the read and the write lock
        ReadWriteLock lock = new ReentrantReadWriteLock();
        this.readLock = lock.readLock();
        this.writeLock = lock.writeLock();
    }

    //
    //
    //internal methods
    //
    //

    /**
     * Gets all the currently active handlers for the given event class.
     *
     * @param clazz the event class
     * @param <E>   the event type (same as the class)
     * @return currently active handlers for the given event class
     */
    protected <E extends Event> List<E>[] getHandlers(@NonNull Class<E> clazz) {
        @SuppressWarnings("unchecked")
        List<E>[] handlers = (List<E>[]) this.activeHandlers.get(clazz);
        if (handlers != null) {
            return handlers;
        } else { //put throw last to avoid unnecessary branch
            throw new IllegalStateException(String.format("Unregistered event class: \"%s\"", clazz));
        }
    }

    //
    //
    //register/deregister methods
    //
    //

    /**
     * Registers all handlers defined in the given object's class, excluding those with the {@link PepsiEvent} where {@link PepsiEvent#addByDefault()} is set
     * to {@code false}.
     *
     * @param handler the event handler
     */
    @SuppressWarnings("unchecked")
    public void register(@NonNull Event handler) {
        EventUtil.CachedData[] cache = EventUtil.getCache(handler.getClass());
        this.writeLock.lock();
        try {
            for (EventUtil.CachedData data : cache) {
                if (data.def) {
                    this.register((Class<Event>) data.clazz, handler, data.priority);
                }
            }
        } finally {
            this.writeLock.unlock();
        }
    }

    /**
     * Registers all handlers defined in the given object's class, including those with the {@link PepsiEvent} where {@link PepsiEvent#addByDefault()} is set
     * to {@code false}.
     *
     * @param handler the event handler
     */
    @SuppressWarnings("unchecked")
    public void registerAll(@NonNull Event handler) {
        EventUtil.CachedData[] cache = EventUtil.getCache(handler.getClass());
        this.writeLock.lock();
        try {
            for (EventUtil.CachedData data : cache) {
                this.register((Class<Event>) data.clazz, handler, data.priority);
            }
        } finally {
            this.writeLock.unlock();
        }
    }

    /**
     * @see #register(Class, Event, EventPriority)
     */
    public <E extends Event> boolean register(@NonNull Class<E> eventClass, @NonNull E handler) {
        return this.register(eventClass, handler, EventPriority.NORMAL);
    }

    /**
     * Registers a new event handler that will listen for the given event class.
     * <p>
     * This will ignore any annotations applied to the event handler class, as this is intended for lambdas.
     *
     * @param eventClass the class of the event that will be listened for
     * @param handler    the handler for the given event
     * @param priority   the priority of the new handler
     * @param <E>        the event type (same as the class)
     * @return whether or not the handler was registered (if {@code false}, it was already added)
     */
    public <E extends Event> boolean register(@NonNull Class<E> eventClass, @NonNull E handler, @NonNull EventPriority priority) {
        this.writeLock.lock();
        try {
            List<E>[] handlers = this.getHandlers(eventClass);
            List<E> list = handlers[priority.ordinal()];
            if (list.isEmpty()) {
                //we need to add the handler
                list = createList();
                list.add(handler);
                handlers[priority.ordinal()] = list;
                return true;
            } else if (!list.contains(handler)) {
                list.add(handler);
                return true;
            } else {
                return false;
            }
        } finally {
            this.writeLock.unlock();
        }
    }

    /**
     * Deregisters all handlers defined in the given object's class.
     *
     * @param handler the event handler
     */
    @SuppressWarnings("unchecked")
    public void deregister(@NonNull Event handler) {
        EventUtil.CachedData[] cache = EventUtil.getCache(handler.getClass());
        this.writeLock.lock();
        try {
            for (EventUtil.CachedData data : cache) {
                this.deregister((Class<Event>) data.clazz, handler, data.priority);
            }
        } finally {
            this.writeLock.unlock();
        }
    }

    /**
     * @see #deregister(Class, Event, EventPriority)
     */
    public <E extends Event> boolean deregister(@NonNull Class<E> eventClass, @NonNull E handler) {
        return this.deregister(eventClass, handler, null);
    }

    /**
     * Deregisters an already registered event handler that is currently listening for the given event class.
     *
     * @param eventClass the class of the event that is being listened for
     * @param handler    the handler for the given event
     * @param priority   the priority with which the event was registered. If unknown, {@code null} may be passed, which will result in a brute-force search
     *                   through all valid priority levels
     * @param <E>        the event type (same as the class)
     * @return whether or not the handler was deregistered (if {@code false}, it wasn't registered)
     */
    public <E extends Event> boolean deregister(@NonNull Class<E> eventClass, @NonNull E handler, EventPriority priority) {
        this.writeLock.lock();
        try {
            List<E>[] handlers = this.getHandlers(eventClass);
            if (priority == null) {
                for (int i = 5; i >= 0; i--) {
                    List<E> list = handlers[i];
                    if (!list.isEmpty() && list.remove(handler)) {
                        if (list.isEmpty()) {
                            //replace with empty list if all handlers are removed
                            handlers[i] = Collections.emptyList();
                        }
                        return true;
                    }
                }
            } else {
                List<E> list = handlers[priority.ordinal()];
                if (!list.isEmpty() && list.remove(handler)) {
                    if (list.isEmpty()) {
                        //replace with empty list if all handlers are removed
                        handlers[priority.ordinal()] = Collections.emptyList();
                    }
                    return true;
                }
            }
            return false;
        } finally {
            this.writeLock.unlock();
        }
    }

    //
    //
    // event fire methods
    //
    //

    @Override
    public void firePreRender(float partialTicks) {
        this.readLock.lock();
        try {
            for (List<PreRenderEvent> handlers : this.getHandlers(PreRenderEvent.class)) {
                for (int i = handlers.size() - 1; i >= 0; i--) {
                    handlers.get(i).firePreRender(partialTicks);
                }
            }
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public EventStatus firePreRenderHUD(float partialTicks, int width, int height) {
        this.readLock.lock();
        try {
            EventStatus status = EventStatus.OK;
            for (List<RenderHUDEvent.Pre> handlers : this.getHandlers(RenderHUDEvent.Pre.class)) {
                for (int i = handlers.size() - 1; i >= 0; i--) {
                    switch (handlers.get(i).firePreRenderHUD(partialTicks, width, height)) {
                        case CANCEL:
                            status = EventStatus.CANCEL;
                            break;
                        case ABORT:
                            return EventStatus.ABORT;
                    }
                }
            }
            return status;
        } finally {
            this.readLock.unlock();
        }
    }

    @Override
    public void firePostRenderHUD(float partialTicks, int width, int height) {
        this.readLock.lock();
        try {
            for (List<RenderHUDEvent.Post> handlers : this.getHandlers(RenderHUDEvent.Post.class)) {
                for (int i = handlers.size() - 1; i >= 0; i--) {
                    handlers.get(i).firePostRenderHUD(partialTicks, width, height);
                }
            }
        } finally {
            this.readLock.unlock();
        }
    }
}
