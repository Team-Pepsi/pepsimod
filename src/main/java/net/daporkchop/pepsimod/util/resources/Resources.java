/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2016-2019 DaPorkchop_
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

package net.daporkchop.pepsimod.util.resources;

import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import net.daporkchop.lib.common.misc.file.PFiles;
import net.daporkchop.pepsimod.asm.PepsimodMixinLoader;
import net.daporkchop.pepsimod.util.PepsiConstants;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;

/**
 * Loads pepsimod's resources (such as groups, capes and splash texts).
 *
 * @author DaPorkchop_
 */
@Getter
@Accessors(fluent = true)
public final class Resources implements PepsiConstants {
    protected final String url;
    protected final File   cacheDir;

    protected final MainMenu mainMenu = new MainMenu();

    @Getter(AccessLevel.NONE)
    protected String baseUrl;

    public Resources(@NonNull String url, @NonNull File cacheDir) {
        this.url = url;
        this.cacheDir = PFiles.ensureDirectoryExists(cacheDir);
    }

    /**
     * Attempts to (re)load all resources, printing a warning to the log in case of failure.
     */
    public void tryLoad() {
        try {
            this.load();
        } catch (IOException e) {
            log.warn("Unable to reload resources!");
        }
    }

    /**
     * (Re)loads all resources.
     *
     * @throws IOException if an IO exception occurs you dummy
     */
    public void load() throws IOException {
        this.baseUrl = "";
        JsonObject root = this.getJson(PepsimodMixinLoader.OBFUSCATED ? this.url : "resources.json");
        if (root != null) {
            this.baseUrl = root.get("baseurl").getAsString();
            JsonObject data = root.getAsJsonObject("data");

            this.mainMenu.load(this, this.getJson(data.get("mainmenu").getAsString()));
        }
    }

    JsonObject getJson(@NonNull String url) throws IOException {
        byte[] b = this.getBytes(url);
        return b == null ? null : JSON_PARSER.parse(new InputStreamReader(new ByteArrayInputStream(b))).getAsJsonObject();
    }

    BufferedImage getImage(@NonNull String url) throws IOException {
        byte[] b = this.getBytes(url);
        return b == null ? null : ImageIO.read(new ByteArrayInputStream(b));
    }

    byte[] getBytes(@NonNull String url) throws IOException {
        if (PepsimodMixinLoader.OBFUSCATED) {
            ByteBuf buf = PooledByteBufAllocator.DEFAULT.ioBuffer();
            try {
                File cacheFile = this.baseUrl.isEmpty() ? null : new File(this.cacheDir, url);

                try (InputStream in = new URL(this.baseUrl + url).openStream()) {
                    while (buf.writeBytes(in, (2 << 20) - buf.readableBytes()) >= 0)
                        ;
                } catch (IOException e) {
                    //try to load from cache
                    if (cacheFile != null && cacheFile.exists() && cacheFile.isFile()) {
                        byte[] b = new byte[(int) cacheFile.length()];
                        try (InputStream in = new FileInputStream(cacheFile)) {
                            if (in.read(b) != b.length) {
                                log.warn("Couldn't read entire file from disk!");
                                b = null;
                            }
                        }
                        return b;
                    } else {
                        return null; //couldn't load from cache
                    }
                }

                byte[] b = new byte[buf.readableBytes()];
                buf.readBytes(b);

                //check if we should store in cache
                if (cacheFile != null) {
                    try (OutputStream out = new FileOutputStream(PFiles.ensureFileExists(cacheFile))) {
                        out.write(b);
                    }
                }

                return b;
            } finally {
                buf.release();
            }
        } else {
            //in a dev environment, we just want to read straight from the resources dir
            File file = new File(mc.gameDir, "../resources/" + url);
            byte[] b = null;
            if (file.exists() && file.isFile()) {
                b = new byte[(int) file.length()];
                try (InputStream in = new FileInputStream(file)) {
                    if (in.read(b) != b.length) {
                        log.warn("Couldn't read entire file from disk!");
                        b = null;
                    }
                }
            }
            return b;
        }
    }
}
