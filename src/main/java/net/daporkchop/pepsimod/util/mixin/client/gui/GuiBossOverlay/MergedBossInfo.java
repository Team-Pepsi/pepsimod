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

package net.daporkchop.pepsimod.util.mixin.client.gui.GuiBossOverlay;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import net.minecraft.client.gui.BossInfoClient;
import net.minecraft.world.BossInfo;

import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;

/**
 * Used by {@link net.daporkchop.pepsimod.asm.core.minecraft.client.gui.MixinGuiBossOverlay} to actually have boss bars merged together.
 * <p>
 * I can't have this as an inner class because Mixin so it's going here.
 *
 * @author DaPorkchop_
 */
@Getter
public final class MergedBossInfo {
    private final String name;
    private final Collection<BossInfoClient> entries = Collections.newSetFromMap(new IdentityHashMap<>());

    private BossInfo.Color   color;
    private BossInfo.Overlay overlay;

    public MergedBossInfo(@NonNull String name) {
        this.name = name;
    }

    public void add(@NonNull BossInfoClient info) {
        if (!this.name.equals(info.getName().getFormattedText())) {
            throw new IllegalStateException("Incompatible names!");
        } else if (this.entries.add(info)) {
            this.update();
        }
    }

    public boolean remove(@NonNull BossInfoClient info) {
        if (!this.name.equals(info.getName().getFormattedText())) {
            throw new IllegalStateException("Incompatible names!");
        } else {
            return this.entries.remove(info) && this.update();
        }
    }

    public boolean update() {
        if (!this.entries.isEmpty()) {
            BossInfo first = this.entries.iterator().next();
            this.color = first.getColor();
            this.overlay = first.getOverlay();
            return false;
        } else {
            return true;
        }
    }

    public int count()    {
        return this.entries.size();
    }
}
