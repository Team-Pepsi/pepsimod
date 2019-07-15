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
