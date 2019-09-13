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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import net.daporkchop.pepsimod.util.render.texture.SimpleTexture;
import net.daporkchop.pepsimod.util.render.texture.Texture;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.StreamSupport;

/**
 * Contains the resources used for customizing the main menu:
 * - splash texts
 * - banner
 *
 * @author DaPorkchop_
 */
@Getter
public final class MainMenu implements Resource {
    protected static final String[] DEFAULT_SPLASHES = {""};

    protected String[] splashes = DEFAULT_SPLASHES;
    protected Texture banner = Texture.NOOP_TEXTURE;

    @Override
    public void load(@NonNull Resources resources, JsonObject obj) throws IOException {
        if (obj == null) {
            this.splashes = DEFAULT_SPLASHES;
            this.banner = Texture.NOOP_TEXTURE;
        } else {
            this.splashes = StreamSupport.stream(obj.getAsJsonArray("splashes").spliterator(), false)
                    .filter(JsonElement::isJsonPrimitive)
                    .map(JsonElement::getAsString)
                    .toArray(String[]::new);
            this.banner = new SimpleTexture(resources.getImage(obj.get("banner").getAsString()));
        }
    }

    /**
     * Gets a random splash text from the list of splash texts.
     *
     * @return a random splash text
     */
    public String randomSplash() {
        return this.splashes[ThreadLocalRandom.current().nextInt(this.splashes.length)];
    }
}
