/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017 Team Pepsi
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from Team Pepsi.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: Team Pepsi), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.daporkchop.pepsimod.util.colors;

import java.awt.*;

public class ColorUtils {
    public static final int BUTTON_OFF_OFF = new Color(255, 32, 32).getRGB();
    public static final int BUTTON_ON_OFF = new Color(244, 66, 66).getRGB();
    public static final int BUTTON_OFF_ON = new Color(43, 120, 255).getRGB();
    public static final int BUTTON_ON_ON = new Color(88, 143, 239).getRGB();
    public static final int WINDOW_ON = new Color(255, 255, 255).getRGB();
    public static final int WINDOW_OFF = new Color(183, 183, 183).getRGB();
    public static final int BACKGROUND = new Color(128, 128, 128).getRGB();
    public static final int TYPE_BUTTON = 0, TYPE_WINDOW = 1, TYPE_SLIDER = 2, TYPE_BG = 3;

    public static int getColorForGuiEntry(int type, boolean hovered, boolean state) {
        switch (type) {
            case TYPE_BUTTON:
                if (hovered) {
                    if (state) {
                        return BUTTON_ON_ON;
                    } else {
                        return BUTTON_ON_OFF;
                    }
                } else {
                    if (state) {
                        return BUTTON_OFF_ON;
                    } else {
                        return BUTTON_OFF_OFF;
                    }
                }
            case TYPE_WINDOW:
                if (hovered) {
                    return WINDOW_ON;
                } else {
                    return WINDOW_OFF;
                }
            case TYPE_SLIDER:
                if (hovered) {
                    return BUTTON_ON_ON;
                } else {
                    return BUTTON_OFF_ON;
                }
            default:
                throw new IllegalStateException("Invalid type: " + type);
        }
    }
}
