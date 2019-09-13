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
