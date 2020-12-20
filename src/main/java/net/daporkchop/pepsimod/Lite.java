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

package net.daporkchop.pepsimod;

import net.daporkchop.pepsimod.misc.data.DataLoader;
import net.daporkchop.pepsimod.util.ReflectionStuff;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.io.File;

/**
 * Controls enabling/disabling pepsimod "lite" (no hacks) mode.
 *
 * @author DaPorkchop_
 */
@Mod(modid = "pepsimod-lite",
        version = "v11.1",
        useMetadata = true)
public class Lite {
    /**
     * Whether or not pepsimod is currently in lite mode.
     */
    public static final boolean LITE;

    public static DataLoader DATA;

    static {
        String jvm = System.getProperty("pepsimod.lite", null);
        if (jvm != null) { //jvm argument overrides everything
            LITE = Boolean.parseBoolean(jvm);
        } else { //check for marker file
            LITE = new File("pepsimod/lite.txt").exists();
        }
    }

    @Mod.EventHandler
    public void construction(FMLConstructionEvent event) {
        ReflectionStuff.init();

        DATA = new DataLoader(
                "https://raw.githubusercontent.com/Team-Pepsi/pepsimod/master/resources/resources.json",
                new File("pepsimod/resources/"));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        DATA.load();
    }
}
