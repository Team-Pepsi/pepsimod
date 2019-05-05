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

public class AntiAFKTranslator implements IConfigTranslator {
    public static final AntiAFKTranslator INSTANCE = new AntiAFKTranslator();

    //spinning
    public boolean spin = false;
    public boolean sneak = true;
    public boolean swingArm = true;
    public boolean move = false;
    public boolean strafe = false;

    public int delay = 5000;
    public boolean requireInactive = true;

    private AntiAFKTranslator() {
    }

    public void encode(JsonObject json) {
        json.addProperty("spin", this.spin);
        json.addProperty("sneak", this.sneak);
        json.addProperty("swingArm", this.swingArm);
        json.addProperty("move", this.move);
        json.addProperty("strafe", this.strafe);
        json.addProperty("delay", this.delay);
        json.addProperty("requireInactive", this.requireInactive);
    }

    public void decode(String fieldName, JsonObject json) {
        this.spin = this.getBoolean(json, "spin", this.spin);
        this.sneak = this.getBoolean(json, "sneak", this.sneak);
        this.swingArm = this.getBoolean(json, "swingArm", this.swingArm);
        this.move = this.getBoolean(json, "move", this.move);
        this.strafe = this.getBoolean(json, "strafe", this.strafe);
        this.delay = this.getInt(json, "delay", this.delay);
        this.requireInactive = this.getBoolean(json, "requireInactive", this.requireInactive);
    }

    public String name() {
        return "antiafk";
    }
}
