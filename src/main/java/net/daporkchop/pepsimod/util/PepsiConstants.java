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
