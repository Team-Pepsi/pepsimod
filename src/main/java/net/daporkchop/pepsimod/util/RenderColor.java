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

package net.daporkchop.pepsimod.util;

import org.lwjgl.opengl.GL11;

public class RenderColor {
    public byte r;
    public byte g;
    public byte b;
    public byte a;
    public int rOrig;
    public int gOrig;
    public int bOrig;
    public int aOrig;

    public RenderColor(int r, int g, int b, int a) {
        this.r = (byte) Math.floorDiv(r, 2);
        this.g = (byte) Math.floorDiv(g, 2);
        this.b = (byte) Math.floorDiv(b, 2);
        this.a = (byte) Math.floorDiv(a, 2);
        this.rOrig = r;
        this.gOrig = g;
        this.bOrig = b;
        this.aOrig = a;
    }

    public static void glColor(int r, int g, int b) {
        glColor(r, g, b, 255);
    }

    public static void glColor(int r, int g, int b, int a) {
        GL11.glColor4b((byte) Math.floorDiv(r, 2), (byte) Math.floorDiv(g, 2), (byte) Math.floorDiv(b, 2), (byte) Math.floorDiv(a, 2));
    }

    public int getIntColor() {
        return (this.a & 255) << 24 | (this.r & 255) << 16 | (this.g & 255) << 8 | (this.b & 255);
    }
}
