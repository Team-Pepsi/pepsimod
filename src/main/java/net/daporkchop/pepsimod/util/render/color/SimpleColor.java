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

import lombok.Getter;
import lombok.experimental.Accessors;
import net.minecraft.client.renderer.GlStateManager;

import static net.minecraft.util.math.MathHelper.floor;

/**
 * A simple implementation of {@link RenderColor} which stores both float and int values, allowing for fast access of both.
 *
 * @author DaPorkchop_
 */
@Getter
public final class SimpleColor implements RenderColor {
    protected final float r;
    protected final float g;
    protected final float b;
    protected final float a;
    protected final int   argb;

    /**
     * Constructs a {@link SimpleColor} instance from a 32-bit ARGB int.
     *
     * @param argb the ARGB value
     */
    public SimpleColor(int argb) {
        this((argb >>> 24) & 0xFF, (argb >>> 16) & 0xFF, (argb >>> 8) & 0xFF, argb & 0xFF);
    }

    /**
     * Constructs an opaque {@link SimpleColor} instance from 3 8-bit integer color values.
     *
     * @param r the red channel
     * @param g the green channel
     * @param b the blue channel
     * @throws IllegalArgumentException if any of the color channels are not in the range 0-255
     */
    public SimpleColor(int r, int g, int b) {
        this(0xFF, r, g, b);
    }

    /**
     * Constructs a {@link SimpleColor} instance from 4 8-bit integer color values.
     *
     * @param a the alpha channel
     * @param r the red channel
     * @param g the green channel
     * @param b the blue channel
     * @throws IllegalArgumentException if any of the color channels are not in the range 0-255
     */
    public SimpleColor(int a, int r, int g, int b) {
        if ((a & ~0xFF) != 0) {
            throw new IllegalArgumentException(String.format("Invalid alpha value. Must be in range 0-255 (found: %d)", a));
        } else if ((r & ~0xFF) != 0) {
            throw new IllegalArgumentException(String.format("Invalid red value. Must be in range 0-255 (found: %d)", r));
        } else if ((g & ~0xFF) != 0) {
            throw new IllegalArgumentException(String.format("Invalid green value. Must be in range 0-255 (found: %d)", g));
        } else if ((b & ~0xFF) != 0) {
            throw new IllegalArgumentException(String.format("Invalid blue value. Must be in range 0-255 (found: %d)", b));
        }
        this.r = r / 255.0f;
        this.g = g / 255.0f;
        this.b = b / 255.0f;
        this.a = a / 255.0f;
        this.argb = (a << 24) | (r << 16) | (g << 8) | b;
    }

    /**
     * Constructs an opaque {@link SimpleColor} instance from 3 float color values.
     *
     * @param r the red channel
     * @param g the green channel
     * @param b the blue channel
     * @throws IllegalArgumentException if any of the color channels are not in the range 0-1
     */
    public SimpleColor(float r, float g, float b) {
        this(1.0f, r, g, b);
    }

    /**
     * Constructs a {@link SimpleColor} instance from 4 float color values.
     *
     * @param a the alpha channel
     * @param r the red channel
     * @param g the green channel
     * @param b the blue channel
     * @throws IllegalArgumentException if any of the color channels are not in the range 0-1
     */
    public SimpleColor(float a, float r, float g, float b) {
        if (a < 0.0f || a > 1.0f) {
            throw new IllegalArgumentException(String.format("Invalid alpha value. Must be in range 0-1 (found: %f)", a));
        } else if (r < 0.0f || r > 1.0f) {
            throw new IllegalArgumentException(String.format("Invalid red value. Must be in range 0-1 (found: %f)", r));
        } else if (g < 0.0f || g > 1.0f) {
            throw new IllegalArgumentException(String.format("Invalid green value. Must be in range 0-1 (found: %f)", g));
        } else if (b < 0.0f || b > 1.0f) {
            throw new IllegalArgumentException(String.format("Invalid blue value. Must be in range 0-1 (found: %f)", b));
        }
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.argb = (floor(a * 255.0f) << 24) | (floor(r * 255.0f) << 16) | (floor(g * 255.0f) << 8) | floor(b * 255.0f);
    }

    @Override
    public void bind() {
        GlStateManager.color(this.r, this.g, this.b, this.a);
    }

    @Override
    public int iA() {
        return (this.argb >>> 24) & 0xFF;
    }

    @Override
    public int iR() {
        return (this.argb >>> 16) & 0xFF;
    }

    @Override
    public int iG() {
        return (this.argb >>> 8) & 0xFF;
    }

    @Override
    public int iB() {
        return this.argb & 0xFF;
    }

    @Override
    public float fA() {
        return this.a;
    }

    @Override
    public float fR() {
        return this.r;
    }

    @Override
    public float fG() {
        return this.g;
    }

    @Override
    public float fB() {
        return this.b;
    }
}
