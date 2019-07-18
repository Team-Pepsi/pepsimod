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

package net.daporkchop.pepsimod.util.mixin;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import net.minecraft.client.gui.BossInfoClient;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;

import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;

/**
 * Used by {@link net.daporkchop.pepsimod.mixin.client.gui.MixinGuiBossOverlay} to actually have boss bars merged together.
 * <p>
 * I can't have this as an inner class because Mixin so it's going here.
 *
 * @author DaPorkchop_
 */
@Getter
@Accessors(fluent = true)
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
