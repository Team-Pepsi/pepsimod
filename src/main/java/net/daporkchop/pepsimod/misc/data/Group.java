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

import net.daporkchop.pepsimod.util.PepsiConstants;
import net.daporkchop.pepsimod.util.render.Texture;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * A single group of players. A group may set some additional attributes about a player, such as their background color in the tab list, an icon
 * next to their player name, or their cape/elytra texture.
 *
 * @author DaPorkchop_
 */
public class Group extends PepsiConstants implements AutoCloseable {
    public final String id;
    public final String name;
    public final Set<UUID> members;
    public final int color;
    public final AtomicReference<Texture> cape = new AtomicReference<>(null);
    public final AtomicReference<Texture> icon = new AtomicReference<>(null);

    public Group(String id, String name, Set<UUID> members, int color, Texture cape, Texture icon) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = name == null ? id : name;
        this.members = Collections.unmodifiableSet(Objects.requireNonNull(members, "members"));
        this.color = color & 0xFFFFFF;
        this.cape.set(cape);
        this.icon.set(icon);
    }

    public void doWithCapeIfPresent(Consumer<Texture> callback) {
        Texture cape = this.cape.get();
        if (cape != null) {
            callback.accept(cape);
        }
    }

    public void doWithIconIfPresent(Consumer<Texture> callback) {
        Texture icon = this.icon.get();
        if (icon != null) {
            callback.accept(icon);
        }
    }

    @Override
    public void close() {
        this.cape.updateAndGet(loc -> {
            if (loc != null)    {
                loc.close();
            }
            return null;
        });
        this.icon.updateAndGet(loc -> {
            if (loc != null) {
                loc.close();
            }
            return null;
        });
    }
}
