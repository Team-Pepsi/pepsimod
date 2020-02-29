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
import lombok.experimental.Accessors;
import net.daporkchop.pepsimod.util.PepsiConstants;
import net.daporkchop.pepsimod.util.render.OpenGL;
import net.daporkchop.pepsimod.util.render.shader.ShaderManager;
import net.daporkchop.pepsimod.util.render.shader.ShaderProgram;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

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
public final class RainbowTextRenderer implements TextRenderer, PepsiConstants {
    protected static final double PI         = 3.1415926535897932384626433832795d;
    protected static final double TWO_PI     = 6.2831853071795864769252867665590d;
    protected static final double BASE_SPEED = 159.15494309d;

    protected ShaderProgram shader;

    //protected final int speedLocation;
    protected final int scaleLocation;
    protected final int rotationLocation;
    protected final int timeLocation;

    @Getter
    protected int   speed;
    @Getter
    protected float scale;
    protected float rotationX;
    protected float rotationY;
    protected float time;

    @Getter
    protected float rotation;
    protected boolean changed = true;

    public RainbowTextRenderer() {
        this(0, 0.0f, 0.0f);
    }

    public RainbowTextRenderer(int speed, float scale, float rotation) {
        this.shader = ShaderManager.get("rainbow");
        OpenGL.checkGLError("Constructor");

        //this.speedLocation = this.shader.uniformLocation("speed");
        //OpenGL.checkGLError("speed");
        this.scaleLocation = this.shader.uniformLocation("scale");
        OpenGL.checkGLError("scale");
        this.rotationLocation = this.shader.uniformLocation("rotation");
        OpenGL.checkGLError("rotation");
        this.timeLocation = this.shader.uniformLocation("time");
        OpenGL.checkGLError("time");

        //TODO: a better system for uniforms

        this.speed(speed)
                .scale(scale)
                .rotation(rotation);
    }

    public void reloadShader() {
        this.shader = ShaderManager.reload(this.shader);
    }

    /**
     * Sets the speed of the rainbow effect.
     *
     * @param speed the speed of the effect
     * @return this instance
     */
    public synchronized RainbowTextRenderer speed(int speed) {
        this.changed = true;

        this.speed = speed;
        return this;
    }

    /**
     * Sets the scale of the rainbow effect.
     *
     * @param scale the scale of the effect
     * @return this instance
     */
    public synchronized RainbowTextRenderer scale(float scale) {
        this.changed = true;

        this.scale = scale;
        return this;
    }

    /**
     * Sets the rotation of the rainbow effect.
     *
     * @param rotation the rotation of the effect
     * @return this instance
     */
    public synchronized RainbowTextRenderer rotation(float rotation) {
        this.changed = true;

        double offsetRadians = Math.toRadians(rotation) + PI;
        this.rotationX = (float) -sin(offsetRadians);
        this.rotationY = (float) cos(offsetRadians);
        this.rotation = rotation;
        return this;
    }

    /**
     * Updates the rainbow cycle according to the current system time.
     * <p>
     * This should be called once per frame to update the rainbow pattern's step.
     */
    @Override
    public synchronized void update() {
        this.time = (float) ((System.currentTimeMillis() % this.speed) * TWO_PI / (double) this.speed);
    }

    /**
     * Prepares the shader for rendering by initializing all uniforms.
     * <p>
     * The shader must not be bound before this method is invoked.
     *
     * @return the shader, to be used in a try-with-resources block
     */
    protected ShaderProgram prepare() {
        ShaderProgram shader = this.shader.use();

        /*OpenGL.glUniform1f(this.speedLocation, this.speed);
        OpenGL.glUniform1f(this.scaleLocation, this.scale);
        OpenGL.glUniform2f(this.rotationLocation, this.rotationX, this.rotationY);
        OpenGL.glUniform1f(this.timeLocation, this.time);*/
        //OpenGL.glUniform1f(shader.uniformLocation("speed"), this.speed);
        OpenGL.glUniform1f(shader.uniformLocation("scale"), this.scale);
        OpenGL.glUniform2f(shader.uniformLocation("rotation"), this.rotationX, this.rotationY);
        OpenGL.glUniform1f(shader.uniformLocation("time"), this.time);
        return shader;
    }

    @Override
    public RainbowTextRenderer render(@NonNull CharSequence text, float x, float y, int startIndex, int length) throws IndexOutOfBoundsException {
        if (startIndex < 0 || length < 0 || startIndex + length > text.length()) {
            throw new IndexOutOfBoundsException();
        }

        FontRenderer renderer = mc.fontRenderer;
        renderer.posX = x;
        renderer.posY = y;

        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.0f);
        try (ShaderProgram shader = this.prepare()) {
            length += startIndex;
            for (; startIndex < length; startIndex++) {
                renderer.posX += renderer.renderChar(text.charAt(startIndex), false);
            }
        }

        return this;
    }

    @Override
    public RainbowTextRenderer renderPieces(@NonNull CharSequence[] textSegments, float x, float y) {
        FontRenderer renderer = mc.fontRenderer;
        renderer.posX = x;
        renderer.posY = y;

        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.0f);
        try (ShaderProgram shader = this.prepare()) {
            for (CharSequence text : textSegments) {
                int length = text.length();
                for (int i = 0; i < length; i++) {
                    renderer.posX += renderer.renderChar(text.charAt(i), false);
                }
            }
        }

        return this;
    }

    @Override
    public RainbowTextRenderer renderLines(@NonNull CharSequence[] lines, float x, float y) {
        FontRenderer renderer = mc.fontRenderer;
        renderer.posX = x;
        renderer.posY = y;

        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.0f);
        try (ShaderProgram shader = this.prepare()) {
            for (CharSequence text : lines) {
                int length = text.length();
                for (int i = 0; i < length; i++) {
                    renderer.posX += renderer.renderChar(text.charAt(i), false);
                }
                renderer.posX = x;
                renderer.posY += 10.0f;
            }
        }

        return this;
    }

    @Override
    public RainbowTextRenderer renderLinesSmart(@NonNull CharSequence[] lines, float x, float y) {
        FontRenderer renderer = mc.fontRenderer;
        renderer.posX = x;
        renderer.posY = y;

        GlStateManager.color(1.0f, 1.0f, 1.0f, 0.0f);
        try (ShaderProgram shader = this.prepare()) {
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
        this.shader.release();
    }
}
