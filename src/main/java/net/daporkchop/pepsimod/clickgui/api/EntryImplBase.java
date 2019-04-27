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

package net.daporkchop.pepsimod.clickgui.api;

import net.daporkchop.pepsimod.util.misc.Default;

public abstract class EntryImplBase extends Default implements IEntry {
    public final int width;
    public final int height;
    public int x;
    public int y;
    protected boolean isHoveredCached = false;

    public EntryImplBase(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public static void drawString(int x, int y, String text, int color) {
        mc.fontRenderer.drawString(text, x, y, color, false);
    }

    public boolean isMouseHovered() {
        return this.isHoveredCached;
    }

    protected void updateIsMouseHovered(int mouseX, int mouseY) {
        int x = this.getX(), y = this.getY();
        int maxX = x + this.width, maxY = y + this.height;
        this.isHoveredCached = (x <= mouseX && mouseX <= maxX && y <= mouseY && mouseY <= maxY);
    }
}
