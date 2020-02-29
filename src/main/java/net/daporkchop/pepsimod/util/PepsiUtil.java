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

package net.daporkchop.pepsimod.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import net.daporkchop.pepsimod.asm.PepsimodMixinLoader;
import net.daporkchop.pepsimod.util.event.EventPriority;
import net.daporkchop.pepsimod.util.event.impl.render.PreRenderEvent;
import net.daporkchop.pepsimod.util.render.text.FixedColorTextRenderer;
import net.daporkchop.pepsimod.util.render.text.TextRenderer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * Common methods used throughout the mod.
 *
 * @author DaPorkchop_
 */
@UtilityClass
public final class PepsiUtil implements PepsiConstants {
    public final int[]           PEPSI_LOGO_SIZES = {16, 32, 64, 128, 256};
    public final BufferedImage[] PEPSI_LOGOS      = new BufferedImage[PEPSI_LOGO_SIZES.length];
    public final char[]          RANDOM_COLORS    = {'c', '9', 'f', '1', '4'};
    public       TextRenderer    TEXT_RENDERER    = new FixedColorTextRenderer(Color.RED);
    public final Field           FIELD_MODIFIERS  = getField(Field.class, "modifiers");

    protected final Object  PEPSIUTIL_MUTEX            = new Object[0];
    protected       boolean STANDARD_EVENTS_REGISTERED = false;

    static {
        for (int i = PEPSI_LOGOS.length - 1; i >= 0; i--) {
            try (InputStream in = PepsiUtil.class.getResourceAsStream(String.format("/assets/pepsimod/textures/icon/pepsilogo-%d.png", PEPSI_LOGO_SIZES[i]))) {
                PEPSI_LOGOS[i] = ImageIO.read(in);
            } catch (Exception e) {
                log.error("Unable to load pepsilogo at %1$dx%1$d resolution!", PEPSI_LOGO_SIZES[i]);
            }
        }
    }

    /**
     * This returns {@code null} in a very roundabout way. This is to work around warnings in IntelliJ that certain values are always
     * {@code null} when they're actually initialized reflectively at runtime.
     *
     * @param <T> the type of {@code null} to get
     * @return {@code null}
     */
    @SuppressWarnings("unchecked")
    public <T> T getNull() {
        Object[] o = new Object[1];
        return (T) o[0];
    }

    /**
     * This returns the input value in a very roundabout way. This is to work around warnings in IntelliJ that certain values are constant when
     * they're actually initialized reflectively at runtime.
     *
     * @param val the value to get
     * @param <T> the type of value to get
     * @return the input value
     */
    public <T> T getInputValue(T val) {
        return val;
    }

    /**
     * Registers all standard event listeners (listeners that are always active).
     * <p>
     * May only be invoked once by {@link net.daporkchop.pepsimod.Pepsimod#preInit(FMLPreInitializationEvent)}.
     */
    public void registerStandardEvents() {
        synchronized (PEPSIUTIL_MUTEX) {
            if (!STANDARD_EVENTS_REGISTERED) {
                STANDARD_EVENTS_REGISTERED = true;

                EVENT_MANAGER.register(PreRenderEvent.class, partialTicks -> {
                    TEXT_RENDERER.update();
                    RESOLUTION.update();
                }, EventPriority.MONITOR);
            } else {
                throw new IllegalStateException("Standard events already registered!");
            }
        }
    }

    /**
     * Gets a field from a class with any one of the given names.
     *
     * @param clazz the class containing the field
     * @param names all of the possible names for the field. The first match will be used
     * @return a {@link Field} with one of the given names
     * @throws IllegalStateException if no field with any of the given names could be found in the given class
     */
    public Field getField(@NonNull Class<?> clazz, @NonNull String... names) throws IllegalStateException {
        Field field = null;
        for (String name : names) {
            if (name == null) {
                throw new NullPointerException();
            }
            try {
                field = clazz.getDeclaredField(name);
                break;
            } catch (NoSuchFieldException e) {
            }
        }
        if (field != null) {
            field.setAccessible(true);
            return field;
        } else {
            throw new IllegalStateException(String.format("Couldn't find field in class \"%s\" with any of the following names: %s", clazz, Arrays.toString(names)));
        }
    }

    /**
     * Replaces the value of a {@code static final} field.
     *
     * @param val   the new value
     * @param clazz the class containing the field
     * @param names all of the possible names of the field
     * @throws IllegalStateException if no field with any of the given names could be found in the given class
     */
    public void putStaticFinalField(Object val, @NonNull Class<?> clazz, @NonNull String... names) throws IllegalStateException {
        putFinalField(null, val, clazz, names);
    }

    /**
     * Replaces the value of a {@code final} field.
     *
     * @param obj   an instance of which to replace the value
     * @param val   the new value
     * @param clazz the class containing the field
     * @param names all of the possible names of the field
     * @throws IllegalStateException if no field with any of the given names could be found in the given class
     */
    public void putFinalField(Object obj, Object val, @NonNull Class<?> clazz, @NonNull String... names) throws IllegalStateException {
        try {
            Field field = getField(clazz, names);
            FIELD_MODIFIERS.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(obj, val);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Replaces the global text renderer instance.
     *
     * @param renderer the new text renderer to use
     */
    public void setTextRenderer(@NonNull TextRenderer renderer) {
        synchronized (PEPSIUTIL_MUTEX) {
            TextRenderer old = TEXT_RENDERER;
            TEXT_RENDERER = renderer;
            old.close();
        }
    }

    /**
     * Gets a resource as an {@link InputStream}.
     * <p>
     * Functions the same as {@link Class#getResourceAsStream(String)}, with the only major difference being that this is is hot-swap safe for dev
     * environments.
     *
     * @param name the name of the resource
     * @return an {@link InputStream} allowing the reading of the resource with the given name, or {@code null} if it could not be found
     */
    public InputStream getResourceAsStream(@NonNull String name) {
        if (PepsimodMixinLoader.OBFUSCATED) {
            return PepsiUtil.class.getResourceAsStream(name);
        } else {
            try {
                return new FileInputStream(new File(mc.gameDir, "../src/main/resources" + name));
            } catch (FileNotFoundException e) {
                return null;
            }
        }
    }
}
