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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import net.daporkchop.pepsimod.util.PepsiConstants;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.Color;

import static java.lang.Math.*;
import static net.minecraft.util.math.MathHelper.clamp;

/**
 * A {@link TextRenderer} that renders text using a fixed color.
 *
 * @author DaPorkchop_
 */
@RequiredArgsConstructor
@Getter
public final class FixedColorTextRenderer implements TextRenderer, PepsiConstants {
    protected final float r;
    protected final float g;
    protected final float b;
    protected final float a;

    public FixedColorTextRenderer(@NonNull Color color)  {
        this(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public FixedColorTextRenderer(int argb) {
        this((argb >>> 16) & 0xFF, (argb >>> 8) & 0xFF, argb & 0xFF, (argb >>> 24) & 0xFF);
    }

    public FixedColorTextRenderer(int r, int g, int b) {
        this(r / 255.0f, g / 255.0f, b / 255.0f, 1.0f);
    }

    public FixedColorTextRenderer(int r, int g, int b, int a) {
        this(r / 255.0f, g / 255.0f, b / 255.0f, a / 255.0f);
    }

    public FixedColorTextRenderer(float r, float g, float b) {
        this(r, g, b, 1.0f);
    }

    @Override
    public void update() {
    }

    /**
     * Sets the gl render color to this renderer's color.
     */
    public void setColor()  {
        GlStateManager.color(this.r, this.g, this.b, this.a);
    }

    @Override
    public FixedColorTextRenderer render(@NonNull CharSequence text, float x, float y, int startIndex, int length) throws IndexOutOfBoundsException {
        if (startIndex < 0 || length < 0 || startIndex + length > text.length()) {
            throw new IndexOutOfBoundsException();
        }
        this.setColor();
        FontRenderer renderer = mc.fontRenderer;
        renderer.posX = x;
        renderer.posY = y;

        length += startIndex;
        for (; startIndex < length; startIndex++) {
            renderer.posX += renderer.renderChar(text.charAt(startIndex), false);
        }

        return this;
    }

    @Override
    public FixedColorTextRenderer renderPieces(@NonNull CharSequence[] textSegments, float x, float y) {
        this.setColor();
        FontRenderer renderer = mc.fontRenderer;
        renderer.posX = x;
        renderer.posY = y;

        for (CharSequence text : textSegments)  {
            int length = text.length();
            for (int i = 0; i < length; i++)    {
                renderer.posX += renderer.renderChar(text.charAt(i), false);
            }
        }

        return this;
    }

    @Override
    public FixedColorTextRenderer renderLines(@NonNull CharSequence[] lines, float x, float y) {
        this.setColor();
        FontRenderer renderer = mc.fontRenderer;
        renderer.posX = x;
        renderer.posY = y;

        for (CharSequence text : lines)  {
            int length = text.length();
            for (int i = 0; i < length; i++)    {
                renderer.posX += renderer.renderChar(text.charAt(i), false);
            }
            renderer.posX = x;
            renderer.posY += 10.0f;
        }

        return this;
    }

    @Override
    public FixedColorTextRenderer renderLinesSmart(@NonNull CharSequence[] lines, float x, float y) {
        this.setColor();
        FontRenderer renderer = mc.fontRenderer;
        renderer.posX = x;
        renderer.posY = y;

        for (CharSequence text : lines)  {
            if (text != null) {
                int length = text.length();
                for (int i = 0; i < length; i++) {
                    renderer.posX += renderer.renderChar(text.charAt(i), false);
                }
            } else {
                renderer.posX = x;
                renderer.posY += 10.0f;
            }
        }

        return this;
    }
}
