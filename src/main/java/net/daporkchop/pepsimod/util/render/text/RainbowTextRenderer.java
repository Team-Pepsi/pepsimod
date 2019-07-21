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

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import net.daporkchop.pepsimod.util.PepsiConstants;
import net.daporkchop.pepsimod.util.render.opengl.OpenGL;
import net.daporkchop.pepsimod.util.render.shader.ShaderProgram;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

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
@Accessors(fluent = true)
public final class RainbowTextRenderer implements TextRenderer, PepsiConstants {
    protected static final double BASE_SPEED        = 159.15494309d;
    protected static final long   RESTART_THRESHOLD = 16777215L;
    protected static final long   START_TIME        = System.currentTimeMillis();

    protected final ShaderProgram shader;

    protected final int speedLocation;
    protected final int scaleLocation;
    protected final int rotationLocation;
    protected final int timeLocation;

    @Getter
    protected float speed;
    @Getter
    protected float scale;
    protected float rotationX;
    protected float rotationY;
    protected float time;

    @Getter
    protected float rotation;
    protected boolean changed = true;

    public RainbowTextRenderer(float speed, float scale, float rotation) {
        this.shader = new ShaderProgram("vert/dummy.vert", "frag/rainbow.frag");
        OpenGL.checkGLError("Constructor");

        this.speedLocation = this.shader.uniformLocation("speed");
        OpenGL.checkGLError("speed");
        this.scaleLocation = this.shader.uniformLocation("scale");
        OpenGL.checkGLError("scale");
        this.rotationLocation = this.shader.uniformLocation("rotation");
        OpenGL.checkGLError("rotation");
        this.timeLocation = this.shader.uniformLocation("time");
        OpenGL.checkGLError("time");
        
        //TODO: a better system for uniforms

        this.speed = speed;
        this.scale = scale;
        this.rotation = rotation;
    }

    /**
     * Sets the speed of the rainbow effect.
     *
     * @param speed the speed of the effect
     * @return this instance
     */
    public RainbowTextRenderer speed(float speed) {
        synchronized (this.shader) {
            this.changed = true;

            this.speed = speed;
        }
        return this;
    }

    /**
     * Sets the scale of the rainbow effect.
     *
     * @param scale the scale of the effect
     * @return this instance
     */
    public RainbowTextRenderer scale(float scale) {
        synchronized (this.shader) {
            this.changed = true;

            this.scale = scale;
        }
        return this;
    }

    /**
     * Sets the rotation of the rainbow effect.
     *
     * @param rotation the rotation of the effect
     * @return this instance
     */
    public RainbowTextRenderer rotation(float rotation) {
        synchronized (this.shader) {
            this.changed = true;

            this.rotationX = (float) sin(Math.toRadians(rotation) + PI);
            this.rotationY = (float) cos(Math.toRadians(rotation) + PI);
            this.rotation = rotation;
        }
        return this;
    }

    /**
     * Updates the rainbow cycle according to the current system time.
     * <p>
     * This should be called once per frame to update the rainbow pattern's step.
     */
    @Override
    public void update() {
        synchronized (this.shader) {
            try (ShaderProgram shader = this.prepare()) {
                if (this.changed) {
                    this.changed = false;

                    /*OpenGL.glUniform1f(this.speedLocation, this.speed);
                    OpenGL.glUniform1f(this.scaleLocation, this.scale);
                    OpenGL.glUniform2f(this.rotationLocation, this.rotationX, this.rotationY);*/
                }

                //OpenGL.glUniform1f(this.timeLocation, ThreadLocalRandom.current().nextFloat());
                this.time = (float) ((System.currentTimeMillis() - START_TIME) / BASE_SPEED * this.speed);
            }
        }
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
        OpenGL.glUniform1f(shader.uniformLocation("speed"), this.speed);
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
        this.shader.dispose();
    }
}
