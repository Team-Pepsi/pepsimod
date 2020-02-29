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

public class ESPTranslator implements IConfigTranslator {
    public static final ESPTranslator INSTANCE = new ESPTranslator();
    public boolean basic = false;
    public boolean trapped = false;
    public boolean ender = false;
    public boolean hopper = false;
    public boolean furnace = false;

    public boolean monsters = false;
    public boolean animals = false;
    public boolean players = false;
    public boolean golems = false;
    public boolean invisible = false;
    public boolean friendColors = true;
    public boolean box = false;

    private ESPTranslator() {

    }

    public void encode(JsonObject json) {
        json.addProperty("basic", this.basic);
        json.addProperty("trapped", this.trapped);
        json.addProperty("ender", this.ender);
        json.addProperty("hopper", this.hopper);
        json.addProperty("furnace", this.furnace);

        json.addProperty("monsters", this.monsters);
        json.addProperty("animals", this.animals);
        json.addProperty("players", this.players);
        json.addProperty("golems", this.golems);
        json.addProperty("invisible", this.invisible);
        json.addProperty("friendColors", this.friendColors);
        json.addProperty("box", this.box);
    }

    public void decode(String fieldName, JsonObject json) {
        this.basic = this.getBoolean(json, "basic", this.basic);
        this.trapped = this.getBoolean(json, "trapped", this.trapped);
        this.ender = this.getBoolean(json, "ender", this.ender);
        this.hopper = this.getBoolean(json, "hopper", this.hopper);
        this.furnace = this.getBoolean(json, "furnace", this.furnace);

        this.monsters = this.getBoolean(json, "monsters", this.monsters);
        this.animals = this.getBoolean(json, "animals", this.animals);
        this.players = this.getBoolean(json, "players", this.players);
        this.golems = this.getBoolean(json, "golems", this.golems);
        this.invisible = this.getBoolean(json, "invisible", this.invisible);
        this.friendColors = this.getBoolean(json, "friendColors", this.friendColors);
        this.box = this.getBoolean(json, "box", this.box);
    }

    public String name() {
        return "esp";
    }
}
