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

package net.daporkchop.pepsimod.util.render.color;

import net.minecraft.client.renderer.GlStateManager;

/**
 * A container around a single color.
 *
 * @author DaPorkchop_
 */
public interface RenderColor {
    /**
     * Binds this color to the renderer, using {@link net.minecraft.client.renderer.GlStateManager}.
     */
    default void bind() {
        GlStateManager.color(this.fR(), this.fG(), this.fB(), this.fA());
    }

    /**
     * @return this color as a 32-bit ARGB int
     */
    int argb();

    /**
     * @return this color's alpha channel as an int from 0-255
     */
    int iA();

    /**
     * @return this color's red channel as an int from 0-255
     */
    int iR();

    /**
     * @return this color's green channel as an int from 0-255
     */
    int iG();

    /**
     * @return this color's blue channel as an int from 0-255
     */
    int iB();

    /**
     * @return this color's alpha channel as a float from 0-1
     */
    float fA();

    /**
     * @return this color's red channel as a float from 0-1
     */
    float fR();

    /**
     * @return this color's green channel as a float from 0-1
     */
    float fG();

    /**
     * @return this color's blue channel as a float from 0-1
     */
    float fB();
}
