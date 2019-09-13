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

import com.google.common.base.Joiner;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import org.lwjgl.opengl.GL20;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * The different types of shaders.
 *
 * @author DaPorkchop_
 */
@RequiredArgsConstructor
@Getter
public enum ShaderType {
    VERTEX("vert", GL20.GL_VERTEX_SHADER) {
        @Override
        protected Shader construct(@NonNull String name, @NonNull String code, @NonNull JsonObject meta) {
            return new Shader(name, code, meta) {
                @Getter
                protected final Collection<String> provides = new HashSet<>();

                @Override
                protected ShaderType type() {
                    return VERTEX;
                }

                @Override
                protected void load(@NonNull JsonObject meta) {
                    if (!this.provides.isEmpty()) {
                        throw new IllegalStateException("Already initialized!");
                    } else if (!meta.has("provides") || !meta.get("provides").isJsonArray()) {
                        throw new IllegalArgumentException(String.format("Metadata for vertex shader \"%s\" does not define provided variables!", this.name));
                    }
                    for (JsonElement element : meta.getAsJsonArray("provides")) {
                        this.provides.add(element.getAsString());
                    }
                }
            };
        }
    },
    FRAGMENT("frag", GL20.GL_FRAGMENT_SHADER) {
        @Override
        protected Shader construct(@NonNull String name, @NonNull String code, @NonNull JsonObject meta) {
            return new Shader(name, code, meta) {
                @Getter
                protected final Collection<String> requires = new HashSet<>();

                @Override
                protected ShaderType type() {
                    return FRAGMENT;
                }

                @Override
                protected void load(@NonNull JsonObject meta) {
                    if (!this.requires.isEmpty()) {
                        throw new IllegalStateException("Already initialized!");
                    } else if (!meta.has("requires") || !meta.get("requires").isJsonArray()) {
                        throw new IllegalArgumentException(String.format("Metadata for fragment shader \"%s\" does not define required variables!", this.name));
                    }
                    for (JsonElement element : meta.getAsJsonArray("requires")) {
                        this.requires.add(element.getAsString());
                    }
                }

                @Override
                protected void assertCompatible(@NonNull Shader counterpart) throws IllegalArgumentException {
                    super.assertCompatible(counterpart);
                    if (!counterpart.provides().containsAll(this.requires)) {
                        throw new IllegalArgumentException(String.format(
                                "Missing required parameters! Required: [%s], provided: [%s]",
                                Joiner.on(", ").join(this.requires),
                                Joiner.on(", ").join(counterpart.provides())
                        ));
                    }
                }
            };
        }
    };

    @Getter(AccessLevel.NONE)
    protected final Map<String, Shader> compiledShaders = new HashMap<>();
    @NonNull
    protected final String extension;
    protected final int    openGlId;

    protected abstract Shader construct(@NonNull String name, @NonNull String code, @NonNull JsonObject meta);
}
