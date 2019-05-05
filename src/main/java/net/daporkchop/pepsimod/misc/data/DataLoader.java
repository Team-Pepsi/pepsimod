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

package net.daporkchop.pepsimod.misc.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import net.daporkchop.lib.common.function.io.IOConsumer;
import net.daporkchop.lib.common.function.io.IOFunction;
import net.daporkchop.pepsimod.PepsiModMixinLoader;
import net.daporkchop.pepsimod.util.PepsiConstants;
import net.daporkchop.pepsimod.util.ReflectionStuff;
import net.daporkchop.pepsimod.util.Texture;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author DaPorkchop_
 */
public class DataLoader extends PepsiConstants {
    protected final String resourcesUrl;
    protected final JsonObject root;
    protected IOFunction<String, InputStream> readerFunction;

    public final Groups groups = new Groups();
    public final Map<String, String> localeKeys = new HashMap<>();
    public final MainMenu mainMenu = new MainMenu();

    public DataLoader(String resourcesUrl) {
        this.resourcesUrl = Objects.requireNonNull(resourcesUrl, "resourcesUrl");

        this.root = this.getResourcesRoot();
        {
            String baseurl = this.root.get("baseurl").getAsString();
            this.readerFunction = PepsiModMixinLoader.isObfuscatedEnvironment ?
                    s -> new URL(String.format("%s%s", baseurl, s)).openStream() :
                    s -> new BufferedInputStream(new FileInputStream(new File(String.format("../resources/%s", s))));
        }
    }

    public void load() {
        JsonObject data = this.root.get("data").getAsJsonObject();
        if (data.has("groups")) {
            JsonObject json = this.readJson(data.get("groups").getAsString());
            this.groups.close();

            StreamSupport.stream(json.getAsJsonArray("groups").spliterator(), false)
                         .map(JsonElement::getAsString)
                         .map((IOFunction<String, JsonObject>) this::readJson)
                         .forEach((IOConsumer<JsonObject>) object -> {
                             int color = 0x000000;
                             if (object.has("color")) {
                                 JsonObject colorJson = object.getAsJsonObject("color");
                                 color = (colorJson.get("r").getAsInt() << 16) |
                                         (colorJson.get("g").getAsInt() << 8) |
                                         (colorJson.get("b").getAsInt());
                             }
                             this.groups.addGroup(new Group(
                                     object.get("id").getAsString(),
                                     object.has("name") ? object.get("name").getAsString() : null,
                                     StreamSupport.stream(object.getAsJsonArray("members").spliterator(), false)
                                                  .map(JsonElement::getAsString)
                                                  .map(UUID::fromString)
                                                  .collect(Collectors.toSet()),
                                     color,
                                     object.has("cape") ? this.readTexture(object.get("cape").getAsString()) : null,
                                     object.has("icon") ? this.readTexture(object.get("icon").getAsString()) : null
                             ));
                         });
        }

        if (data.has("lang")) {
            JsonObject json = this.readJson(data.get("lang").getAsString());
            Map<String, String> internalMap = ReflectionStuff.getLanguageMapMap();
            Map<String, String> ourMap = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject("translations").entrySet()) {
                ourMap.put(entry.getKey(), entry.getValue().getAsString());
            }
            this.localeKeys.forEach(internalMap::remove);
            internalMap.putAll(ourMap);
            this.localeKeys.clear();
            this.localeKeys.putAll(ourMap);
        }

        if (data.has("mainmenu")) {
            JsonObject json = this.readJson(data.get("mainmenu").getAsString());
            this.mainMenu.setup(
                    StreamSupport.stream(json.getAsJsonArray("splashes").spliterator(), false)
                                 .map(JsonElement::getAsString)
                                 .toArray(String[]::new),
                    this.readTexture(json.get("banner").getAsString())
            );
        }
    }

    public Group getGroup(EntityPlayer entity)   {
        return this.getGroup(entity.getGameProfile());
    }

    public Group getGroup(NetworkPlayerInfo info)   {
        return this.getGroup(info.getGameProfile());
    }

    public Group getGroup(GameProfile profile)   {
        return this.getGroup(profile.getId());
    }

    public Group getGroup(UUID uuid)   {
        return this.groups.playerToGroup.get(uuid);
    }

    protected JsonObject getResourcesRoot() {
        if (PepsiModMixinLoader.isObfuscatedEnvironment) {
            try (InputStream in = new URL(this.resourcesUrl).openStream()) {
                return this.readJson(in);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            try (InputStream in = new BufferedInputStream(new FileInputStream(new File("../resources/resources.json")))) {
                return this.readJson(in);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected JsonObject readJson(InputStream in) {
        try {
            return new JsonParser().parse(new InputStreamReader(in, "UTF-8")).getAsJsonObject();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("go buy a computer that isn't shit", e);
        }
    }

    protected JsonObject readJson(String path) {
        try (InputStream in = this.readerFunction.apply(path)) {
            return this.readJson(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Texture readTexture(String path) {
        try (InputStream in = this.readerFunction.apply(path)) {
            return new Texture(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
