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

package net.daporkchop.pepsimod.util.resources;

import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import net.daporkchop.lib.common.misc.file.PFiles;
import net.daporkchop.pepsimod.Pepsimod.SystemConfig;
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
public final class Resources implements PepsiConstants {
    protected final File cacheDir = PFiles.ensureDirectoryExists(new File(mc.gameDir, "pepsimod/resources/"));

    protected final Lang     lang     = new Lang();
    protected final MainMenu mainMenu = new MainMenu();

    @Getter(AccessLevel.NONE)
    protected final boolean enabled = SystemConfig.resources.enable;
    @Getter(AccessLevel.NONE)
    protected String baseUrl;

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
        if (!this.enabled) {
            return;
        }
        this.baseUrl = "";
        JsonObject root = this.getJson(PepsimodMixinLoader.OBFUSCATED ? SystemConfig.resources.baseUrl : "resources.json");
        if (root != null) {
            this.baseUrl = root.get("baseurl").getAsString();
            JsonObject data = root.getAsJsonObject("data");

            this.lang.load(this, this.getJson(data.get("lang").getAsString()));
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
                    while (buf.writeBytes(in, (2 << 20) - buf.readableBytes()) >= 0) {
                        ;
                    }
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
