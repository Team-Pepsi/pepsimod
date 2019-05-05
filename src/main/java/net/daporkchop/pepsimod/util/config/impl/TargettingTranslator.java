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
        json.addProperty("players", this.players);
        json.addProperty("animals", this.animals);
        json.addProperty("monsters", this.monsters);
        json.addProperty("golems", this.golems);
        json.addProperty("sleeping", this.sleeping);
        json.addProperty("invisible", this.invisible);
        json.addProperty("teams", this.teams);
        json.addProperty("friends", this.friends);
        json.addProperty("through_walls", this.through_walls);
        json.addProperty("use_cooldown", this.use_cooldown);
        json.addProperty("silent", this.silent);
        json.addProperty("rotate", this.rotate);
        json.addProperty("bone", this.targetBone.ordinal());
        json.addProperty("fov", this.fov);
        json.addProperty("reach", this.reach);
        json.addProperty("delay", this.delay);
    }

    public void decode(String fieldName, JsonObject json) {
        this.players = this.getBoolean(json, "players", this.players);
        this.animals = this.getBoolean(json, "animals", this.animals);
        this.monsters = this.getBoolean(json, "monsters", this.monsters);
        this.golems = this.getBoolean(json, "golems", this.golems);
        this.sleeping = this.getBoolean(json, "sleeping", this.sleeping);
        this.invisible = this.getBoolean(json, "invisible", this.invisible);
        this.teams = this.getBoolean(json, "teams", this.teams);
        this.friends = this.getBoolean(json, "friends", this.friends);
        this.through_walls = this.getBoolean(json, "through_walls", this.through_walls);
        this.use_cooldown = this.getBoolean(json, "use_cooldown", this.use_cooldown);
        this.silent = this.getBoolean(json, "silent", this.silent);
        this.rotate = this.getBoolean(json, "rotate", this.rotate);
        this.targetBone = TargetBone.getBone(this.getInt(json, "bone", this.targetBone.ordinal()));
        this.fov = this.getFloat(json, "fov", this.fov);
        this.reach = this.getFloat(json, "reach", this.reach);
        this.delay = this.getInt(json, "delay", this.delay);
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
