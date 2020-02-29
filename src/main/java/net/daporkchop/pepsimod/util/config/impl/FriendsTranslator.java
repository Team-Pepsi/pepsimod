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
