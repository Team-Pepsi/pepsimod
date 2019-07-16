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

import lombok.NonNull;
import net.daporkchop.pepsimod.util.PepsiConstants;
import net.daporkchop.pepsimod.util.event.render.PreRenderEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
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
    protected static final Constructor<Collection<? extends Event>> IDENTITY_LINKED_LIST_CONSTRUCTOR;
    public static final    Collection<Class<? extends Event>>       EVENT_CLASSES;

    static {
        Constructor<Collection<? extends Event>> identityLinkedListConstructor = null;
        Collection<Class<? extends Event>> eventClasses = Collections.newSetFromMap(new IdentityHashMap<>());
        try {
            {
                //i hate java
                @SuppressWarnings("unchecked")
                Constructor<Collection<? extends Event>> identityLinkedListConstructor_butINeedItToBeUnchecked
                        = (Constructor<Collection<? extends Event>>) Class.forName("sun.awt.util.IdentityLinkedList").getConstructor();
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
            EVENT_CLASSES = Collections.unmodifiableCollection(eventClasses);
        }
    }

    /**
     * I need to reflect to get access to {@link sun.awt.util.IdentityLinkedList}, since using any classes in the {@link sun} package cause un-suppressable
     * compile-time warnings.
     *
     * @return a new instance of {@link sun.awt.util.IdentityLinkedList}
     */
    protected static Collection<? extends Event> createIdentityLinkedList() {
        try {
            return IDENTITY_LINKED_LIST_CONSTRUCTOR.newInstance();
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    protected final Map<Class<? extends Event>, Collection<? extends Event>> activeHandlers = new IdentityHashMap<>();
    protected final Lock readLock;
    protected final Lock writeLock;

    public EventManager() {
        if (EVENT_MANAGER != null) {
            throw new IllegalStateException("Event manager already instantiated!");
        }

        //intialize active handlers map
        for (Class<? extends Event> clazz : EVENT_CLASSES) {
            this.activeHandlers.put(clazz, createIdentityLinkedList());
        }

        //eliminate pointer chasing by directly referencing the read and the write lock
        ReadWriteLock lock = new ReentrantReadWriteLock();
        this.readLock = lock.readLock();
        this.writeLock = lock.writeLock();
    }

    /**
     * Registers an new event handler that will listen for the given event class.
     *
     * @param eventClass the class of the event that will be listened for
     * @param handler    the handler for the given event
     * @param <E>        the event type (same as the class)
     * @return whether or not the handler was registered (if {@code false}, it was already added)
     */
    public <E extends Event> boolean register(@NonNull Class<E> eventClass, @NonNull E handler) {
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
     * Gets all the currently active handlers for the given event class.
     *
     * @param clazz the event class
     * @param <E>   the event type (same as the class)
     * @return currently active handlers for the given event class
     */
    protected <E extends Event> Collection<E> getHandlers(@NonNull Class<E> clazz) {
        @SuppressWarnings("unchecked")
        Collection<E> handlers = (Collection<E>) this.activeHandlers.get(clazz);
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
}
