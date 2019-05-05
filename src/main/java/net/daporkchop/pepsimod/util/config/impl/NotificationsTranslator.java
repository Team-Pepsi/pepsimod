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

public class NotificationsTranslator implements IConfigTranslator {
    public static final NotificationsTranslator INSTANCE = new NotificationsTranslator();
    public boolean queue = false;
    public boolean death = false;
    public boolean chat = false;
    public boolean player = false;

    private NotificationsTranslator() {

    }

    public void encode(JsonObject json) {
        json.addProperty("queue", this.queue);
        json.addProperty("death", this.death);
        json.addProperty("chat", this.chat);
        json.addProperty("player", this.player);
    }

    public void decode(String fieldName, JsonObject json) {
        this.queue = this.getBoolean(json, "queue", this.queue);
        this.death = this.getBoolean(json, "death", this.death);
        this.chat = this.getBoolean(json, "chat", this.chat);
        this.player = this.getBoolean(json, "player", this.player);
    }

    public String name() {
        return "notifications";
    }
}
