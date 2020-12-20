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

package net.daporkchop.pepsimod.misc.data;

import net.daporkchop.pepsimod.util.render.Texture;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Contains additional data injected by pepsimod into Minecraft's main menu
 *
 * @author DaPorkchop_
 */
public class MainMenu implements AutoCloseable {
    protected String[] splashes;
    public Texture banner;

    public void setup(String[] splashes, Texture banner) {
        if (this.banner != null) {
            this.close();
        }
        this.splashes = Objects.requireNonNull(splashes, "splashes");
        this.banner = Objects.requireNonNull(banner, "banner");
    }

    public String getRandomSplash() {
        if (this.splashes == null) {
            return "\u00A7cerror";
        }

        Random r = ThreadLocalRandom.current();
        String[] colors = {
                "\u00A71",
                "\u00A79",
                "\u00A7a",
                "\u00A7b",
                "\u00A7c",
                "\u00A7f",
        };
        return colors[r.nextInt(colors.length)] + this.splashes[r.nextInt(this.splashes.length)];
    }

    @Override
    public void close() {
        this.banner.close();
        this.splashes = null;
    }
}
