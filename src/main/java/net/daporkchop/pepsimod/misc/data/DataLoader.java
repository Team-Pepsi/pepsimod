/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2019 DaPorkchop_
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

package net.daporkchop.pepsimod.misc.data;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.daporkchop.pepsimod.PepsiModMixinLoader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Function;

/**
 * @author DaPorkchop_
 */
public class DataLoader {
    protected static final String RESOURCES_URL = "https://raw.githubusercontent.com/Team-Pepsi/pepsimod/master/resources/resources.json";

    protected static Function<String, InputStream> READER_FUNCTION = null;

    public static void load()   {
    }

    protected static JsonObject getResourcesRoot()  {
        if (PepsiModMixinLoader.isObfuscatedEnvironment)    {
        }
        return null; //TODO
    }

    protected static JsonObject readJson(InputStream in)    {
        return new JsonParser().parse(new InputStreamReader(in)).getAsJsonObject();
    }
}
