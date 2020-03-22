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

package net.daporkchop.pepsimod.util.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Fields decorated with this annotation are considered to be configuration options.
 *
 * @author DaPorkchop_
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Option {
    /**
     * The unique ID of this option.
     * <p>
     * This is never displayed directly to the user, but rather is used internally for things such as serialization.
     * <p>
     * If unset, defaults to the name of the field.
     */
    String id() default "";

    /**
     * A comment describing the option.
     * <p>
     * This will be used as a tooltip in the GUI, and shown as a popup when configuring the option using commands.
     * <p>
     * If unset, no comment will be shown.
     */
    String[] comment() default {};

    /**
     * The input type used for this option.
     * <p>
     * If unset, defaults to {@link Input#AUTO}.
     */
    Input input() default Input.AUTO;

    /**
     * The input type used for an option.
     * <p>
     * Only used for the GUI.
     *
     * @author DaPorkchop_
     */
    enum Input {
        /**
         * Automatically choose the type best suited for this option.
         */
        AUTO,
        /**
         * A bar that can be slid back and forth to change the value between the minimum and maximum values.
         */
        NUMBER_SLIDER,
        /**
         * A number input field with arrows on the right to increase/decrease the value by the step size.
         */
        NUMBER_SPINNER,
        /**
         * A simple, scrollable dropdown menu.
         */
        ENUM_DROPDOWN,
        /**
         * A simple, editable line of text that sits flush with other options in the GUI.
         */
        TEXT_INLINE,
        /**
         * The GUI element will be a clickable prompt to edit the value. When clicked, a fullscreen popup menu will appear to edit the text.
         */
        TEXT_POPUP,
        /**
         * The GUI element will be a clickable prompt to edit the value. When clicked, a fullscreen popup menu will appear to edit the text lines.
         * <p>
         * All text lines will be displayed in a single text box, with line breaks separating them. Text may be edited normally, and when saved, each
         * line break will be considered the beginning of a new line of text.
         */
        LINES_SINGLE,
        /**
         * The GUI element will be a clickable prompt to edit the value. When clicked, a fullscreen popup menu will appear to edit the text lines.
         * <p>
         * All text lines will be displayed in their own, single-line text box. Additional lines may be added, inserted or removed, and the order of
         * existing lines may be changed.
         */
        LINES_MULTI,
        /**
         * The option will not be configurable in the GUI.
         */
        NONE;
    }

    /**
     * Sets the default value for an option.
     * <p>
     * Must be applied to a field with the {@link Option} annotation.
     *
     * Any annotation values that do not correspond to the type will be ignored.
     *
     * @author DaPorkchop_
     */
    @interface Default  {
        boolean booleanValue() default false;

        int intValue() default 0;

        long longValue() default 0L;

        float floatValue() default 0.0f;

        double doubleValue() default 0.0d;

        String textValue() default "";

        String enumValue() default "";
    }
}
