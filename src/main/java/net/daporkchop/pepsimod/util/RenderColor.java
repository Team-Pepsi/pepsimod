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

package net.daporkchop.pepsimod.util;

import org.lwjgl.opengl.GL11;

public class RenderColor {
    public static void glColor(int r, int g, int b) {
        glColor(r, g, b, 255);
    }

    public static void glColor(int r, int g, int b, int a) {
        GL11.glColor4b((byte) Math.floorDiv(r, 2), (byte) Math.floorDiv(g, 2), (byte) Math.floorDiv(b, 2), (byte) Math.floorDiv(a, 2));
    }

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

    public int getIntColor() {
        return (this.a & 255) << 24 | (this.r & 255) << 16 | (this.g & 255) << 8 | (this.b & 255);
    }
}
