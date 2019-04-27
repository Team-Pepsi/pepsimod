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

package net.daporkchop.pepsimod.util.colors.rainbow;

import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.colors.ColorizedElement;
import net.daporkchop.pepsimod.util.colors.ColorizedText;
import net.daporkchop.pepsimod.util.colors.FixedColorElement;
import net.daporkchop.pepsimod.util.colors.PlainColorElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

import java.awt.Color;

public class RainbowText extends ColorizedText {
    private final ColorizedElement[] elements;
    private final int width;
    private final FontRenderer fontRenderer;
    public String text;
    private int offset;

    public RainbowText(String text) {
        this(text, 0);
    }

    public RainbowText(String text, int offset) {
        this.fontRenderer = mc.fontRenderer;
        this.offset = offset;
        String[] split = text.split(PepsiUtils.COLOR_ESCAPE + "custom");
        String[] split2 = split[0].split("");
        this.elements = new ColorizedElement[split2.length + (split.length > 1 ? 1 : 0)];
        for (int i = 0; i < split2.length; i++) {
            this.elements[i] = new PlainColorElement(split2[i]);
        }
        if (split.length > 1) {
            this.elements[this.elements.length - 1] = new FixedColorElement(Integer.parseInt(split[1].substring(0, Math.min(split[1].length(), 6)), 16), split[1].substring(6));
            int i = 0;
            for (ColorizedElement element : this.elements) {
                i += element.width;
            }
            this.width = i;
            this.text = split[0] + split[1].substring(6);
        } else {
            this.width = this.fontRenderer.getStringWidth(text);
        }
        this.text = text;
    }

    //int debug = 0;
    public void drawAtPos(Gui screen, int x, int y) {
        int i = 0;
        RainbowCycle cycle = PepsiUtils.rainbowCycle(this.offset, PepsiUtils.rainbowCycle.clone());
        for (ColorizedElement element : this.elements) {
            if (element instanceof FixedColorElement) {
                screen.drawString(Minecraft.getMinecraft().fontRenderer, element.text, x + i, y, ((FixedColorElement) element).color);
                return;
            }
            cycle = PepsiUtils.rainbowCycle(1, cycle);
            Color color = new Color(PepsiUtils.ensureRange(cycle.r, 0, 255), PepsiUtils.ensureRange(cycle.g, 0, 255), PepsiUtils.ensureRange(cycle.b, 0, 255));
            screen.drawString(Minecraft.getMinecraft().fontRenderer, element.text, x + i, y, color.getRGB());
            i += element.width;
        }
    }

    public void drawAtPos(Gui screen, int x, int y, int offset) {
        int tempOffset = this.offset;
        this.offset = offset;
        this.drawAtPos(screen, x, y);
        this.offset = tempOffset;
    }

    public int width() {
        return this.width;
    }

    public String getRawText() {
        return this.text;
    }
}
