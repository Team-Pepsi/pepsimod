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

package net.daporkchop.pepsimod.util.misc.waypoints;

import net.minecraft.util.math.Vec3d;

public class Waypoint {
    public final String name;
    public final int x;
    public final int y;
    public final int z;
    public final int dim;
    public boolean shown;

    public Waypoint(String name, double x, double y, double z, int dim) {
        this(name, x, y, z, true, dim);
    }

    public Waypoint(String name, double x, double y, double z, boolean shown, int dim) {
        this(name, (int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z), shown, dim);
    }

    public Waypoint(String name, int x, int y, int z, int dim) {
        this(name, x, y, z, true, dim);
    }

    public Waypoint(String name, int x, int y, int z, boolean shown, int dim) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.shown = shown;
        this.dim = dim;
    }

    public Vec3d getPosition() {
        return new Vec3d(this.x, this.y, this.z);
    }
}
