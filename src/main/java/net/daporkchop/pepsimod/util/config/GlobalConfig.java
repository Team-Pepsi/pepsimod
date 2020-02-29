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
        @Option.Default(enumValue = "RAINBOW")
        public TextRenderer.Type type;

        @UtilityClass
        @OptionRoot(id = "general.text.rainbow", type = Type.GLOBAL)
        public static class Rainbow {
            @Option(comment = {
                    "The speed at which the rainbow effect will run.",
                    "Unit: ms per full color cycle"
            })
            @Option.Default(intValue = 3000)
            public int speed;

            @Option(comment = {
                    "The scale of the rainbow effect.",
                    "Unit: (not sure)"
            })
            @Option.Default(floatValue = 0.03f)
            public float scale;

            @Option(comment = {
                    "The direction that the rainbow effect will move towards.",
                    "Unit: degrees"
            })
            @Option.Default(floatValue = 45.0f)
            public float rotation;
        }
    }
}
