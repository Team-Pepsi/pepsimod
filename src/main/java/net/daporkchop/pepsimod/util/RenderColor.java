package net.daporkchop.pepsimod.util;

public class RenderColor {
    public byte r;
    public byte g;
    public byte b;
    public byte a;

    public RenderColor(int r, int g, int b, int a) {
        this.r = (byte) Math.floorDiv(r, 2);
        this.g = (byte) Math.floorDiv(g, 2);
        this.b = (byte) Math.floorDiv(b, 2);
        this.a = (byte) Math.floorDiv(a, 2);
    }
}
