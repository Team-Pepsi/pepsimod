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

package net.daporkchop.pepsimod.util;

import lombok.NonNull;
import net.daporkchop.pepsimod.util.event.EventPriority;
import net.daporkchop.pepsimod.util.event.render.PreRenderEvent;
import net.daporkchop.pepsimod.util.render.text.RainbowTextRenderer;
import net.daporkchop.pepsimod.util.render.text.TextRenderer;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * Common methods used throughout the mod.
 *
 * @author DaPorkchop_
 */
public final class PepsiUtil implements PepsiConstants {
    public static final int[]           PEPSI_LOGO_SIZES = {16, 32, 64, 128, 256};
    public static final BufferedImage[] PEPSI_LOGOS      = new BufferedImage[PEPSI_LOGO_SIZES.length];
    public static final char[]          RANDOM_COLORS    = {'c', '9', 'f', '1', '4'};
    public static       TextRenderer    TEXT_RENDERER    = new RainbowTextRenderer(0.2d, 0.03d, 45.0d);
    public static final Field           FIELD_MODIFIERS  = getField(Field.class, "modifiers");

    protected static final Object  PEPSIUTIL_MUTEX            = new Object[0];
    protected static       boolean STANDARD_EVENTS_REGISTERED = false;

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
    public static <T> T getNull() {
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
    public static <T> T getInputValue(T val) {
        return val;
    }

    /**
     * Registers all standard event listeners (listeners that are always active).
     * <p>
     * May only be invoked once by {@link net.daporkchop.pepsimod.Pepsimod#preInit(FMLPreInitializationEvent)}.
     */
    public static void registerStandardEvents() {
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
    public static Field getField(@NonNull Class<?> clazz, @NonNull String... names) throws IllegalStateException {
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
    public static void putStaticFinalField(Object val, @NonNull Class<?> clazz, @NonNull String... names) throws IllegalStateException {
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
    public static void putFinalField(Object obj, Object val, @NonNull Class<?> clazz, @NonNull String... names) throws IllegalStateException {
        try {
            Field field = getField(clazz, names);
            FIELD_MODIFIERS.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(obj, val);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
