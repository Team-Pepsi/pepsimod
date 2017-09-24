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

package net.daporkchop.pepsimod.util.misc.waypoints.pathfind;

import net.minecraft.util.math.BlockPos;

public class PathPos extends BlockPos {
    private final boolean jumping;

    public PathPos(BlockPos pos) {
        this(pos, false);
    }

    public PathPos(BlockPos pos, boolean jumping) {
        super(pos);
        this.jumping = jumping;
    }

    public boolean isJumping() {
        return jumping;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (!(obj instanceof PathPos))
            return false;

        PathPos node = (PathPos) obj;
        return getX() == node.getX() && getY() == node.getY()
                && getZ() == node.getZ() && isJumping() == node.isJumping();
    }

    @Override
    public int hashCode() {
        return super.hashCode() * 2 + (isJumping() ? 1 : 0);
    }

    public boolean roughEquals(BlockPos otherPos) {
        return Math.abs(otherPos.getX() - getX()) <= 1
                && Math.abs(otherPos.getY() - getY()) <= 1
                && Math.abs(otherPos.getZ() - getZ()) <= 1;
    }
}
