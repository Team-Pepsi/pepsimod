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

package net.daporkchop.pepsimod.module;

import lombok.NonNull;
import lombok.experimental.Accessors;
import net.daporkchop.pepsimod.module.annotation.ModInfo;
import net.daporkchop.pepsimod.module.impl.NoWeatherMod;

/**
 * An enum containing all mods in pepsimod.
 * <p>
 * Ordinals should not be considered persistent, as they can and will change with the addition/removal of new modules.
 * <p>
 * Entries are sorted alphabetically.
 *
 * @author DaPorkchop_
 */
@Accessors(fluent = true)
public enum Mods {
    NoWeather(NoWeatherMod.class);

    protected final Class<? extends Module> clazz;
    protected final String id;
    protected final boolean ghost;

    Mods(@NonNull Class<? extends Module> clazz)    {
        this.clazz = clazz;
        ModInfo info = clazz.getAnnotation(ModInfo.class);
        if (info != null)     {
            this.id = info.value();
            this.ghost = info.ghost();
        } else {
            throw new IllegalStateException("Module doesn't have @ModInfo annotation!");
        }
    }
}
