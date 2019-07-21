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

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import net.daporkchop.pepsimod.util.render.opengl.OpenGL;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * A container for a shader.
 *
 * @author DaPorkchop_
 */
@Getter
@Accessors(fluent = true)
public abstract class Shader {
    protected final Set<ShaderProgram> usages = Collections.newSetFromMap(new IdentityHashMap<>());
    protected       int                id     ;

    protected Shader(@NonNull String shaderText, @NonNull JsonObject descriptor)  {

    }

    /**
     * Attaches this shader to the given shader program.
     *
     * @param program the program to which to attach this shader
     */
    protected void attach(@NonNull ShaderProgram program) {
        OpenGL.assertOpenGL();
        if (this.id == -1) {
            throw new IllegalStateException("Already deleted!");
        } else if (!this.usages.add(program)) {
            throw new IllegalStateException("Already attached to the given shader!");
        } else {
            OpenGL.glAttachShader(program.programId, this.id);
        }
    }

    /**
     * Detaches this shader from the given shader program.
     *
     * @param program the program from which to detach this shader
     * @return whether or not this shader has been disposed
     */
    protected boolean detach(@NonNull ShaderProgram program) {
        OpenGL.assertOpenGL();
        if (this.id == -1) {
            throw new IllegalStateException("Already deleted!");
        } else if (!this.usages.remove(program)) {
            throw new IllegalStateException("Not attached to the given shader!");
        } else if (this.usages.isEmpty()) {
            OpenGL.glDeleteShader(this.id);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return this shader's type
     */
    public abstract ShaderType type();
}
