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
        json.addProperty("clientSide", this.clientSide);
        json.addProperty("join", this.join);
        json.addProperty("leave", this.leave);
        json.addProperty("eat", this.eat);
        json.addProperty("walk", this.walk);
        json.addProperty("mine", this.mine);
        json.addProperty("place", this.place);
        json.addProperty("delay", this.delay);
    }

    public void decode(String fieldName, JsonObject json) {
        this.clientSide = this.getBoolean(json, "clientSide", this.clientSide);
        this.join = this.getBoolean(json, "join", this.join);
        this.leave = this.getBoolean(json, "leave", this.leave);
        this.eat = this.getBoolean(json, "eat", this.eat);
        this.walk = this.getBoolean(json, "walk", this.walk);
        this.mine = this.getBoolean(json, "mine", this.mine);
        this.place = this.getBoolean(json, "place", this.place);
        this.delay = this.getInt(json, "delay", this.delay);
    }

    public String name() {
        return "announcer";
    }
}
