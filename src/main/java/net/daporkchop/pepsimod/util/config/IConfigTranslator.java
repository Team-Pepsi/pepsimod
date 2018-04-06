/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2018 DaPorkchop_
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

package net.daporkchop.pepsimod.util.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public interface IConfigTranslator {
    void encode(JsonObject json);

    void decode(String fieldName, JsonObject json);

    String name();

    default int getInt(JsonObject object, String name, int def) {
        if (object.has(name)) {
            JsonElement element = object.get(name);
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
                return element.getAsJsonPrimitive().getAsNumber().intValue();
            }
        }

        return def;
    }

    default short getShort(JsonObject object, String name, short def) {
        if (object.has(name)) {
            JsonElement element = object.get(name);
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
                return element.getAsJsonPrimitive().getAsNumber().shortValue();
            }
        }

        return def;
    }

    default byte getByte(JsonObject object, String name, byte def) {
        if (object.has(name)) {
            JsonElement element = object.get(name);
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
                return element.getAsJsonPrimitive().getAsNumber().byteValue();
            }
        }

        return def;
    }

    default long getLong(JsonObject object, String name, long def) {
        if (object.has(name)) {
            JsonElement element = object.get(name);
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
                return element.getAsJsonPrimitive().getAsNumber().longValue();
            }
        }

        return def;
    }

    default float getFloat(JsonObject object, String name, float def) {
        if (object.has(name)) {
            JsonElement element = object.get(name);
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
                return element.getAsJsonPrimitive().getAsNumber().floatValue();
            }
        }

        return def;
    }

    default double getDouble(JsonObject object, String name, double def) {
        if (object.has(name)) {
            JsonElement element = object.get(name);
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
                return element.getAsJsonPrimitive().getAsNumber().doubleValue();
            }
        }

        return def;
    }

    default boolean getBoolean(JsonObject object, String name, boolean def) {
        if (object.has(name)) {
            JsonElement element = object.get(name);
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean()) {
                return element.getAsJsonPrimitive().getAsBoolean();
            }
        }

        return def;
    }

    default String getString(JsonObject object, String name, String def) {
        if (object.has(name)) {
            JsonElement element = object.get(name);
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
                return element.getAsJsonPrimitive().getAsString();
            }
        }

        return def;
    }

    default JsonArray getArray(JsonObject object, String name, JsonArray def) {
        if (object.has(name)) {
            JsonElement element = object.get(name);
            if (element.isJsonArray()) {
                return element.getAsJsonArray();
            }
        }

        return def;
    }
}
