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
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Basic wrapper around a shader.
 *
 * @author DaPorkchop_
 */
public final class Shader implements PepsiConstants, AutoCloseable {
    protected static byte[] getResource(@NonNull String name) {
        try (InputStream in = Shader.class.getResourceAsStream(String.format("/assets/pepsimod/shaders/%s", name))) {
            if (in == null) {
                throw new IOException();
            }
            return IOUtils.toByteArray(in);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Unable to find shader named \"%s\"!", name));
        }
    }

    protected static int compileShader(@NonNull String name, @NonNull byte[] bytes, int type) {
        if (type != OpenGlHelper.GL_FRAGMENT_SHADER && type != OpenGlHelper.GL_VERTEX_SHADER) {
            throw new IllegalArgumentException(String.format("Illegal shader type: %d", type));
        }
        int id = OpenGlHelper.glCreateShader(type);
        try {
            ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length);
            buffer.put(bytes).flip();
            OpenGlHelper.glShaderSource(id, buffer);
            OpenGlHelper.glCompileShader(id);

            if (OpenGlHelper.glGetShaderi(id, OpenGlHelper.GL_COMPILE_STATUS) == GL11.GL_NO_ERROR) {
                throw new IllegalStateException(String.format("Couldn't compile shader \"%s\": %s", name, OpenGlHelper.glGetShaderInfoLog(id, 32768).trim()));
            }
        } catch (Exception e) {
            OpenGlHelper.glDeleteShader(id);
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
            vertexId = this.vertexId = compileShader(vertexName, getResource(vertexName), OpenGlHelper.GL_VERTEX_SHADER);
            fragmentId = this.fragmentId = compileShader(fragmentName, getResource(fragmentName), OpenGlHelper.GL_FRAGMENT_SHADER);

            programId = this.programId = OpenGlHelper.glCreateProgram();
            OpenGlHelper.glAttachShader(programId, vertexId);
            OpenGlHelper.glAttachShader(programId, fragmentId);
            OpenGlHelper.glLinkProgram(programId);

            if (OpenGlHelper.glGetShaderi(programId, OpenGlHelper.GL_LINK_STATUS) == GL11.GL_NO_ERROR) {
                throw new IllegalStateException(String.format("Couldn't link shader \"%s\"+\"%s\": %s", vertexName, fragmentName, OpenGlHelper.glGetShaderInfoLog(programId, 32768).trim()));
            }
        } catch (RuntimeException e)   {
            if (vertexId != 0)  {
                OpenGlHelper.glDeleteShader(vertexId);
            }
            if (fragmentId != 0)  {
                OpenGlHelper.glDeleteShader(fragmentId);
            }
            if (programId != 0)  {
                OpenGlHelper.glDeleteProgram(programId);
            }
            throw e;
        }

        int the_vertexId = vertexId;
        int the_fragmentId = fragmentId;
        int the_programId = programId;
        this.cleaner = PCleaner.cleaner(this, () -> mc.addScheduledTask(() -> {
            OpenGlHelper.glDeleteShader(the_vertexId);
            OpenGlHelper.glDeleteShader(the_fragmentId);
            OpenGlHelper.glDeleteProgram(the_programId);
        }));
    }

    /**
     * Gets the location of a uniform value in the vertex shader.
     *
     * @param name the uniform's name
     * @return the uniform's location
     */
    public int getVertexUniformLocation(@NonNull String name) {
        return OpenGlHelper.glGetUniformLocation(this.vertexId, name);
    }

    /**
     * Gets the location of a uniform value in the fragment shader.
     *
     * @param name the uniform's name
     * @return the uniform's location
     */
    public int getFragmentUniformLocation(@NonNull String name) {
        return OpenGlHelper.glGetUniformLocation(this.fragmentId, name);
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
                OpenGlHelper.glUseProgram(this.programId);
                return this;
            } catch (RuntimeException e) {
                //not in a gl context
                this.active.set(false);
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
                OpenGlHelper.glUseProgram(0);
            } catch (RuntimeException e) {
                //not in a gl context
                this.active.set(true);
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
