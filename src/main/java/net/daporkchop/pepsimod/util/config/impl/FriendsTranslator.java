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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.daporkchop.pepsimod.util.config.IConfigTranslator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FriendsTranslator implements IConfigTranslator {
    public static final FriendsTranslator INSTANCE = new FriendsTranslator();
    public Set<UUID> friends = new HashSet<>();

    private FriendsTranslator() {
    }

    public void encode(JsonObject json) {
        JsonArray array = new JsonArray();
        for (UUID uuid : this.friends) {
            JsonObject object = new JsonObject();
            object.addProperty("msb", uuid.getMostSignificantBits());
            object.addProperty("lsb", uuid.getLeastSignificantBits());
            array.add(object);
        }
        json.add("friends", array);
    }

    public void decode(String fieldName, JsonObject json) {
        JsonArray array = this.getArray(json, "friends", new JsonArray());
        for (JsonElement element : array) {
            if (element.isJsonPrimitive())  {
                //convert old format
                this.friends.add(UUID.fromString(element.getAsString()));
            } else {
                JsonObject object = element.getAsJsonObject();
                this.friends.add(new UUID(object.get("msb").getAsLong(), object.get("lsb").getAsLong()));
            }
        }
    }

    public boolean isFriend(Entity entity)  {
        return this.friends.contains(entity.getUniqueID());
    }

    public String name() {
        return "friends";
    }
}
