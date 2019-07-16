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

package net.daporkchop.pepsimod.util.render.text;

import lombok.NonNull;
import net.daporkchop.pepsimod.util.capability.Updateable;

/**
 * Exposes the ability to render text on the screen in 2d space.
 *
 * @author DaPorkchop_
 */
public interface TextRenderer<I extends TextRenderer<I>> extends Updateable {
    /**
     * @see #renderText(CharSequence, float, float, int, int)
     */
    default I renderText(int x, int y, @NonNull CharSequence text) {
        return this.renderText(text, (float) x, (float) y, 0, text.length());
    }

    /**
     * @see #renderText(CharSequence, float, float, int, int)
     */
    default I renderText(int x, int y, int startIndex, int length, @NonNull CharSequence text) {
        return this.renderText(text, (float) x, (float) y, startIndex, length);
    }

    /**
     * @see #renderText(CharSequence, float, float, int, int)
     */
    default I renderText(float x, float y, @NonNull CharSequence text) {
        return this.renderText(text, x, y, 0, text.length());
    }

    /**
     * @see #renderText(CharSequence, float, float, int, int)
     */
    default I renderText(float x, float y, int startIndex, int length, @NonNull CharSequence text) {
        return this.renderText(text, x, y, startIndex, length);
    }

    /**
     * @see #renderText(CharSequence, float, float, int, int)
     */
    default I renderText(@NonNull CharSequence text, int x, int y) {
        return this.renderText(text, (float) x, (float) y, 0, text.length());
    }

    /**
     * @see #renderText(CharSequence, float, float, int, int)
     */
    default I renderText(@NonNull CharSequence text, int x, int y, int startIndex, int length) {
        return this.renderText(text, (float) x, (float) y, startIndex, length);
    }

    /**
     * @see #renderText(CharSequence, float, float, int, int)
     */
    default I renderText(@NonNull CharSequence text, float x, float y) {
        return this.renderText(text, x, y, 0, text.length());
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
    I renderText(@NonNull CharSequence text, float x, float y, int startIndex, int length) throws IndexOutOfBoundsException;

    @Override
    void update();
}
