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
    public float width = 2.0f;

    private TracersTranslator() {
    }

    public void encode(JsonObject json) {
        json.addProperty("sleeping", this.sleeping);
        json.addProperty("invisible", this.invisible);
        json.addProperty("friendColors", this.friendColors);
        json.addProperty("animals", this.animals);
        json.addProperty("monsters", this.monsters);
        json.addProperty("players", this.players);
        json.addProperty("items", this.items);
        json.addProperty("everything", this.everything);
        json.addProperty("distanceColor", this.distanceColor);
        json.addProperty("width", this.width);
    }

    public void decode(String fieldName, JsonObject json) {
        this.sleeping = this.getBoolean(json, "sleeping", this.sleeping);
        this.invisible = this.getBoolean(json, "invisible", this.invisible);
        this.friendColors = this.getBoolean(json, "friendColors", this.friendColors);
        this.animals = this.getBoolean(json, "animals", this.animals);
        this.monsters = this.getBoolean(json, "monsters", this.monsters);
        this.players = this.getBoolean(json, "players", this.players);
        this.items = this.getBoolean(json, "items", this.items);
        this.everything = this.getBoolean(json, "everything", this.everything);
        this.distanceColor = this.getBoolean(json, "distanceColor", this.distanceColor);
        this.width = this.getFloat(json, "width", this.width);
    }

    public String name() {
        return "tracers";
    }
}
