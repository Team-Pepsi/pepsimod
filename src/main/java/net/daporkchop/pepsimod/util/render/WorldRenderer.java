/*
 * Adapted from The MIT License (MIT)
 *
 * Copyright (c) 2016-2021 DaPorkchop_
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
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

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

    protected final Tessellator tessellator = Tessellator.getInstance();
    protected final BufferBuilder buffer = this.tessellator.getBuffer();

    protected float width = 1.0f;

    protected int r = 0xFF;
    protected int g = 0xFF;
    protected int b = 0xFF;
    protected int a = 0xFF;

    public WorldRenderer(Vec3d start, double x, double y, double z, float partialTicks) {
        this(start.x, start.y, start.z, x, y, z, partialTicks);
    }

    public WorldRenderer(Vec3d start, Vec3d pos, float partialTicks) {
        this(start.x, start.y, start.z, pos.x, pos.y, pos.z, partialTicks);
    }

    public WorldRenderer(double startX, double startY, double startZ, double x, double y, double z, float partialTicks) {
        this.startX = startX + x;
        this.startY = startY + y;
        this.startZ = startZ + z;
        this.x = x;
        this.y = y;
        this.z = z;
        this.partialTicks = partialTicks;

        this.init();
    }

    public WorldRenderer init() {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        glEnable(GL11.GL_LINE_SMOOTH);
        glLineWidth(2.0f);
        GlStateManager.disableTexture2D();
        GlStateManager.enableCull();
        GlStateManager.disableDepth();

        this.buffer.setTranslation(-this.x, -this.y, -this.z);

        return this.resume();
    }

    @Override
    public void close() {
        this.flush();

        this.buffer.setTranslation(0.0d, 0.0d, 0.0d);

        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableCull();
        glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.disableBlend();
    }

    public WorldRenderer resume() {
        this.buffer.begin(GL_LINES, DefaultVertexFormats.POSITION_COLOR);
        return this;
    }

    public WorldRenderer flush() {
        glLineWidth(this.width);
        this.tessellator.draw();
        return this;
    }

    public WorldRenderer color(int r, int g, int b) {
        return this.color(r, g, b, 0xFF);
    }

    public WorldRenderer color(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        return this;
    }

    public WorldRenderer color(RenderColor color) {
        this.r = color.rOrig;
        this.g = color.gOrig;
        this.b = color.bOrig;
        this.a = color.aOrig;
        return this;
    }

    public WorldRenderer color(Color color) {
        this.r = color.getRed();
        this.g = color.getGreen();
        this.b = color.getBlue();
        this.a = color.getAlpha();
        return this;
    }

    public WorldRenderer width(float width) {
        if (width != this.width) {
            this.flush();
            this.width = width;
            this.resume();
        }
        return this;
    }

    public WorldRenderer line(double x1, double y1, double z1, double x2, double y2, double z2) {
        return this.vertex(x1, y1, z1).vertex(x2, y2, z2);
    }

    public WorldRenderer line(float x1, float y1, float z1, float x2, float y2, float z2) {
        return this.vertex(x1, y1, z1).vertex(x2, y2, z2);
    }

    public WorldRenderer line(int x1, int y1, int z1, int x2, int y2, int z2) {
        return this.vertex(x1, y1, z1).vertex(x2, y2, z2);
    }

    public WorldRenderer line(Vec3d pos1, Vec3d pos2) {
        return this.line(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z);
    }

    public WorldRenderer line(Vec3d pos1, Entity pos2) {
        return this.line(pos1.x, pos1.y, pos1.z, pos2.posX, pos2.posY, pos2.posZ);
    }

    public WorldRenderer line(Entity pos1, Entity pos2) {
        return this.line(pos1.posX, pos1.posY, pos1.posZ, pos2.posX, pos2.posY, pos2.posZ);
    }

    public WorldRenderer lineFromEyes(double x, double y, double z) {
        return this.vertex(this.startX, this.startY, this.startZ).vertex(x, y, z);
    }

    public WorldRenderer lineFromEyes(Vec3d pos) {
        return this.lineFromEyes(pos.x, pos.y, pos.z);
    }

    public WorldRenderer lineFromEyes(Entity entity, float partialTicks) {
        if (partialTicks == 1.0F) {
            return this.lineFromEyes(entity.posX, entity.posY, entity.posZ);
        } else {
            double x = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
            double y = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks;
            double z = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
            return this.lineFromEyes(x, y, z);
        }
    }

    public WorldRenderer lineFromEyes(Entity entity) {
        return this.lineFromEyes(entity, this.partialTicks);
    }

    public WorldRenderer outline(AxisAlignedBB bb) {
        return this.outline(bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ);
    }

    public WorldRenderer outline(Entity entity) {
        double x = entity.prevPosX + (entity.posX - entity.prevPosX) * this.partialTicks;
        double y = entity.prevPosY + (entity.posY - entity.prevPosY) * this.partialTicks;
        double z = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * this.partialTicks;
        double halfwidth = entity.width * 0.5d;
        return this.outline(
                x - halfwidth, y, z - halfwidth,
                x + halfwidth, y + entity.height, z + halfwidth
        );
    }

    public WorldRenderer outline(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return this.internal_outline(
                minX, minY, minZ,
                maxX, maxY, maxZ
        );
    }

    protected WorldRenderer internal_outline(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return this.line(minX, minY, minZ, maxX, minY, minZ)
                .line(maxX, minY, minZ, maxX, minY, maxZ)
                .line(maxX, minY, maxZ, minX, minY, maxZ)
                .line(minX, minY, maxZ, minX, minY, minZ)
                .line(minX, minY, minZ, minX, maxY, minZ)
                .line(maxX, minY, minZ, maxX, maxY, minZ)
                .line(maxX, minY, maxZ, maxX, maxY, maxZ)
                .line(minX, minY, maxZ, minX, maxY, maxZ)
                .line(minX, maxY, minZ, maxX, maxY, minZ)
                .line(maxX, maxY, minZ, maxX, maxY, maxZ)
                .line(maxX, maxY, maxZ, minX, maxY, maxZ)
                .line(minX, maxY, maxZ, minX, maxY, minZ);
    }

    protected WorldRenderer vertex(double x, double y, double z) {
        this.buffer.pos(x, y, z).color(this.r, this.g, this.b, this.a).endVertex();
        return this;
    }
}
