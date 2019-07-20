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

package net.daporkchop.pepsimod.util.render.text;

import lombok.NonNull;
import net.daporkchop.pepsimod.util.capability.Updateable;

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
}
