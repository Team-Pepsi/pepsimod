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

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import net.daporkchop.pepsimod.util.PepsiConstants;
import net.daporkchop.pepsimod.util.render.shader.Shader;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static java.lang.Math.*;

/**
 * Used for managing rainbow-color text in a simple manner.
 * <p>
 * Copy-paste the following to https://thebookofshaders.com/edit.php:
 * <p>
 * <p>
 * <p>
 * #ifdef GL_ES
 * precision mediump float;
 * #endif
 * <p>
 * uniform vec2 u_resolution;
 * uniform vec2 u_mouse;
 * uniform float u_time;
 * <p>
 * const float PI = 3.1415926535;
 * const float SCALE = 0.03;
 * const float OFFSET = 0.5;
 * <p>
 * const vec3 BASE = vec3(
 * 0.,
 * PI * 0.66666666666666,
 * PI * 1.33333333333333
 * );
 * <p>
 * const float ROT = 45. / 360. * 2. * PI;
 * <p>
 * void main() {
 * float f = (gl_FragCoord.x* sin(ROT) - gl_FragCoord.y * cos(ROT)) * SCALE;
 * <p>
 * gl_FragColor = vec4(
 * OFFSET + sin(BASE + u_time + f),
 * 1.
 * );
 * }
 *
 * @author DaPorkchop_
 */
@Getter
@Accessors(fluent = true)
public final class RainbowTextRenderer implements TextRenderer, PepsiConstants {
    protected static final double TWO_THIRDS_PI  = PI * 0.66666666666666d;
    protected static final double FOUR_THIRDS_PI = PI * 1.33333333333333d;
    protected static final double TWO_PI         = PI * 2.0d;
    protected static final double BASE_SPEED     = 159.15494309d;

    @Getter(AccessLevel.NONE)
    protected final Shader shader;
    @Getter(AccessLevel.NONE)
    protected final FloatBuffer settings = BufferUtils.createFloatBuffer(4);

    @Getter(AccessLevel.NONE)
    protected final int settingsLocation;
    @Getter(AccessLevel.NONE)
    protected final int timeLocation;

    protected float speed;
    protected float scale;
    protected float rotation;

    public RainbowTextRenderer(float speed, float scale, float rotation) {
        this.shader = new Shader("dummy.vsh", "rainbow.fsh");
        this.settingsLocation = this.shader.getFragmentUniformLocation("settings");
        this.timeLocation = this.shader.getFragmentUniformLocation("time");

        this.setup(speed, scale, rotation);
    }

    /**
     * Sets all three options that may be configured with the rainbow text renderer.
     *
     * @param speed    the speed of the effect
     * @param scale    the scale of the effect
     * @param rotation the rotation of the effect
     * @return this instance
     */
    public RainbowTextRenderer setup(float speed, float scale, float rotation) {
        synchronized (this.settings) {
            this.speed = speed;
            this.scale = scale;
            this.rotation = rotation;
            mc.addScheduledTask(() -> {
                this.settings.clear();
                this.settings
                        .put(speed)
                        .put(scale)
                        .put((float) sin(Math.toRadians(rotation) + PI))
                        .put((float) cos(Math.toRadians(rotation) + PI))
                        .flip();
                try (Shader shader = this.shader.use()) {
                    OpenGlHelper.glUniform1(this.settingsLocation, this.settings);
                }
            });
            return this;
        }
    }

    /**
     * Updates the rainbow cycle according to the current system time.
     * <p>
     * This should be called once per frame to update the rainbow pattern's step.
     */
    @Override
    public void update() {
        try (Shader shader = this.shader.use()) {
            //sure, this'll wrap around, but i don't really care too much. it's only every 2 billion milliseconds :P
            OpenGlHelper.glUniform1i(this.timeLocation, (int) (System.currentTimeMillis() & Integer.MAX_VALUE));
        }
    }

    /**
     * Updates the render color based on the given coordinates.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     */
    public void setColor(double x, double y) {
        int i = 0;
        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.5f);
        /*double d = -(x * this.rotationX + y * this.rotationY) * this.scale;

        GlStateManager.color(
                (float) clamp(0.5d + sin(this.time + d), 0.0d, 1.0d),
                (float) clamp(0.5d + sin(this.time + d + TWO_THIRDS_PI), 0.0d, 1.0d),
                (float) clamp(0.5d + sin(this.time + d + FOUR_THIRDS_PI), 0.0d, 1.0d)
        );*/
    }

    @Override
    public RainbowTextRenderer render(@NonNull CharSequence text, float x, float y, int startIndex, int length) throws IndexOutOfBoundsException {
        if (startIndex < 0 || length < 0 || startIndex + length > text.length()) {
            throw new IndexOutOfBoundsException();
        }

        FontRenderer renderer = mc.fontRenderer;
        renderer.posX = x;
        renderer.posY = y;

        length += startIndex;
        for (; startIndex < length; startIndex++) {
            this.setColor(renderer.posX, renderer.posY);
            renderer.posX += renderer.renderChar(text.charAt(startIndex), false);
        }

        return this;
    }

    @Override
    public RainbowTextRenderer renderPieces(@NonNull CharSequence[] textSegments, float x, float y) {
        FontRenderer renderer = mc.fontRenderer;
        renderer.posX = x;
        renderer.posY = y;

        for (CharSequence text : textSegments) {
            int length = text.length();
            for (int i = 0; i < length; i++) {
                this.setColor(renderer.posX, renderer.posY);
                renderer.posX += renderer.renderChar(text.charAt(i), false);
            }
        }

        return this;
    }

    @Override
    public RainbowTextRenderer renderLines(@NonNull CharSequence[] lines, float x, float y) {
        FontRenderer renderer = mc.fontRenderer;
        renderer.posX = x;
        renderer.posY = y;

        for (CharSequence text : lines) {
            int length = text.length();
            for (int i = 0; i < length; i++) {
                this.setColor(renderer.posX, renderer.posY);
                renderer.posX += renderer.renderChar(text.charAt(i), false);
            }
            renderer.posX = x;
            renderer.posY += 10.0f;
        }

        return this;
    }

    @Override
    public RainbowTextRenderer renderLinesSmart(@NonNull CharSequence[] lines, float x, float y) {
        FontRenderer renderer = mc.fontRenderer;
        renderer.posX = x;
        renderer.posY = y;

        this.setColor(renderer.posX, renderer.posY);
        try (Shader shader = this.shader.use()) {
            for (CharSequence text : lines) {
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
        }

        return this;
    }

    @Override
    public void close() {
        this.shader.dispose();
    }
}
