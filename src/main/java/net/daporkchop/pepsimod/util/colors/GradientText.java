/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2018 DaPorkchop_
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
            text += element.text;
        }
    }

    public void drawAtPos(Gui screen, int x, int y) {
        int i = 0;
        for (FixedColorElement element : elements) {
            screen.drawString(Minecraft.getMinecraft().fontRenderer, element.text, x + i, y, element.color);
            i += element.width;
        }
    }

    public void drawWithEndAtPos(Gui screen, int x, int y) {
        int i = 0;
        for (FixedColorElement element : elements) {
            i -= element.width;
        }
        for (FixedColorElement element : elements) {
            screen.drawString(Minecraft.getMinecraft().fontRenderer, element.text, x + i, y, element.color);
            i += element.width;
        }
    }

    public int width() {
        return width;
    }

    public String getRawText() {
        return text;
    }
}
