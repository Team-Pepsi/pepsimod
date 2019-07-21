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

import lombok.NonNull;
import net.daporkchop.lib.unsafe.PCleaner;
import net.daporkchop.pepsimod.util.PepsiConstants;
import net.daporkchop.pepsimod.util.render.opengl.OpenGL;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Basic wrapper around a shader.
 *
 * @author DaPorkchop_
 */
public final class Shader implements PepsiConstants, AutoCloseable {
    protected static String getResource(@NonNull String name) {
        try (InputStream in = Shader.class.getResourceAsStream(String.format("/assets/pepsimod/shaders/%s", name))) {
            if (in == null) {
                throw new IOException();
            }
            /*byte[] b = IOUtils.toByteArray(in);
            ByteBuffer buffer = BufferUtils.createByteBuffer(b.length);
            buffer.put(b).flip();
            return buffer;*/
            return IOUtils.toString(in, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(String.format("Unable to find shader named \"%s\"!", name));
        }
    }

    protected static int compileShader(@NonNull String name, int type) {
        if (type != OpenGL.GL_FRAGMENT_SHADER && type != OpenGL.GL_VERTEX_SHADER) {
            throw new IllegalArgumentException(String.format("Illegal shader type: %d", type));
        }
        int id = OpenGL.glCreateShader(type);
        try {
            OpenGL.glShaderSource(id, getResource(name));
            OpenGL.glCompileShader(id);

            if (OpenGL.glGetShaderi(id, OpenGL.GL_COMPILE_STATUS) == OpenGL.GL_FALSE) {
                String error = String.format("Couldn't compile shader \"%s\": %s", name, OpenGL.glGetShaderInfoLog(id, 32768).trim());
                System.err.println(error);
                throw new IllegalStateException(error);
            }
        } catch (Exception e) {
            e.printStackTrace();
            OpenGL.glDeleteShader(id);
            throw new RuntimeException(e);
        }
        return id;
    }

    protected final PCleaner cleaner;
    protected final AtomicBoolean active = new AtomicBoolean(false);
    protected final int vertexId;
    protected final int fragmentId;
    protected final int programId;

    /**
     * Loads a new shader with the given file name and type.
     *
     * @param vertexName   the resource name of the vertex shader
     * @param fragmentName the resource name of the fragment shader
     */
    public Shader(@NonNull String vertexName, @NonNull String fragmentName) {
        int vertexId = 0;
        int fragmentId = 0;
        int programId = 0;
        try {
            vertexId = this.vertexId = compileShader(vertexName, OpenGL.GL_VERTEX_SHADER);
            fragmentId = this.fragmentId = compileShader(fragmentName, OpenGL.GL_FRAGMENT_SHADER);

            programId = this.programId = OpenGL.glCreateProgram();
            OpenGL.glAttachShader(programId, vertexId);
            OpenGL.glAttachShader(programId, fragmentId);
            OpenGL.glLinkProgram(programId);

            if (OpenGL.glGetShaderi(programId, OpenGL.GL_LINK_STATUS) == OpenGL.GL_FALSE) {
                String error = String.format("Couldn't link shader \"%s\"+\"%s\": %s", vertexName, fragmentName, OpenGL.glGetShaderInfoLog(programId, 32768).trim());
                System.err.println(error);
                throw new IllegalStateException(error);
            }
        } catch (RuntimeException e)   {
            e.printStackTrace();
            if (vertexId != 0)  {
                OpenGL.glDeleteShader(vertexId);
            }
            if (fragmentId != 0)  {
                OpenGL.glDeleteShader(fragmentId);
            }
            if (programId != 0)  {
                OpenGL.glDeleteProgram(programId);
            }
            throw e;
        }

        int the_vertexId = vertexId;
        int the_fragmentId = fragmentId;
        int the_programId = programId;
        this.cleaner = PCleaner.cleaner(this, () -> mc.addScheduledTask(() -> {
            OpenGL.glDeleteShader(the_vertexId);
            OpenGL.glDeleteShader(the_fragmentId);
            OpenGL.glDeleteProgram(the_programId);
        }));
    }

    /**
     * Gets the location of a uniform value in the vertex shader.
     *
     * @param name the uniform's name
     * @return the uniform's location
     */
    public int getVertexUniformLocation(@NonNull String name) {
        return OpenGL.glGetUniformLocation(this.vertexId, name);
    }

    /**
     * Gets the location of a uniform value in the fragment shader.
     *
     * @param name the uniform's name
     * @return the uniform's location
     */
    public int getFragmentUniformLocation(@NonNull String name) {
        return OpenGL.glGetUniformLocation(this.fragmentId, name);
    }

    /**
     * Binds this shader for use when rendering.
     * <p>
     * This method returns itself, for use in a try-with-resources block.
     */
    public synchronized Shader use() {
        if (this.active.getAndSet(true)) {
            throw new IllegalStateException("Shader already active!");
        } else {
            try {
                OpenGL.glUseProgram(this.programId);
                return this;
            } catch (RuntimeException e) {
                //not in a gl context
                this.active.set(false);
                e.printStackTrace();
                throw e;
            }
        }
    }

    @Override
    public synchronized void close() {
        if (!this.active.getAndSet(false)) {
            throw new IllegalStateException("Shader not currently active!");
        } else {
            try {
                OpenGL.glUseProgram(0);
            } catch (RuntimeException e) {
                //not in a gl context
                this.active.set(true);
                e.printStackTrace();
                throw e;
            }
        }
    }

    /**
     * Releases this shader.
     */
    public void dispose() {
        this.cleaner.clean();
    }
}
