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

package net.daporkchop.pepsimod.util.render.shader;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import net.daporkchop.pepsimod.util.PepsiConstants;
import net.daporkchop.pepsimod.util.render.OpenGL;

/**
 * Basic wrapper around a shader.
 *
 * @author DaPorkchop_
 */
public final class ShaderProgram implements PepsiConstants, AutoCloseable {
    @Getter
    protected final String name;
    protected final Shader vertex;
    protected final Shader fragment;
    protected       int    id;
    protected       int    usages;

    /**
     * Creates a new shader program by attaching the given vertex shader with the given fragment shader.
     *
     * @param name     the program's name
     * @param vertex   the vertex shader
     * @param fragment fragment shader
     */
    protected ShaderProgram(@NonNull String name, @NonNull Shader vertex, @NonNull Shader fragment) {
        OpenGL.assertOpenGL();
        this.name = name;
        this.id = -1;

        try {
            //allocate program
            this.id = OpenGL.glCreateProgram();

            //attach shaders
            vertex.attach(this);
            fragment.attach(this);

            //link and validate
            OpenGL.glLinkProgram(this.id);
            ShaderManager.validate(this.name, this.id, OpenGL.GL_LINK_STATUS);
            OpenGL.glValidateProgram(this.id);
            ShaderManager.validate(this.name, this.id, OpenGL.GL_VALIDATE_STATUS);
        } catch (Exception e) {
            vertex.detachSoft(this);
            fragment.detachSoft(this);
            this.release();
        }

        this.vertex = vertex;
        this.fragment = fragment;
    }

    protected ShaderProgram incrementUsages() {
        OpenGL.assertOpenGL();
        if (this.id == -1) {
            throw new IllegalStateException("Already deleted!");
        } else {
            this.usages++;
            return this;
        }
    }

    /**
     * Decrements the shader's usage count by 1, deleting it if the usage count reaches 0.
     * <p>
     * This must only be called when you are no longer using the shader.
     */
    public void release() {
        OpenGL.assertOpenGL();
        if (this.id == -1) {
            throw new IllegalStateException("Already deleted!");
        } else {
            if (this.vertex != null) {
                this.vertex.detach(this);
            }
            if (this.fragment != null) {
                this.fragment.detach(this);
            }
            OpenGL.glDeleteProgram(this.id);
            this.id = -1;
            ShaderManager.LINKED_PROGRAMS.remove(this.name, this);
            /*if (!ShaderManager.LINKED_PROGRAMS.remove(this.name, this)) {
                throw new IllegalStateException("Couldn't remove self from linked programs registry!");
            }*/
        }
    }

    /**
     * Gets the location of a uniform value in the fragment shader.
     *
     * @param name the uniform's name
     * @return the uniform's location
     */
    public int uniformLocation(@NonNull String name) {
        if (this.id == -1) {
            throw new IllegalStateException("Already deleted!");
        } else {
            return OpenGL.glGetUniformLocation(this.id, name);
        }
    }

    /**
     * Binds this shader for use when rendering.
     * <p>
     * This method returns itself, for use in a try-with-resources block.
     */
    public ShaderProgram use() {
        if (this.id == -1) {
            throw new IllegalStateException("Already deleted!");
        } else {
            OpenGL.glUseProgram(this.id);
            return this;
        }
    }

    @Override
    public synchronized void close() {
        OpenGL.glUseProgram(0);
    }
}
