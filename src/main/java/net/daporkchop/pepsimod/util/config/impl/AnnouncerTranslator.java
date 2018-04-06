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

public class AnnouncerTranslator implements IConfigTranslator {
    public static final AnnouncerTranslator INSTANCE = new AnnouncerTranslator();
    public boolean clientSide = false;
    public boolean join = false;
    public boolean leave = false;
    public boolean eat = false;
    public boolean walk = false;
    public boolean mine = false;
    public boolean place = false;
    public int delay = 5000;

    private AnnouncerTranslator() {

    }

    public void encode(JsonObject json) {
        json.addProperty("clientSide", clientSide);
        json.addProperty("join", join);
        json.addProperty("leave", leave);
        json.addProperty("eat", eat);
        json.addProperty("walk", walk);
        json.addProperty("mine", mine);
        json.addProperty("place", place);
        json.addProperty("delay", delay);
    }

    public void decode(String fieldName, JsonObject json) {
        clientSide = getBoolean(json, "clientSide", clientSide);
        join = getBoolean(json, "join", join);
        leave = getBoolean(json, "leave", leave);
        eat = getBoolean(json, "eat", eat);
        walk = getBoolean(json, "walk", walk);
        mine = getBoolean(json, "mine", mine);
        place = getBoolean(json, "place", place);
        delay = getInt(json, "delay", delay);
    }

    public String name() {
        return "announcer";
    }
}
