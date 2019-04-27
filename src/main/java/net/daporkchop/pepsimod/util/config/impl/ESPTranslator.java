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
    }

    public String name() {
        return "esp";
    }
}
