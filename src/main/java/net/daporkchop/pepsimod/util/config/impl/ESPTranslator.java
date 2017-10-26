/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017 Team Pepsi
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from Team Pepsi.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: Team Pepsi), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
        json.addProperty("basic", basic);
        json.addProperty("trapped", trapped);
        json.addProperty("ender", ender);
        json.addProperty("hopper", hopper);
        json.addProperty("furnace", furnace);

        json.addProperty("monsters", monsters);
        json.addProperty("animals", animals);
        json.addProperty("players", players);
        json.addProperty("golems", golems);
        json.addProperty("invisible", invisible);
        json.addProperty("friendColors", friendColors);
    }

    public void decode(String fieldName, JsonObject json) {
        basic = getBoolean(json, "basic", basic);
        trapped = getBoolean(json, "trapped", trapped);
        ender = getBoolean(json, "ender", ender);
        hopper = getBoolean(json, "hopper", hopper);
        furnace = getBoolean(json, "furnace", furnace);

        monsters = getBoolean(json, "monsters", monsters);
        animals = getBoolean(json, "animals", animals);
        players = getBoolean(json, "players", players);
        golems = getBoolean(json, "golems", golems);
        invisible = getBoolean(json, "invisible", invisible);
        friendColors = getBoolean(json, "friendColors", friendColors);
    }

    public String name() {
        return "esp";
    }
}
