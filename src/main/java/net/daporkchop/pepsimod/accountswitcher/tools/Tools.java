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

package net.daporkchop.pepsimod.accountswitcher.tools;

import net.minecraft.client.gui.Gui;

/**
 * @author MRebhan
 * @author The_Fireplace
 */

public class Tools {
    /**
     * Draws a rectangle with a border
     *
     * @param x           First corner x
     * @param y           First corner y
     * @param x1          Opposite corner x
     * @param y1          Opposite corner y
     * @param size        border width
     * @param borderColor border color(ARGB format)
     * @param insideColor inside color(ARGB format)
     */
    public static void drawBorderedRect(int x, int y, int x1, int y1, int size, int borderColor, int insideColor) {
        Gui.drawRect(x + size, y + size, x1 - size, y1 - size, insideColor);
        Gui.drawRect(x + size, y + size, x1, y, borderColor);
        Gui.drawRect(x, y, x + size, y1, borderColor);
        Gui.drawRect(x1, y1, x1 - size, y + size, borderColor);
        Gui.drawRect(x, y1 - size, x1, y1, borderColor);
    }
}
