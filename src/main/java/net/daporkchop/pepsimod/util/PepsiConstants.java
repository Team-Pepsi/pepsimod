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

package net.daporkchop.pepsimod.util;

import com.google.gson.JsonParser;
import net.daporkchop.pepsimod.Pepsimod;
import net.daporkchop.pepsimod.util.event.EventManager;
import net.daporkchop.pepsimod.util.render.BetterScaledResolution;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Constant values used throughout the mod.
 *
 * @author DaPorkchop_
 */
public interface PepsiConstants {
    Minecraft              mc            = PepsiUtil.getNull();
    Pepsimod               pepsimod      = Pepsimod.INSTANCE();
    Logger                 log           = LogManager.getFormatterLogger("pepsimod");
    EventManager           EVENT_MANAGER = PepsiUtil.getInputValue(new EventManager());
    JsonParser             JSON_PARSER   = new JsonParser();
    BetterScaledResolution RESOLUTION    = BetterScaledResolution.NOOP;

    String MOD_ID       = "pepsimod";
    String VERSION      = PepsiUtil.getInputValue("unknown");
    String VERSION_FULL = PepsiUtil.getInputValue("unknown");
}
