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

package net.daporkchop.pepsimod.util.render;

import net.daporkchop.pepsimod.util.RenderColor;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * Helps with drawing things in the world.
 * 
 * @author DaPorkchop_
 */
public class WorldRenderer implements AutoCloseable {
    protected final double startX;
    protected final double startY;
    protected final double startZ;

    protected final double x;
    protected final double y;
    protected final double z;

    protected final float partialTicks;

    public WorldRenderer(double startX, double startY, double startZ, double x, double y, double z, float partialTicks) {
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.x = x;
        this.y = y;
        this.z = z;
        this.partialTicks = partialTicks;

        this.init();
    }

    public WorldRenderer init()  {
        glEnable(GL11.GL_BLEND);
        glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL11.GL_LINE_SMOOTH);
        glLineWidth(2.0f);
        glDisable(GL11.GL_TEXTURE_2D);
        glEnable(GL11.GL_CULL_FACE);
        glDisable(GL11.GL_DEPTH_TEST);

        return this.resume();
    }

    @Override
    public void close() {
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.pause();

        glEnable(GL11.GL_DEPTH_TEST);
        glEnable(GL11.GL_TEXTURE_2D);
        glDisable(GL11.GL_CULL_FACE);
        glDisable(GL11.GL_LINE_SMOOTH);
        glDisable(GL11.GL_BLEND);
    }

    public WorldRenderer resume()    {
        glBegin(GL11.GL_LINES);
        return this;
    }

    public WorldRenderer pause() {
        glEnd();
        return this;
    }

    public WorldRenderer(Vec3d start, double x, double y, double z, float partialTicks) {
        this(start.x, start.y, start.z, x, y, z, partialTicks);
    }

    public WorldRenderer(Vec3d start, Vec3d pos, float partialTicks) {
        this(start.x, start.y, start.z, pos.x, pos.y, pos.z, partialTicks);
    }

    public WorldRenderer color(float r, float g, float b) {
        glColor4f(r, g, b, 1.0f);
        return this;
    }

    public WorldRenderer color(float r, float g, float b, float a) {
        glColor4f(r, g, b, a);
        return this;
    }

    public WorldRenderer color(int r, int g, int b) {
        glColor4f(r * 0.003921569f, g * 0.003921569f, b * 0.003921569f, 1.0f);
        return this;
    }

    public WorldRenderer color(int r, int g, int b, int a) {
        glColor4f(r * 0.003921569f, g * 0.003921569f, b * 0.003921569f, a * 0.003921569f);
        return this;
    }

    public WorldRenderer color(RenderColor color)    {
        glColor4b(color.r, color.g, color.b, color.a);
        return this;
    }

    public WorldRenderer color(Color color)    {
        glColor4f(color.getRed() * 0.003921569f, color.getGreen() * 0.003921569f, color.getBlue() * 0.003921569f, color.getAlpha() * 0.003921569f);
        return this;
    }

    public WorldRenderer width(float width)  {
        this.pause();
        glLineWidth(width);
        return this.resume();
    }

    public WorldRenderer line(double x1, double y1, double z1, double x2, double y2, double z2)  {
        glVertex3d(x1 - this.x, y1 - this.y, z1 - this.z);
        glVertex3d(x2 - this.x, y2 - this.y, z2 - this.z);
        return this;
    }

    public WorldRenderer line(float x1, float y1, float z1, float x2, float y2, float z2)  {
        glVertex3d(x1 - this.x, y1 - this.y, z1 - this.z);
        glVertex3d(x2 - this.x, y2 - this.y, z2 - this.z);
        return this;
    }

    public WorldRenderer line(int x1, int y1, int z1, int x2, int y2, int z2)  {
        glVertex3d(x1 - this.x, y1 - this.y, z1 - this.z);
        glVertex3d(x2 - this.x, y2 - this.y, z2 - this.z);
        return this;
    }

    public WorldRenderer line(Vec3d pos1, Vec3d pos2)  {
        return this.line(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z);
    }

    public WorldRenderer line(Vec3d pos1, Entity pos2)  {
        return this.line(pos1.x, pos1.y, pos1.z, pos2.posX, pos2.posY, pos2.posZ);
    }

    public WorldRenderer line(Entity pos1, Entity pos2)  {
        return this.line(pos1.posX, pos1.posY, pos1.posZ, pos2.posX, pos2.posY, pos2.posZ);
    }
    
    public WorldRenderer lineFromEyes(double x, double y, double z)  {
        glVertex3d(this.startX, this.startY, this.startZ);
        glVertex3d(x - this.x, y - this.y, z - this.z);
        return this;
    }

    public WorldRenderer lineFromEyes(Vec3d pos)  {
        return this.lineFromEyes(pos.x, pos.y, pos.z);
    }

    public WorldRenderer lineFromEyes(Entity entity, float partialTicks)  {
        if (partialTicks == 1.0F) {
            return this.lineFromEyes(entity.posX, entity.posY, entity.posZ);
        } else {
            double x = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
            double y = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks;
            double z = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
            return this.lineFromEyes(x, y, z);
        }
    }

    public WorldRenderer lineFromEyes(Entity entity)  {
        return this.lineFromEyes(entity, this.partialTicks);
    }

    public WorldRenderer outline(AxisAlignedBB bb)   {
        return this.outline(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
    }

    public WorldRenderer outline(Entity entity)   {
        double x = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
        double y = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks;
        double z = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
        double halfwidth = entity.width * 0.5d;
        return this.outline(
                x - halfwidth, y, z - halfwidth,
                x + halfwidth, y + entity.height, z + halfwidth
        );
    }

    public WorldRenderer outline(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)   {
        return this.internal_outline(
                minX - this.x, minY - this.y, minZ - this.z,
                maxX - this.x, maxY - this.y, maxZ - this.z
        );
    }

    protected WorldRenderer internal_outline(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        glVertex3d(minX, minY, minZ);
        glVertex3d(maxX, minY, minZ);

        glVertex3d(maxX, minY, minZ);
        glVertex3d(maxX, minY, maxZ);

        glVertex3d(maxX, minY, maxZ);
        glVertex3d(minX, minY, maxZ);

        glVertex3d(minX, minY, maxZ);
        glVertex3d(minX, minY, minZ);

        glVertex3d(minX, minY, minZ);
        glVertex3d(minX, maxY, minZ);

        glVertex3d(maxX, minY, minZ);
        glVertex3d(maxX, maxY, minZ);

        glVertex3d(maxX, minY, maxZ);
        glVertex3d(maxX, maxY, maxZ);

        glVertex3d(minX, minY, maxZ);
        glVertex3d(minX, maxY, maxZ);

        glVertex3d(minX, maxY, minZ);
        glVertex3d(maxX, maxY, minZ);

        glVertex3d(maxX, maxY, minZ);
        glVertex3d(maxX, maxY, maxZ);

        glVertex3d(maxX, maxY, maxZ);
        glVertex3d(minX, maxY, maxZ);

        glVertex3d(minX, maxY, maxZ);
        glVertex3d(minX, maxY, minZ);
        return this;
    }
}
