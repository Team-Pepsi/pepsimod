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

public class TracersTranslator implements IConfigTranslator {
    public static final TracersTranslator INSTANCE = new TracersTranslator();
    public boolean sleeping = false;
    public boolean invisible = false;
    public boolean friendColors = true;
    public boolean animals = false;
    public boolean monsters = false;
    public boolean players = false;
    public boolean items = false;
    public boolean everything = false;
    public boolean distanceColor = true;

    private TracersTranslator() {

    }

    public void encode(JsonObject json) {
        json.addProperty("sleeping", sleeping);
        json.addProperty("invisible", invisible);
        json.addProperty("friendColors", friendColors);
        json.addProperty("animals", animals);
        json.addProperty("monsters", monsters);
        json.addProperty("players", players);
        json.addProperty("items", items);
        json.addProperty("everything", everything);
        json.addProperty("distanceColor", distanceColor);
    }

    public void decode(String fieldName, JsonObject json) {
        sleeping = getBoolean(json, "sleeping", sleeping);
        invisible = getBoolean(json, "invisible", invisible);
        friendColors = getBoolean(json, "friendColors", friendColors);
        animals = getBoolean(json, "animals", animals);
        monsters = getBoolean(json, "monsters", monsters);
        players = getBoolean(json, "players", players);
        items = getBoolean(json, "items", items);
        everything = getBoolean(json, "everything", everything);
        distanceColor = getBoolean(json, "distanceColor", distanceColor);
    }

    public String name() {
        return "tracers";
    }
}
