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
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

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

    public int GL_FALSE           = GL11.GL_FALSE;
    public int GL_NO_ERROR        = GL11.GL_NO_ERROR;
    public int GL_COMPILE_STATUS  = GL20.GL_COMPILE_STATUS;
    public int GL_LINK_STATUS     = GL20.GL_LINK_STATUS;
    public int GL_VALIDATE_STATUS = GL20.GL_VALIDATE_STATUS;
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

        if (VERSION < 21) {
            throw new IllegalStateException("Requires at least OpenGL 2.1, but found " + VERSION);
        }
    }

    public boolean checkOpenGL() {
        try {
            GLContext.getCapabilities();
        } catch (RuntimeException e) {
            if (e.getMessage() == "No OpenGL context found in the current thread.") {
                return false;
            } else {
                throw e;
            }
        }
        return true;
    }

    public void assertOpenGL() {
        GLContext.getCapabilities();
    }

    public int glCreateShader(int type) {
        return ARBShaderObjects.glCreateShaderObjectARB(type);
    }

    public void glDeleteShader(int shader) {
        ARBShaderObjects.glDeleteObjectARB(shader);
    }

    public void glShaderSource(int type, @NonNull ByteBuffer buffer) {
        ARBShaderObjects.glShaderSourceARB(type, buffer);
    }

    public void glShaderSource(int type, @NonNull CharSequence text) {
        ARBShaderObjects.glShaderSourceARB(type, text);
    }

    public void glCompileShader(int id) {
        ARBShaderObjects.glCompileShaderARB(id);
    }

    public int glGetShaderi(int shader, int pname) {
        return ARBShaderObjects.glGetObjectParameteriARB(shader, pname);
    }

    public String glGetShaderInfoLog(int shader, int maxLength) {
        return ARBShaderObjects.glGetInfoLogARB(shader, maxLength);
    }

    public String glGetProgramInfoLog(int program, int maxLength) {
        return ARBShaderObjects.glGetInfoLogARB(program, maxLength);
    }

    public int glCreateProgram() {
        return ARBShaderObjects.glCreateProgramObjectARB();
    }

    public void glDeleteProgram(int program) {
        ARBShaderObjects.glDeleteObjectARB(program);
    }

    public void glUseProgram(int program) {
        ARBShaderObjects.glUseProgramObjectARB(program);
    }

    public void glAttachShader(int program, int shader) {
        ARBShaderObjects.glAttachObjectARB(program, shader);
    }

    public void glLinkProgram(int program) {
        ARBShaderObjects.glLinkProgramARB(program);
    }

    public void glValidateProgram(int program) {
        ARBShaderObjects.glValidateProgramARB(program);
    }

    public String glGetLogInfo(int program) {
        return ARBShaderObjects.glGetInfoLogARB(program, ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }

    public int glGetUniformLocation(int program, @NonNull CharSequence name) {
        return ARBShaderObjects.glGetUniformLocationARB(program, name);
    }

    public void glUniform1i(int location, int v0) {
        ARBShaderObjects.glUniform1iARB(location, v0);
    }

    public void glUniform2i(int location, int v0, int v1) {
        ARBShaderObjects.glUniform2iARB(location, v0, v1);
    }

    public void glUniform3i(int location, int v0, int v1, int v2) {
        ARBShaderObjects.glUniform3iARB(location, v0, v1, v2);
    }

    public void glUniform4i(int location, int v0, int v1, int v2, int v3) {
        ARBShaderObjects.glUniform4iARB(location, v0, v1, v2, v3);
    }

    public void glUniform1f(int location, float v0) {
        ARBShaderObjects.glUniform1fARB(location, v0);
    }

    public void glUniform2f(int location, float v0, float v1) {
        ARBShaderObjects.glUniform2fARB(location, v0, v1);
    }

    public void glUniform3f(int location, float v0, float v1, float v2) {
        ARBShaderObjects.glUniform3fARB(location, v0, v1, v2);
    }

    public void glUniform4f(int location, float v0, float v1, float v2, float v3) {
        ARBShaderObjects.glUniform4fARB(location, v0, v1, v2, v3);
    }

    public void checkGLError() {
        checkGLError("unknown");
    }

    public void checkGLError(@NonNull String msg) {
        int i = 0;
        int error;
        while ((error = GL11.glGetError()) != GL_NO_ERROR) {
            if (++i == 1) {
                System.err.printf("########## GL ERROR ##########\n@ %s\n%d: %s\n", msg, error, GLU.gluErrorString(error));
            } else {
                System.err.printf("########## GL ERROR ##########\n@ %s (x%d)\n%d: %s\n", msg, i, error, GLU.gluErrorString(error));
            }
        }
        System.err.flush();
    }
}
