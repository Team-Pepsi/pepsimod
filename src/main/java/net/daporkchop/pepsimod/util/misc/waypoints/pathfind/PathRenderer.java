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

package net.daporkchop.pepsimod.util.misc.waypoints.pathfind;

import net.minecraft.util.math.BlockPos;

import static org.lwjgl.opengl.GL11.*;

public class PathRenderer {
    public static void renderArrow(BlockPos start, BlockPos end) {
        int startX = start.getX();
        int startY = start.getY();
        int startZ = start.getZ();

        int endX = end.getX();
        int endY = end.getY();
        int endZ = end.getZ();

        glPushMatrix();

        glBegin(GL_LINES);
        {
            glVertex3d(startX, startY, startZ);
            glVertex3d(endX, endY, endZ);
        }
        glEnd();

        glPopMatrix();
    }

    public static void renderNode(BlockPos pos) {
        glPushMatrix();

        glTranslated(pos.getX(), pos.getY(), pos.getZ());
        glScaled(0.1, 0.1, 0.1);

        glBegin(GL_LINES);
        {
            // middle part
            glVertex3d(0, 0, 1);
            glVertex3d(-1, 0, 0);

            glVertex3d(-1, 0, 0);
            glVertex3d(0, 0, -1);

            glVertex3d(0, 0, -1);
            glVertex3d(1, 0, 0);

            glVertex3d(1, 0, 0);
            glVertex3d(0, 0, 1);

            // top part
            glVertex3d(0, 1, 0);
            glVertex3d(1, 0, 0);

            glVertex3d(0, 1, 0);
            glVertex3d(-1, 0, 0);

            glVertex3d(0, 1, 0);
            glVertex3d(0, 0, -1);

            glVertex3d(0, 1, 0);
            glVertex3d(0, 0, 1);

            // bottom part
            glVertex3d(0, -1, 0);
            glVertex3d(1, 0, 0);

            glVertex3d(0, -1, 0);
            glVertex3d(-1, 0, 0);

            glVertex3d(0, -1, 0);
            glVertex3d(0, 0, -1);

            glVertex3d(0, -1, 0);
            glVertex3d(0, 0, 1);
        }
        glEnd();

        glPopMatrix();
    }
}

