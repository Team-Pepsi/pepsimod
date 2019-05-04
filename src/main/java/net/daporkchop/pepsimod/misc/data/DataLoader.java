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
import net.daporkchop.lib.binary.UTF8;
import net.daporkchop.lib.common.function.io.IOConsumer;
import net.daporkchop.lib.common.function.io.IOFunction;
import net.daporkchop.pepsimod.PepsiModMixinLoader;
import net.daporkchop.pepsimod.util.PepsiConstants;
import net.daporkchop.pepsimod.util.ReflectionStuff;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.stream.StreamSupport;

/**
 * @author DaPorkchop_
 */
public class DataLoader extends PepsiConstants {
    protected static final String RESOURCES_URL = "https://raw.githubusercontent.com/Team-Pepsi/pepsimod/master/resources/resources.json";

    protected static IOFunction<String, InputStream> READER_FUNCTION = null;

    public static Groups groups = new Groups();
    public static final Map<String, String> localeKeys = new HashMap<>();
    public static String[] splashes = {""};
    public static ResourceLocation banner;

    public static void load() {
        try {
            JsonObject root = getResourcesRoot();
            {
                String baseurl = root.get("baseurl").getAsString();
                READER_FUNCTION = PepsiModMixinLoader.isObfuscatedEnvironment ?
                        s -> new URL(String.format("%s%s", baseurl, s)).openStream() :
                        s -> new BufferedInputStream(new FileInputStream(new File(String.format("../resources/%s", s))));
            }
            JsonObject data = root.get("data").getAsJsonObject();
            if (data.has("groups")) {
                JsonObject json = readJson(data.get("groups").getAsString());
                Groups groups = new Groups();
                DataLoader.groups.cleanup();

                groups.playerToGroup = new HashMap<>();
                groups.groups = new HashSet<>();
                StreamSupport.stream(json.getAsJsonArray("groups").spliterator(), false)
                        .map(JsonElement::getAsString)
                        .map((IOFunction<String, JsonObject>) DataLoader::readJson)
                        .forEach((IOConsumer<JsonObject>) object -> {
                            Groups.Group group = new Groups.Group();
                            group.id = object.get("id").getAsString();
                            group.name = object.has("name") ? object.get("name").getAsString() : group.id;
                            group.cape = object.has("cape") ? readTexture(object.get("cape").getAsString()) : null;
                            group.icon = object.has("icon") ? readTexture(object.get("icon").getAsString()) : null;

                            group.members = new HashSet<>();
                            StreamSupport.stream(object.getAsJsonArray("members").spliterator(), false)
                                    .map(JsonElement::getAsString)
                                    .map(UUID::fromString)
                                    .forEach(uuid -> {
                                        group.members.add(uuid);
                                        groups.playerToGroup.put(uuid, group);
                                    });
                            groups.groups.add(group);
                        });
                DataLoader.groups = groups;
            }
            if (data.has("lang")) {
                JsonObject json = readJson(data.get("lang").getAsString());
                Map<String, String> internalMap = ReflectionStuff.getLanguageMapMap();
                Map<String, String> ourMap = new HashMap<>();
                for (Map.Entry<String, JsonElement> entry : json.getAsJsonObject("translations").entrySet()) {
                    ourMap.put(entry.getKey(), entry.getValue().getAsString());
                }
                localeKeys.forEach(internalMap::remove);
                internalMap.putAll(ourMap);
                localeKeys.clear();
                localeKeys.putAll(ourMap);
            }
            if (data.has("mainmenu")) {
                JsonObject json = readJson(data.get("mainmenu").getAsString());

                splashes = StreamSupport.stream(json.getAsJsonArray("splashes").spliterator(), false)
                        .map(JsonElement::getAsString)
                        .toArray(String[]::new);

                if (banner != null) {
                    mc.getTextureManager().deleteTexture(banner);
                }
                banner = readTexture(json.get("banner").getAsString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static JsonObject getResourcesRoot() throws IOException {
        if (PepsiModMixinLoader.isObfuscatedEnvironment) {
            try (InputStream in = new URL(RESOURCES_URL).openStream()) {
                return readJson(in);
            }
        } else {
            try (InputStream in = new BufferedInputStream(new FileInputStream(new File("../resources/resources.json")))) {
                return readJson(in);
            }
        }
    }

    protected static JsonObject readJson(InputStream in) {
        return new JsonParser().parse(new InputStreamReader(in, UTF8.utf8)).getAsJsonObject();
    }

    protected static JsonObject readJson(String path) throws IOException {
        try (InputStream in = READER_FUNCTION.apply(path)) {
            return readJson(in);
        }
    }

    protected static ResourceLocation readTexture(String path) throws IOException {
        DynamicTexture texture;
        try (InputStream in = READER_FUNCTION.apply(path)) {
            texture = new DynamicTexture(ImageIO.read(in));
        }
        return mc.getTextureManager().getDynamicTextureLocation(String.format("pepsimod_%s", path), texture);
    }
}
