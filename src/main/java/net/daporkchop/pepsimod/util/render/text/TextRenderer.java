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

package net.daporkchop.pepsimod.util.render.text;

import lombok.Getter;
import lombok.NonNull;
import net.daporkchop.pepsimod.util.capability.Updateable;
import net.daporkchop.pepsimod.util.config.GlobalConfig;

/**
 * Exposes the ability to render text on the screen in 2d space.
 *
 * @author DaPorkchop_
 */
public interface TextRenderer extends Updateable<TextRenderer>, AutoCloseable {
    /**
     * @see #render(CharSequence, float, float, int, int)
     */
    default TextRenderer render(int x, int y, @NonNull CharSequence text) {
        return this.render(text, (float) x, (float) y, 0, text.length());
    }

    /**
     * @see #render(CharSequence, float, float, int, int)
     */
    default TextRenderer render(int x, int y, int startIndex, int length, @NonNull CharSequence text) {
        return this.render(text, (float) x, (float) y, startIndex, length);
    }

    /**
     * @see #render(CharSequence, float, float, int, int)
     */
    default TextRenderer render(float x, float y, @NonNull CharSequence text) {
        return this.render(text, x, y, 0, text.length());
    }

    /**
     * @see #render(CharSequence, float, float, int, int)
     */
    default TextRenderer render(float x, float y, int startIndex, int length, @NonNull CharSequence text) {
        return this.render(text, x, y, startIndex, length);
    }

    /**
     * @see #render(CharSequence, float, float, int, int)
     */
    default TextRenderer render(@NonNull CharSequence text, int x, int y) {
        return this.render(text, (float) x, (float) y, 0, text.length());
    }

    /**
     * @see #render(CharSequence, float, float, int, int)
     */
    default TextRenderer render(@NonNull CharSequence text, int x, int y, int startIndex, int length) {
        return this.render(text, (float) x, (float) y, startIndex, length);
    }

    /**
     * @see #render(CharSequence, float, float, int, int)
     */
    default TextRenderer render(@NonNull CharSequence text, float x, float y) {
        return this.render(text, x, y, 0, text.length());
    }

    /**
     * Renders some text at the given coordinates.
     *
     * @param text       the text to render
     * @param x          the X coordinate to render the text at
     * @param y          the Y coordinate to render the text at
     * @param startIndex the first index of the text to render
     * @param length     the number of letters in the text to render
     * @return this {@link RainbowTextRenderer} instance
     * @throws IndexOutOfBoundsException if the startIndex and/or length aren't within the bounds of the given text
     */
    TextRenderer render(@NonNull CharSequence text, float x, float y, int startIndex, int length) throws IndexOutOfBoundsException;

    /**
     * @see #renderPieces(float, float, CharSequence[])
     */
    default TextRenderer renderPieces(float x, float y, @NonNull CharSequence... textSegments) {
        return this.renderPieces(textSegments, x, y);
    }

    /**
     * Renders multiple pieces of text sequentially at the given coordinates.
     * <p>
     * This may be used to avoid redundant string concatenation.
     *
     * @param textSegments the pieces of text to render
     * @param x            the X coordinate to render the text at
     * @param y            the Y coordinate to render the text at
     * @return this {@link RainbowTextRenderer} instance
     */
    TextRenderer renderPieces(@NonNull CharSequence[] textSegments, float x, float y);

    /**
     * @see #renderLines(float, float, CharSequence...)
     */
    default TextRenderer renderLines(float x, float y, @NonNull CharSequence... lines) {
        return this.renderLines(lines, x, y);
    }

    /**
     * Renders multiple lines of text at the given coordinates.
     *
     * @param lines the lines of text to render
     * @param x     the X coordinate to render the text at
     * @param y     the Y coordinate to render the text at
     * @return this {@link RainbowTextRenderer} instance
     */
    TextRenderer renderLines(@NonNull CharSequence[] lines, float x, float y);

    /**
     * @see #renderLinesSmart(float, float, CharSequence...)
     */
    default TextRenderer renderLinesSmart(float x, float y, @NonNull CharSequence... lines) {
        return this.renderLinesSmart(lines, x, y);
    }

    /**
     * Renders multiple lines of text at the given coordinates.
     * <p>
     * This is more powerful than {@link #renderLines(CharSequence[], float, float)} because it will only insert a line break when it finds a {@code null}
     * value instead of text.
     *
     * @param lines the lines of text to render
     * @param x     the X coordinate to render the text at
     * @param y     the Y coordinate to render the text at
     * @return this {@link RainbowTextRenderer} instance
     */
    TextRenderer renderLinesSmart(@NonNull CharSequence[] lines, float x, float y);

    /**
     * Updates this text renderer.
     * <p>
     * Must be called once per frame.
     * <p>
     * Must be called from the render thread.
     */
    @Override
    void update();

    @Override
    default void close() {
    }

    /**
     * The default text renderer types.
     *
     * @author DaPorkchop_
     */
    enum Type {
        NORMAL {
            @Override
            public TextRenderer renderer() {
                return null;
            }

            @Override
            public void update() {

            }
        },
        RAINBOW {
            @Getter
            private final RainbowTextRenderer renderer = new RainbowTextRenderer();

            @Override
            public void update() {
                this.renderer.speed(GlobalConfig.Text.Rainbow.speed)
                        .scale(GlobalConfig.Text.Rainbow.scale)
                        .rotation(GlobalConfig.Text.Rainbow.rotation);
            }
        };

        /**
         * Creates a new {@link TextRenderer} instance using the current settings.
         */
        public abstract TextRenderer renderer();

        /**
         * Updates the text renderer.
         * <p>
         * Should be fired whenever a config value changed, but otherwise may be safely left alone.
         */
        public abstract void update();
    }
}
