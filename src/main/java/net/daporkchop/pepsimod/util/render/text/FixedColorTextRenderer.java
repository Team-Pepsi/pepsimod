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
@Accessors(fluent = true)
public final class FixedColorTextRenderer implements TextRenderer<FixedColorTextRenderer>, PepsiConstants {
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
    public FixedColorTextRenderer renderText(@NonNull CharSequence text, float x, float y, int startIndex, int length) throws IndexOutOfBoundsException {
        if (startIndex < 0 || length < 0 || startIndex + length > text.length()) {
            throw new IndexOutOfBoundsException();
        }

        FontRenderer renderer = mc.fontRenderer;
        renderer.posX = x;
        renderer.posY = y;

        length += startIndex;
        for (; startIndex < length; startIndex++) {
            renderer.posX += renderer.renderChar(text.charAt(startIndex), false);
        }

        return this;
    }
}
