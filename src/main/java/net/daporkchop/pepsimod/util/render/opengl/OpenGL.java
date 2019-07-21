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

package net.daporkchop.pepsimod.util.render.opengl;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.nio.ByteBuffer;

/**
 * {@link net.minecraft.client.renderer.OpenGlHelper}, but better.
 *
 * @author DaPorkchop_
 */
@UtilityClass
public class OpenGL {
    public ContextCapabilities CAPABILITIES;

    public int VERSION = -1;

    public int GL_FALSE        = GL11.GL_FALSE;
    public int GL_NO_ERROR        = GL11.GL_NO_ERROR;
    public int GL_LINK_STATUS     = GL20.GL_LINK_STATUS;
    public int GL_COMPILE_STATUS  = GL20.GL_COMPILE_STATUS;
    public int GL_VERTEX_SHADER   = GL20.GL_VERTEX_SHADER;
    public int GL_FRAGMENT_SHADER = GL20.GL_FRAGMENT_SHADER;

    public synchronized void init(@NonNull ContextCapabilities capabilities) {
        if (CAPABILITIES != null) {
            throw new IllegalStateException("Already initialized!");
        }
        CAPABILITIES = capabilities;

        if (capabilities.OpenGL45) {
            VERSION = 45;
        } else if (capabilities.OpenGL44) {
            VERSION = 44;
        } else if (capabilities.OpenGL43) {
            VERSION = 43;
        } else if (capabilities.OpenGL42) {
            VERSION = 42;
        } else if (capabilities.OpenGL41) {
            VERSION = 41;
        } else if (capabilities.OpenGL40) {
            VERSION = 40;
        } else if (capabilities.OpenGL33) {
            VERSION = 33;
        } else if (capabilities.OpenGL32) {
            VERSION = 32;
        } else if (capabilities.OpenGL31) {
            VERSION = 31;
        } else if (capabilities.OpenGL30) {
            VERSION = 30;
        } else if (capabilities.OpenGL21) {
            VERSION = 21;
        } else if (capabilities.OpenGL20) {
            VERSION = 20;
        } else if (capabilities.OpenGL15) {
            VERSION = 15;
        } else if (capabilities.OpenGL14) {
            VERSION = 14;
        } else if (capabilities.OpenGL13) {
            VERSION = 13;
        } else if (capabilities.OpenGL12) {
            VERSION = 12;
        } else if (capabilities.OpenGL11) {
            VERSION = 11;
        }

        if (VERSION < 21)   {
            throw new IllegalStateException("Requires at least OpenGL 2.1, but found " + VERSION);
        }
    }

    public int glCreateShader(int type) {
        return VERSION >= 21 ? ARBShaderObjects.glCreateShaderObjectARB(type) : GL20.glCreateShader(type);
    }

    public void glDeleteShader(int shader) {
        if (VERSION >= 21) {
            ARBShaderObjects.glDeleteObjectARB(shader);
        } else {
            GL20.glDeleteShader(shader);
        }
    }

    public void glShaderSource(int type, @NonNull ByteBuffer buffer) {
        if (VERSION >= 21) {
            ARBShaderObjects.glShaderSourceARB(type, buffer);
        } else {
            GL20.glShaderSource(type, buffer);
        }
    }

    public void glShaderSource(int type, @NonNull CharSequence text) {
        if (VERSION >= 21) {
            ARBShaderObjects.glShaderSourceARB(type, text);
        } else {
            GL20.glShaderSource(type, text);
        }
    }

    public void glCompileShader(int id) {
        if (VERSION >= 21) {
            ARBShaderObjects.glCompileShaderARB(id);
        } else {
            GL20.glCompileShader(id);
        }
    }

    public int glGetShaderi(int shader, int pname) {
        return VERSION >= 21 ? ARBShaderObjects.glGetObjectParameteriARB(shader, pname) : GL20.glGetShaderi(shader, pname);
    }

    public String glGetShaderInfoLog(int shader, int maxLength) {
        return VERSION >= 21 ? ARBShaderObjects.glGetInfoLogARB(shader, maxLength) : GL20.glGetShaderInfoLog(shader, maxLength);
    }

    public String glGetProgramInfoLog(int program, int maxLength) {
        return VERSION >= 21 ? ARBShaderObjects.glGetInfoLogARB(program, maxLength) : GL20.glGetProgramInfoLog(program, maxLength);
    }

    public int glCreateProgram() {
        return VERSION >= 21 ? ARBShaderObjects.glCreateProgramObjectARB() : GL20.glCreateProgram();
    }

    public void glDeleteProgram(int program) {
        if (VERSION >= 21) {
            ARBShaderObjects.glDeleteObjectARB(program);
        } else {
            GL20.glDeleteProgram(program);
        }
    }

    public void glUseProgram(int program) {
        if (VERSION >= 21) {
            ARBShaderObjects.glUseProgramObjectARB(program);
        } else {
            GL20.glUseProgram(program);
        }
    }

    public void glAttachShader(int program, int shader) {
        if (VERSION >= 21) {
            ARBShaderObjects.glAttachObjectARB(program, shader);
        } else {
            GL20.glAttachShader(program, shader);
        }
    }

    public void glLinkProgram(int program) {
        if (VERSION >= 21) {
            ARBShaderObjects.glLinkProgramARB(program);
        } else {
            GL20.glLinkProgram(program);
        }
    }

    public String glGetLogInfo(int program) {
            return ARBShaderObjects.glGetInfoLogARB(program, ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }

    public int glGetUniformLocation(int program, @NonNull CharSequence name) {
        return VERSION >= 21 ? ARBShaderObjects.glGetUniformLocationARB(program, name) : GL20.glGetUniformLocation(program, name);
    }

    public void glUniform1i(int location, int v0) {
        if (VERSION >= 21) {
            ARBShaderObjects.glUniform1iARB(location, v0);
        } else {
            GL20.glUniform1i(location, v0);
        }
    }

    public void glUniform2i(int location, int v0, int v1) {
        if (VERSION >= 21) {
            ARBShaderObjects.glUniform2iARB(location, v0, v1);
        } else {
            GL20.glUniform2i(location, v0, v1);
        }
    }

    public void glUniform3i(int location, int v0, int v1, int v2) {
        if (VERSION >= 21) {
            ARBShaderObjects.glUniform3iARB(location, v0, v1, v2);
        } else {
            GL20.glUniform3i(location, v0, v1, v2);
        }
    }

    public void glUniform4i(int location, int v0, int v1, int v2, int v3) {
        if (VERSION >= 21) {
            ARBShaderObjects.glUniform4iARB(location, v0, v1, v2, v3);
        } else {
            GL20.glUniform4i(location, v0, v1, v2, v3);
        }
    }

    public void glUniform1f(int location, float v0) {
        if (VERSION >= 21) {
            ARBShaderObjects.glUniform1fARB(location, v0);
        } else {
            GL20.glUniform1f(location, v0);
        }
    }

    public void glUniform2f(int location, float v0, float v1) {
        if (VERSION >= 21) {
            ARBShaderObjects.glUniform2fARB(location, v0, v1);
        } else {
            GL20.glUniform2f(location, v0, v1);
        }
    }

    public void glUniform3f(int location, float v0, float v1, float v2) {
        if (VERSION >= 21) {
            ARBShaderObjects.glUniform3fARB(location, v0, v1, v2);
        } else {
            GL20.glUniform3f(location, v0, v1, v2);
        }
    }

    public void glUniform4f(int location, float v0, float v1, float v2, float v3) {
        if (VERSION >= 21) {
            ARBShaderObjects.glUniform4fARB(location, v0, v1, v2, v3);
        } else {
            GL20.glUniform4f(location, v0, v1, v2, v3);
        }
    }
}
