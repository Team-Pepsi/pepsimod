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
        return new Vec3d(x, y, z);
    }
}
