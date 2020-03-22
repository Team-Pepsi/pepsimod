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

package net.daporkchop.pepsimod.util.render.texture;

/**
 * A simple container around a texture, that provides methods for drawing the texture at specific locations, etc.
 *
 * @author DaPorkchop_
 */
public interface Texture extends AutoCloseable {
    /**
     * A {@link Texture} that does nothing at all, and can serve as a placeholder instead of {@code null}.
     */
    Texture NOOP_TEXTURE = new Texture() {
        @Override
        public int width() {
            return 0;
        }

        @Override
        public int height() {
            return 0;
        }

        @Override
        public void draw(int x, int y, int width, int height) {
        }

        @Override
        public void close() {
        }
    };

    /**
     * @return this texture's width (in pixels)
     */
    int width();

    /**
     * @return this texture's height (in pixels)
     */
    int height();

    /**
     * Draws this texture in 2d pixel space at the given position with the given width.
     *
     * The height will be scaled according to the width.
     *
     * @param x      the X coordinate (of the left edge)
     * @param y      the Y coordinate (of the top edge)
     * @param width  the width of the image
     */
    default void draw(int x, int y, int width)  {
        this.draw(x, y, width, (int) (this.height() * ((float) width / this.width())));
    }

    /**
     * Draws this texture in 2d pixel space at the given position and scale.
     *
     * @param x     the X coordinate (of the left edge)
     * @param y     the Y coordinate (of the top edge)
     * @param scale the scale factor for the image
     */
    default void draw(int x, int y, float scale) {
        this.draw(x, y, (int) (this.width() * scale), (int) (this.height() * scale));
    }

    /**
     * Draws this texture in 2d pixel space at the given position with the given dimensions.
     *
     * @param x      the X coordinate (of the left edge)
     * @param y      the Y coordinate (of the top edge)
     * @param width  the width of the image
     * @param height the height of the image
     */
    void draw(int x, int y, int width, int height);

    /**
     * Releases this texture, freeing up any VRAM resources used by it.
     * <p>
     * This method will be implicitly invoked when the object is garbage-collected, however this provides a way of forcing it.
     * <p>
     * A texture instance is no longer safe to use after invoking this method, and calling any methods will produce undefined behavior.
     */
    @Override
    void close();
}
