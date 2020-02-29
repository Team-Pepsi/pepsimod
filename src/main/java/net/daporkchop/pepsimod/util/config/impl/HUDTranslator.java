/*
 * Adapted from The MIT License (MIT)
 *
 * Copyright (c) 2016-2020 DaPorkchop_
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * Any persons and/or organizations using this software must include the above copyright notice and this permission notice,
 * provide sufficient credit to the original authors of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
    public boolean clampTabList = false;
    public int maxTabRows = 20;
    //public int maxTabCols = 5;
    public JsonObject json = new JsonObject();

    private HUDTranslator() {

    }

    public void encode(JsonObject json) {
        json.addProperty("drawLogo", this.drawLogo);
        json.addProperty("arrayList", this.arrayList);
        json.addProperty("tps", this.TPS);
        json.addProperty("coords", this.coords);
        json.addProperty("netherCoords", this.netherCoords);
        json.addProperty("arrayListTop", this.arrayListTop);
        json.addProperty("serverBrand", this.serverBrand);
        json.addProperty("rainbow", this.rainbow);
        json.addProperty("r", this.r);
        json.addProperty("g", this.g);
        json.addProperty("b", this.b);
        json.addProperty("direction", this.direction);
        json.addProperty("armor", this.armor);
        json.addProperty("effects", this.effects);
        json.addProperty("fps", this.fps);
        json.addProperty("ping", this.ping);
        json.addProperty("clampTabList", this.clampTabList);
        json.addProperty("maxTabRows", this.maxTabRows);
        //json.addProperty("maxTabCols", this.maxTabCols);
    }

    public void decode(String fieldName, JsonObject json) {
        this.json = json;
    }

    public void parseConfigLate() {
        this.drawLogo = this.getBoolean(this.json, "drawLogo", this.drawLogo);
        this.arrayList = this.getBoolean(this.json, "arrayList", this.arrayList);
        this.TPS = this.getBoolean(this.json, "tps", this.TPS);
        this.coords = this.getBoolean(this.json, "coords", this.coords);
        this.netherCoords = this.getBoolean(this.json, "netherCoords", this.netherCoords);
        this.arrayListTop = this.getBoolean(this.json, "arrayListTop", this.arrayListTop);
        this.serverBrand = this.getBoolean(this.json, "serverBrand", this.serverBrand);
        this.rainbow = this.getBoolean(this.json, "rainbow", this.rainbow);
        this.r = this.getInt(this.json, "r", this.r);
        this.g = this.getInt(this.json, "g", this.g);
        this.b = this.getInt(this.json, "b", this.b);
        this.direction = this.getBoolean(this.json, "direction", this.direction);
        this.armor = this.getBoolean(this.json, "armor", this.armor);
        this.effects = this.getBoolean(this.json, "effects", this.effects);
        this.fps = this.getBoolean(this.json, "fps", this.fps);
        this.ping = this.getBoolean(this.json, "ping", this.ping);
        this.clampTabList = this.getBoolean(this.json, "clampTabList", this.clampTabList);
        this.maxTabRows = this.getInt(this.json, "maxTabRows", this.maxTabRows);
        //this.maxTabCols = this.getInt(this.json, "maxTabCols", this.maxTabCols);
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
        return (255) << 24 | (this.r & 255) << 16 | (this.g & 255) << 8 | (this.b & 255);
    }
}
