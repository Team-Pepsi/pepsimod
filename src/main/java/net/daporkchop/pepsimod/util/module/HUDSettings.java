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

package net.daporkchop.pepsimod.util.module;

import org.lwjgl.opengl.GL11;

import java.io.Serializable;

public class HUDSettings implements Serializable {
    public boolean drawLogo = true;  //
    public boolean arrayList = true; //
    public boolean TPS = false;      //
    public boolean coords = false;
    public boolean netherCoords = false;
    public boolean arrayListTop = true;//
    public boolean serverBrand = false;
    public boolean rainbow = true;
    public int r = 0;
    public int g = 0;
    public int b = 0;
    public boolean direction = true;
    public boolean armor = false;
    public boolean effects = false;
    public boolean liquidVision = false;
    public boolean capes = true;
    public boolean fps = true;//
    public boolean ping = true;//

    public void bindColor() {
        byte r = (byte) Math.floorDiv(this.r, 2);
        byte g = (byte) Math.floorDiv(this.g, 2);
        byte b = (byte) Math.floorDiv(this.b, 2);
        GL11.glColor3b(r, g, b);
    }

    public int getColor()   {
        return (255 & 255) << 24 | (r & 255) << 16 | (g & 255) << 8 | (b & 255) << 0;
    }
}
