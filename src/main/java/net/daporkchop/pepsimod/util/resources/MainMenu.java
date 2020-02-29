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
