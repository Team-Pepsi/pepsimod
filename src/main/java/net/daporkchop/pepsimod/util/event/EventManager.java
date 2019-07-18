/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2019 DaPorkchop_
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

import com.google.common.collect.ImmutableSet;
import lombok.NonNull;
import net.daporkchop.pepsimod.util.PepsiConstants;
import net.daporkchop.pepsimod.util.event.annotation.EventHandler;
import net.daporkchop.pepsimod.util.event.render.PreRenderEvent;
import net.daporkchop.pepsimod.util.event.render.RenderHUDEvent;

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
 * This class is used to actually fire events and add/remove handlers, and is accessed via {@link PepsiConstants#EVENT_MANAGER}.
 * <p>
 * This is superior to other annotation+class-based event handlers as it doesn't require object allocations every time an event is fired, however
 * it requires a method to be added for every event, and all event fields must be passed as method parameters.
 *
 * @author DaPorkchop_
 */
public final class EventManager implements EveryEvent, PepsiConstants {
    protected static final Constructor<List<? extends Event>> IDENTITY_LINKED_LIST_CONSTRUCTOR;
    public static final    Collection<Class<? extends Event>> EVENT_CLASSES;

    static {
        Constructor<List<? extends Event>> identityLinkedListConstructor = null;
        Collection<Class<? extends Event>> eventClasses = new HashSet<>();
        try {
            {
                //i hate java
                @SuppressWarnings("unchecked")
                Constructor<List<? extends Event>> identityLinkedListConstructor_butINeedItToBeUnchecked
                        = (Constructor<List<? extends Event>>) Class.forName("sun.awt.util.IdentityLinkedList").getConstructor();
                identityLinkedListConstructor = identityLinkedListConstructor_butINeedItToBeUnchecked;
            }
            @SuppressWarnings("unchecked")
            Class<? extends Event>[] interfaces = (Class<? extends Event>[]) EveryEvent.class.getInterfaces();
            for (Class<? extends Event> clazz : interfaces) {
                if (Event.class.isAssignableFrom(clazz)) {
                    eventClasses.add(clazz);
                } else {
                    log.warn("%s holds non-event interface: %s", EveryEvent.class, clazz);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IDENTITY_LINKED_LIST_CONSTRUCTOR = identityLinkedListConstructor;
            EVENT_CLASSES = ImmutableSet.copyOf(eventClasses);
        }
    }

    /**
     * I need to reflect to get access to {@link sun.awt.util.IdentityLinkedList}, since using any classes in the {@link sun} package cause un-suppressable
     * compile-time warnings.
     *
     * @return a new instance of {@link sun.awt.util.IdentityLinkedList}
     */
    protected static List<? extends Event> createIdentityLinkedList() {
        try {
            return IDENTITY_LINKED_LIST_CONSTRUCTOR.newInstance();
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    protected final Map<Class<? extends Event>, List<? extends Event>> activeHandlers = new IdentityHashMap<>();
    protected final Lock readLock;
    protected final Lock writeLock;

    public EventManager() {
        if (EVENT_MANAGER != null) {
            throw new IllegalStateException("Event manager already instantiated!");
        }

        //intialize active handlers map
        for (Class<? extends Event> clazz : EVENT_CLASSES) {
            this.activeHandlers.put(clazz, Collections.emptyList());
        }

        //eliminate pointer chasing by directly referencing the read and the write lock
        ReadWriteLock lock = new ReentrantReadWriteLock();
        this.readLock = lock.readLock();
        this.writeLock = lock.writeLock();
    }

    /**
     * @see #register(Class, Event, EventPriority)
     */
    public <E extends Event> boolean register(@NonNull Class<E> eventClass, @NonNull E handler) {
        return this.register(eventClass, handler, EventPriority.NORMAL);
    }

    /**
     * Registers an new event handler that will listen for the given event class.
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
            Collection<E> handlers = this.getHandlers(eventClass);
            if (!handlers.contains(handler)) {
                handlers.add(handler);
                return true;
            } else {
                return false;
            }
        } finally {
            this.writeLock.lock();
        }
    }

    /**
     * Registers all handlers defined in the given object, excluding those with the {@link EventHandler} where {@link EventHandler#addByDefault()} is set
     * to {@code false}.
     *
     * @param handler the method handler
     */
    @SuppressWarnings("unchecked")
    public void register(@NonNull Event handler) {
        Map<Class<? extends Event>, EventUtil.EventCache> cache = EventUtil.getCache(handler.getClass());
        this.writeLock.lock();
        try {
            for (EventUtil.EventCache cache1 : cache.values())  {
                if (cache1.def) {
                    this.register((Class<Event>) cache1.clazz, handler, cache1.priority);
                }
            }
        } finally {
            this.writeLock.lock();
        }
    }

    /**
     * Gets all the currently active handlers for the given event class.
     *
     * @param clazz the event class
     * @param <E>   the event type (same as the class)
     * @return currently active handlers for the given event class
     */
    protected <E extends Event> List<E> getHandlers(@NonNull Class<E> clazz) {
        @SuppressWarnings("unchecked")
        List<E> handlers = (List<E>) this.activeHandlers.get(clazz);
        if (handlers != null) {
            return handlers;
        } else { //put throw last to avoid unnecessary branch
            throw new IllegalStateException(String.format("Unregistered event class: \"%s\"", clazz));
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
            for (PreRenderEvent event : this.getHandlers(PreRenderEvent.class)) {
                event.firePreRender(partialTicks);
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
            for (RenderHUDEvent.Pre event : this.getHandlers(RenderHUDEvent.Pre.class)) {
                switch (event.firePreRenderHUD(partialTicks, width, height)) {
                    case CANCEL:
                        status = EventStatus.CANCEL;
                        break;
                    case ABORT:
                        return EventStatus.ABORT;
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
            for (RenderHUDEvent.Post event : this.getHandlers(RenderHUDEvent.Post.class)) {
                event.firePostRenderHUD(partialTicks, width, height);
            }
        } finally {
            this.readLock.unlock();
        }
    }
}
