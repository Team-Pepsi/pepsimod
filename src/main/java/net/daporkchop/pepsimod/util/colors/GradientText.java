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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class GradientText extends ColorizedText {
    public final FixedColorElement[] elements;
    public final int width;
    public String text = "";

    public GradientText(FixedColorElement[] elements, int width) {
        this.elements = elements;
        this.width = width;
        for (FixedColorElement element : elements) {
            this.text += element.text;
        }
    }

    public void drawAtPos(Gui screen, int x, int y) {
        int i = 0;
        for (FixedColorElement element : this.elements) {
            screen.drawString(Minecraft.getMinecraft().fontRenderer, element.text, x + i, y, element.color);
            i += element.width;
        }
    }

    public void drawWithEndAtPos(Gui screen, int x, int y) {
        int i = 0;
        for (FixedColorElement element : this.elements) {
            i -= element.width;
        }
        for (FixedColorElement element : this.elements) {
            screen.drawString(Minecraft.getMinecraft().fontRenderer, element.text, x + i, y, element.color);
            i += element.width;
        }
    }

    public int width() {
        return this.width;
    }

    public String getRawText() {
        return this.text;
    }
}
