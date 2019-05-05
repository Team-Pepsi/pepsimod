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

    public void setup(String[] splashes, Texture banner)    {
        if (this.banner != null)    {
            this.close();
        }
        this.splashes = Objects.requireNonNull(splashes, "splashes");
        this.banner = Objects.requireNonNull(banner, "banner");
    }

    public String getRandomSplash() {
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
