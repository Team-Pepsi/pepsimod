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

package net.daporkchop.pepsimod.util.colors;

import java.awt.Color;

public class ColorUtils {
    public static final int BUTTON_OFF_OFF = new Color(255, 32, 32).getRGB();
    public static final int BUTTON_ON_OFF = new Color(244, 66, 66).getRGB();
    public static final int BUTTON_OFF_ON = new Color(43, 120, 255).getRGB();
    public static final int BUTTON_ON_ON = new Color(88, 143, 239).getRGB();
    public static final int WINDOW_ON = new Color(255, 255, 255).getRGB();
    public static final int WINDOW_OFF = new Color(183, 183, 183).getRGB();
    public static final int BACKGROUND = new Color(128, 128, 128).getRGB();
    public static final int TYPE_BUTTON = 0;
    public static final int TYPE_WINDOW = 1;
    public static final int TYPE_SLIDER = 2;
    public static final int TYPE_BG = 3;

    public static int getColorForGuiEntry(int type, boolean hovered, boolean state) {
        switch (type) {
            case TYPE_BUTTON:
                if (hovered) {
                    return state ? BUTTON_ON_ON : BUTTON_ON_OFF;
                } else {
                    return state ? BUTTON_OFF_ON : BUTTON_OFF_OFF;
                }
            case TYPE_WINDOW:
                return hovered ? WINDOW_ON : WINDOW_OFF;
            case TYPE_SLIDER:
                return hovered ? BUTTON_ON_ON : BUTTON_OFF_ON;
            default:
                throw new IllegalStateException("Invalid type: " + type);
        }
    }
}
