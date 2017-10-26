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

public class TargettingTranslator implements IConfigTranslator {
    public static final TargettingTranslator INSTANCE = new TargettingTranslator();
    public boolean players = false;
    public boolean animals = false;
    public boolean monsters = false;
    public boolean golems = false;
    public boolean sleeping = false;
    public boolean invisible = false;
    public boolean teams = false;
    public boolean friends = false;
    public boolean through_walls = false;
    public boolean use_cooldown = false;
    public boolean silent = false;
    public boolean rotate = false;
    public TargetBone targetBone = TargetBone.FEET;
    public float fov = 360f;
    public float reach = 4.25f;
    public int delay = 20;

    private TargettingTranslator() {

    }

    public void encode(JsonObject json) {
        json.addProperty("players", players);
        json.addProperty("animals", animals);
        json.addProperty("monsters", monsters);
        json.addProperty("golems", golems);
        json.addProperty("sleeping", sleeping);
        json.addProperty("invisible", invisible);
        json.addProperty("teams", teams);
        json.addProperty("friends", friends);
        json.addProperty("through_walls", through_walls);
        json.addProperty("use_cooldown", use_cooldown);
        json.addProperty("silent", silent);
        json.addProperty("rotate", rotate);
        json.addProperty("bone", targetBone.ordinal());
        json.addProperty("fov", fov);
        json.addProperty("reach", reach);
        json.addProperty("delay", delay);
    }

    public void decode(String fieldName, JsonObject json) {
        players = getBoolean(json, "players", players);
        animals = getBoolean(json, "animals", animals);
        monsters = getBoolean(json, "monsters", monsters);
        golems = getBoolean(json, "golems", golems);
        sleeping = getBoolean(json, "sleeping", sleeping);
        invisible = getBoolean(json, "invisible", invisible);
        teams = getBoolean(json, "teams", teams);
        friends = getBoolean(json, "friends", friends);
        through_walls = getBoolean(json, "through_walls", through_walls);
        use_cooldown = getBoolean(json, "use_cooldown", use_cooldown);
        silent = getBoolean(json, "silent", silent);
        rotate = getBoolean(json, "rotate", rotate);
        targetBone = TargetBone.getBone(getInt(json, "bone", targetBone.ordinal()));
        fov = getFloat(json, "fov", fov);
        reach = getFloat(json, "reach", reach);
        delay = getInt(json, "delay", delay);
    }

    public String name() {
        return "targetting";
    }

    public enum TargetBone {
        HEAD,
        FEET,
        MIDDLE;

        public static TargetBone getBone(int id) {
            return values()[id];
        }
    }
}
