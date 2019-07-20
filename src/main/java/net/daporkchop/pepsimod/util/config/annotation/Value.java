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

import lombok.experimental.UtilityClass;

/**
 * Container class for default value annotations for {@link Option} fields.
 *
 * @author DaPorkchop_
 */
@UtilityClass
public class Value {
    /**
     * The input type used for an option whose value is numeric.
     * <p>
     * Only used for the GUI.
     *
     * @author DaPorkchop_
     * @see Int
     * @see Long
     * @see Float
     * @see Double
     */
    public enum NumberInput {
        /**
         * A bar that can be slid back and forth to change the value between the minimum and maximum values.
         */
        SLIDER,
        /**
         * A number input field with arrows on the right to increase/decrease the value by the step size.
         */
        SPINNER,
        /**
         * The option will not be configurable in the GUI.
         */
        NONE;
    }

    /**
     * The input type used for an option whose value is an enum.
     * <p>
     * Only used for the GUI.
     *
     * @author DaPorkchop_
     * @see Enum
     */
    public enum EnumInput {
        /**
         * A simple, scrollable dropdown menu.
         */
        DROPDOWN,
        /**
         * The option will not be configurable in the GUI.
         */
        NONE;
    }

    /**
     * The input type used for an option whose value is text.
     * <p>
     * Only used for the GUI.
     *
     * @author DaPorkchop_
     * @see Text
     */
    public enum TextInput {
        /**
         * A simple, editable line of text that sits flush with other options in the GUI.
         */
        INLINE,
        /**
         * The GUI element will be a clickable prompt to edit the value. When clicked, a fullscreen popup menu will appear to edit the text.
         */
        POPUP,
        /**
         * The option will not be configurable in the GUI.
         */
        NONE;
    }

    /**
     * The input type used for an option whose value is multiple lines of text.
     * <p>
     * Only used for the GUI.
     *
     * @author DaPorkchop_
     * @see TextLines
     */
    public enum TextLinesInput {
        /**
         * The GUI element will be a clickable prompt to edit the value. When clicked, a fullscreen popup menu will appear to edit the text lines.
         * <p>
         * All text lines will be displayed in a single text box, with line breaks separating them. Text may be edited normally, and when saved, each
         * line break will be considered the beginning of a new line of text.
         */
        POPUP_SINGLE,
        /**
         * The GUI element will be a clickable prompt to edit the value. When clicked, a fullscreen popup menu will appear to edit the text lines.
         * <p>
         * All text lines will be displayed in their own, single-line text box. Additional lines may be added, inserted or removed, and the order of
         * existing lines may be changed.
         */
        POPUP_MULTI,
        /**
         * The option will not be configurable in the GUI.
         */
        NONE;
    }

    /**
     * Sets the option's default value.
     *
     * @author DaPorkchop_
     */
    public @interface Bool {
        /**
         * The option's default value.
         */
        boolean value();
    }

    /**
     * Sets the option's default value.
     *
     * @author DaPorkchop_
     */
    public @interface Int {
        /**
         * The option's default value.
         */
        int value();

        /**
         * The GUI input type used for this option.
         */
        NumberInput input() default NumberInput.SLIDER;

        /**
         * The step size used for GUI input.
         * <p>
         * This has a different effect depending on the input type (defined in {@link #input()}:
         * - if {@link NumberInput#SLIDER}, the slider will jump to the closest multiple of the step size.
         * - if {@link NumberInput#SPINNER}, the arrows will increment/decrement the value by the step size
         * <p>
         * Defaults to {@code 1}.
         */
        int step() default 1;

        /**
         * The minimum (lowest) value that this option may be set to.
         * <p>
         * If {@code -1}, it will use the value from {@link #min()}.
         * <p>
         * This is only used for GUI input, to set the actual limit use {@link #min()}.
         * <p>
         * Defaults to {@code 0}.
         */
        int softMin() default 0;

        /**
         * The maximum (lowest) value that this option may be set to.
         * <p>
         * If {@code -1}, it will use the value from {@link #max()}.
         * <p>
         * This is only used for GUI input, to set the actual limit use {@link #max()}.
         * <p>
         * Defaults to {@code 100}.
         */
        int softMax() default 100;

        /**
         * The minimum (lowest) value that this option may be set to.
         * <p>
         * Defaults to {@link Integer#MIN_VALUE}.
         */
        int min() default Integer.MIN_VALUE;

        /**
         * The maximum (highest) value that this option may be set to.
         * <p>
         * Defaults to {@link Integer#MAX_VALUE}.
         */
        int max() default Integer.MAX_VALUE;
    }

    /**
     * Sets the option's default value.
     *
     * @author DaPorkchop_
     */
    public @interface Long {
        /**
         * The option's default value.
         */
        long value();

        /**
         * The GUI input type used for this option.
         */
        NumberInput input() default NumberInput.SLIDER;

        /**
         * The step size used for GUI input.
         * <p>
         * This has a different effect depending on the input type (defined in {@link #input()}:
         * - if {@link NumberInput#SLIDER}, the slider will jump to the closest multiple of the step size.
         * - if {@link NumberInput#SPINNER}, the arrows will increment/decrement the value by the step size
         * <p>
         * Defaults to {@code 1}.
         */
        long step() default 1;

        /**
         * The minimum (lowest) value that this option may be set to.
         * <p>
         * If {@code -1L}, it will use the value from {@link #min()}.
         * <p>
         * This is only used for GUI input, to set the actual limit use {@link #min()}.
         * <p>
         * Defaults to {@code 0L}.
         */
        long softMin() default 0L;

        /**
         * The maximum (lowest) value that this option may be set to.
         * <p>
         * If {@code -1L}, it will use the value from {@link #max()}.
         * <p>
         * This is only used for GUI input, to set the actual limit use {@link #max()}.
         * <p>
         * Defaults to {@code 100L}.
         */
        long softMax() default 100L;

        /**
         * The minimum (lowest) value that this option may be set to.
         * <p>
         * Defaults to {@link java.lang.Long#MIN_VALUE}.
         */
        long min() default java.lang.Long.MIN_VALUE;

        /**
         * The maximum (highest) value that this option may be set to.
         * <p>
         * Defaults to {@link java.lang.Long#MAX_VALUE}.
         */
        long max() default java.lang.Long.MAX_VALUE;
    }

    /**
     * Sets the option's default value.
     *
     * @author DaPorkchop_
     */
    public @interface Float {
        /**
         * The option's default value.
         */
        float value();

        /**
         * The GUI input type used for this option.
         */
        NumberInput input() default NumberInput.SLIDER;

        /**
         * The step size used for GUI input.
         * <p>
         * This has a different effect depending on the input type (defined in {@link #input()}:
         * - if {@link NumberInput#SLIDER}, the slider will jump to the closest multiple of the step size.
         * - if {@link NumberInput#SPINNER}, the arrows will increment/decrement the value by the step size
         * <p>
         * Defaults to {@code 0.1f}.
         */
        float step() default 0.1f;

        /**
         * The minimum (lowest) value that this option may be set to.
         * <p>
         * If {@link java.lang.Float#NaN}, it will use the value from {@link #min()}.
         * <p>
         * This is only used for GUI input, to set the actual limit use {@link #min()}.
         * <p>
         * Defaults to {@code 0.0f}.
         */
        float softMin() default 0.0f;

        /**
         * The maximum (lowest) value that this option may be set to.
         * <p>
         * If {@link java.lang.Float#NaN}, it will use the value from {@link #max()}.
         * <p>
         * This is only used for GUI input, to set the actual limit use {@link #max()}.
         * <p>
         * Defaults to {@code 1.0f}.
         */
        float softMax() default 1.0f;

        /**
         * The minimum (lowest) value that this option may be set to.
         * <p>
         * Defaults to {@link java.lang.Float#MIN_VALUE}.
         */
        float min() default java.lang.Float.MIN_VALUE;

        /**
         * The maximum (highest) value that this option may be set to.
         * <p>
         * Defaults to {@link java.lang.Float#MAX_VALUE}.
         */
        float max() default java.lang.Float.MAX_VALUE;
    }

    /**
     * Sets the option's default value.
     *
     * @author DaPorkchop_
     */
    public @interface Double {
        /**
         * The option's default value.
         */
        double value();

        /**
         * The GUI input type used for this option.
         */
        NumberInput input() default NumberInput.SLIDER;

        /**
         * The step size used for GUI input.
         * <p>
         * This has a different effect depending on the input type (defined in {@link #input()}:
         * - if {@link NumberInput#SLIDER}, the slider will jump to the closest multiple of the step size.
         * - if {@link NumberInput#SPINNER}, the arrows will increment/decrement the value by the step size
         * <p>
         * Defaults to {@code 0.1d}.
         */
        double step() default 0.1d;

        /**
         * The minimum (lowest) value that this option may be set to.
         * <p>
         * If {@link java.lang.Double#NaN}, it will use the value from {@link #min()}.
         * <p>
         * This is only used for GUI input, to set the actual limit use {@link #min()}.
         * <p>
         * Defaults to {@code 0.0d}.
         */
        double softMin() default 0.0d;

        /**
         * The maximum (lowest) value that this option may be set to.
         * <p>
         * If {@link java.lang.Double#NaN}, it will use the value from {@link #max()}.
         * <p>
         * This is only used for GUI input, to set the actual limit use {@link #max()}.
         * <p>
         * Defaults to {@code 1.0d}.
         */
        double softMax() default 1.0d;

        /**
         * The minimum (lowest) value that this option may be set to.
         * <p>
         * Defaults to {@link java.lang.Double#MIN_VALUE}.
         */
        double min() default java.lang.Double.MIN_VALUE;

        /**
         * The maximum (highest) value that this option may be set to.
         * <p>
         * Defaults to {@link java.lang.Double#MAX_VALUE}.
         */
        double max() default java.lang.Double.MAX_VALUE;
    }

    /**
     * Sets the option's default value.
     *
     * @author DaPorkchop_
     */
    public @interface Enum {
        /**
         * The option's default value.
         */
        String value();

        /**
         * The GUI input type used for this option.
         */
        EnumInput input() default EnumInput.DROPDOWN;
    }

    /**
     * Sets the option's default value.
     *
     * @author DaPorkchop_
     */
    public @interface Text {
        /**
         * The option's default value.
         */
        String value();

        /**
         * The GUI input type used for this option.
         */
        TextInput input() default TextInput.INLINE;
    }

    /**
     * Sets the option's default value.
     *
     * @author DaPorkchop_
     */
    public @interface TextLines {
        /**
         * The option's default value.
         */
        String[] value();

        /**
         * The GUI input type used for this option.
         */
        TextLinesInput input() default TextLinesInput.POPUP_MULTI;
    }
}
