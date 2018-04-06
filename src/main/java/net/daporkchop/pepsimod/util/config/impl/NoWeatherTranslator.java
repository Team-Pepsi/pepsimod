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

package net.daporkchop.pepsimod.util.config.impl;

import com.google.gson.JsonObject;
import net.daporkchop.pepsimod.util.config.IConfigTranslator;

public class NoWeatherTranslator implements IConfigTranslator {
    public static final NoWeatherTranslator INSTANCE = new NoWeatherTranslator();
    public boolean disableRain = false;
    public boolean changeTime = false;
    public int time = 0;

    private NoWeatherTranslator() {

    }

    public void encode(JsonObject json) {
        json.addProperty("disableRain", disableRain);
        json.addProperty("changeTime", changeTime);
        json.addProperty("time", time);
    }

    public void decode(String fieldName, JsonObject json) {
        disableRain = getBoolean(json, "disableRain", disableRain);
        changeTime = getBoolean(json, "changeTime", changeTime);
        time = getInt(json, "time", time);
    }

    public String name() {
        return "noWeather";
    }
}
