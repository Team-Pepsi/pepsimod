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
import org.lwjgl.opengl.GL11;

public class HUDTranslator implements IConfigTranslator {
    public static final HUDTranslator INSTANCE = new HUDTranslator();

    public boolean drawLogo = true;
    public boolean arrayList = true;
    public boolean TPS = false;
    public boolean coords = false;
    public boolean netherCoords = false;
    public boolean arrayListTop = true;
    public boolean serverBrand = false;
    public boolean rainbow = true;
    public int r = 0;
    public int g = 0;
    public int b = 0;
    public boolean direction = true;
    public boolean armor = false;
    public boolean effects = false;
    public boolean fps = true;
    public boolean ping = true;
    public JsonObject json = new JsonObject();

    private HUDTranslator() {

    }

    public void encode(JsonObject json) {
        json.addProperty("drawLogo", drawLogo);
        json.addProperty("arrayList", arrayList);
        json.addProperty("tps", TPS);
        json.addProperty("coords", coords);
        json.addProperty("netherCoords", netherCoords);
        json.addProperty("arrayListTop", arrayListTop);
        json.addProperty("serverBrand", serverBrand);
        json.addProperty("rainbow", rainbow);
        json.addProperty("r", r);
        json.addProperty("g", g);
        json.addProperty("b", b);
        json.addProperty("direction", direction);
        json.addProperty("armor", armor);
        json.addProperty("effects", effects);
        json.addProperty("fps", fps);
        json.addProperty("ping", ping);
    }

    public void decode(String fieldName, JsonObject json) {
        this.json = json;
    }

    public void parseConfigLate() {
        drawLogo = getBoolean(json, "drawLogo", drawLogo);
        arrayList = getBoolean(json, "arrayList", arrayList);
        TPS = getBoolean(json, "tps", TPS);
        coords = getBoolean(json, "coords", coords);
        netherCoords = getBoolean(json, "netherCoords", netherCoords);
        arrayListTop = getBoolean(json, "arrayListTop", arrayListTop);
        serverBrand = getBoolean(json, "serverBrand", serverBrand);
        rainbow = getBoolean(json, "rainbow", rainbow);
        r = getInt(json, "r", r);
        g = getInt(json, "g", g);
        b = getInt(json, "b", b);
        direction = getBoolean(json, "direction", direction);
        armor = getBoolean(json, "armor", armor);
        effects = getBoolean(json, "effects", effects);
        fps = getBoolean(json, "fps", fps);
        ping = getBoolean(json, "ping", ping);
    }

    public String name() {
        return "hud";
    }

    public void bindColor() {
        byte r = (byte) Math.floorDiv(this.r, 2);
        byte g = (byte) Math.floorDiv(this.g, 2);
        byte b = (byte) Math.floorDiv(this.b, 2);
        GL11.glColor3b(r, g, b);
    }

    public int getColor() {
        return (255 & 255) << 24 | (r & 255) << 16 | (g & 255) << 8 | (b & 255) << 0;
    }
}
