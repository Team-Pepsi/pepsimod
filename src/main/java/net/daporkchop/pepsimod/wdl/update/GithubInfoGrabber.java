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

package net.daporkchop.pepsimod.wdl.update;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.daporkchop.pepsimod.wdl.VersionConstants;
import net.daporkchop.pepsimod.wdl.WDL;
import net.daporkchop.pepsimod.wdl.WDLMessageTypes;
import net.daporkchop.pepsimod.wdl.WDLMessages;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Uses <a href="https://developer.github.com/v3/">GitHub's API</a> to get
 * various data.
 */
public class GithubInfoGrabber {
    @Nonnull
    private static final String USER_AGENT;
    @Nonnull
    private static final JsonParser PARSER = new JsonParser();
    /**
     * Location of the entire release list.
     */
    @Nonnull
    private static final String RELEASE_LIST_LOCATION = "https://api.github.com/repos/" + WDL.GITHUB_REPO + "/releases?per_page=100";
    /**
     * File for the release cache.
     */
    @Nonnull
    private static final File CACHED_RELEASES_FILE = new File(
            Minecraft.getMinecraft().mcDataDir,
            "WorldDownloader_Update_Cache.json");

    static {
        String mcVersion = VersionConstants.getMinecraftVersionInfo();
        String wdlVersion = VersionConstants.getModVersion();

        USER_AGENT = String.format("World Downloader mod by Pokechu22 "
                + "(Minecraft %s; WDL %s) ", mcVersion, wdlVersion);
    }

    /**
     * Gets a list of all releases.
     *
     * @see https://developer.github.com/v3/repos/releases/#list-releases-for-a-repository
     */
    @Nonnull
    public static List<Release> getReleases() throws Exception {
        JsonArray array = query(RELEASE_LIST_LOCATION).getAsJsonArray();
        List<Release> returned = new ArrayList<>();
        for (JsonElement element : array) {
            returned.add(new Release(element.getAsJsonObject()));
        }
        return returned;
    }

    /**
     * Fetches the given URL, and converts it into a {@link JsonElement}.
     *
     * @param path
     * @return
     * @throws Exception
     */
    @Nonnull
    public static JsonElement query(@Nonnull String path) throws Exception {
        InputStream stream = null;
        try {
            HttpsURLConnection connection = (HttpsURLConnection) (new URL(path))
                    .openConnection();

            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Accept",
                    "application/vnd.github.v3.full+json");
            // ETag - allows checking if the value was modified (and helps
            // avoid getting rate-limited, as if it is unchanged it no
            // longer counts).
            // See https://developer.github.com/v3/#conditional-requests
            if (WDL.globalProps.getProperty("UpdateETag") != null) {
                String etag = WDL.globalProps.getProperty("UpdateETag");
                if (!etag.isEmpty()) {
                    connection.setRequestProperty("If-None-Match", etag);
                }
            }

            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_MODIFIED) {
                // 304 not modified; use the cached version.
                WDLMessages.chatMessageTranslated(WDLMessageTypes.UPDATE_DEBUG,
                        "net.daporkchop.pepsimod.wdl.messages.updates.usingCachedUpdates");

                stream = new FileInputStream(CACHED_RELEASES_FILE);
            } else if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // 200 OK
                WDLMessages.chatMessageTranslated(WDLMessageTypes.UPDATE_DEBUG,
                        "net.daporkchop.pepsimod.wdl.messages.updates.grabingUpdatesFromGithub");

                stream = connection.getInputStream();
            } else {
                throw new Exception("Unexpected response while getting " + path
                        + ": " + connection.getResponseCode() + " "
                        + connection.getResponseMessage());
            }

            try (InputStreamReader reader = new InputStreamReader(stream)) {
                JsonElement element = PARSER.parse(reader);

                // Write that cached version to disk, and save the ETAG.
                String etag = null;
                try (PrintStream output = new PrintStream(CACHED_RELEASES_FILE)) {
                    output.println(element.toString()); // Write to file

                    etag = connection.getHeaderField("ETag");
                } catch (Exception e) {
                    // We don't want to cache an old version if didn't save.
                    etag = null;
                    throw e;
                } finally {
                    if (etag != null) {
                        WDL.globalProps.setProperty("UpdateETag", etag);
                    } else {
                        WDL.globalProps.remove("UpdateETag");
                    }

                    WDL.saveGlobalProps();
                }

                return element;
            }
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
}
