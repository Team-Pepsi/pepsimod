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

package net.daporkchop.pepsimod.util.config;

import lombok.experimental.UtilityClass;
import net.daporkchop.pepsimod.util.config.annotation.Option;
import net.daporkchop.pepsimod.util.config.annotation.OptionRoot;
import net.daporkchop.pepsimod.util.config.annotation.OptionRoot.Type;
import net.daporkchop.pepsimod.util.render.text.TextRenderer;

/**
 * @author DaPorkchop_
 */
@UtilityClass
@OptionRoot(id = "general", type = Type.GLOBAL)
public class GlobalConfig {
    @UtilityClass
    @OptionRoot(id = "general.text", type = Type.GLOBAL)
    public static class Text {
        @Option(comment = {
                "The renderer used for displaying most pepsimod text.",
                "Valid options are: NORMAL, RAINBOW"
        })
        @Option.Enum("RAINBOW")
        public TextRenderer.Type type;

        @UtilityClass
        @OptionRoot(id = "general.text.rainbow", type = Type.GLOBAL)
        public static class Rainbow {
            @Option(comment = {
                    "The speed at which the rainbow effect will run.",
                    "Unit: ms per full color cycle"
            })
            @Option.Int(3000)
            public int speed;

            @Option(comment = {
                    "The scale of the rainbow effect.",
                    "Unit: (not sure)"
            })
            @Option.Float(0.03f)
            public float scale;

            @Option(comment = {
                    "The direction that the rainbow effect will move towards.",
                    "Unit: degrees"
            })
            @Option.Float(value = 45.0f, min = -180.0f, max = 180.0f)
            public float rotation;
        }
    }
}
