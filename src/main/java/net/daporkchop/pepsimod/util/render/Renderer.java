package net.daporkchop.pepsimod.util.render;

import net.daporkchop.pepsimod.util.RenderColor;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

import static org.lwjgl.opengl.GL11.*;

/**
 * Helps with drawing lines in the world.
 * 
 * @author DaPorkchop_
 */
public class Renderer implements AutoCloseable {
    protected final double startX;
    protected final double startY;
    protected final double startZ;

    protected final double x;
    protected final double y;
    protected final double z;

    protected final float partialTicks;

    public Renderer(double startX, double startY, double startZ, double x, double y, double z, float partialTicks) {
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.x = x;
        this.y = y;
        this.z = z;
        this.partialTicks = partialTicks;

        this.init();
    }

    public Renderer init()  {
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

    public Renderer resume()    {
        glBegin(GL11.GL_LINES);
        return this;
    }

    public Renderer pause() {
        glEnd();
        return this;
    }

    public Renderer(Vec3d start, double x, double y, double z, float partialTicks) {
        this(start.x, start.y, start.z, x, y, z, partialTicks);
    }

    public Renderer(Vec3d start, Vec3d pos, float partialTicks) {
        this(start.x, start.y, start.z, pos.x, pos.y, pos.z, partialTicks);
    }

    public Renderer color(float r, float g, float b) {
        glColor4f(r, g, b, 1.0f);
        return this;
    }

    public Renderer color(float r, float g, float b, float a) {
        glColor4f(r, g, b, a);
        return this;
    }

    public Renderer color(int r, int g, int b) {
        glColor4f(r * 0.003921569f, g * 0.003921569f, b * 0.003921569f, 1.0f);
        return this;
    }

    public Renderer color(int r, int g, int b, int a) {
        glColor4f(r * 0.003921569f, g * 0.003921569f, b * 0.003921569f, a * 0.003921569f);
        return this;
    }

    public Renderer color(RenderColor color)    {
        glColor4b(color.r, color.g, color.b, color.a);
        return this;
    }

    public Renderer color(Color color)    {
        glColor4f(color.getRed() * 0.003921569f, color.getGreen() * 0.003921569f, color.getBlue() * 0.003921569f, color.getAlpha() * 0.003921569f);
        return this;
    }

    public Renderer width(float width)  {
        this.pause();
        glLineWidth(width);
        return this.resume();
    }

    public Renderer line(double x1, double y1, double z1, double x2, double y2, double z2)  {
        glVertex3d(x1 - this.x, y1 - this.y, z1 - this.z);
        glVertex3d(x2 - this.x, y2 - this.y, z2 - this.z);
        return this;
    }

    public Renderer line(float x1, float y1, float z1, float x2, float y2, float z2)  {
        glVertex3d(x1 - this.x, y1 - this.y, z1 - this.z);
        glVertex3d(x2 - this.x, y2 - this.y, z2 - this.z);
        return this;
    }

    public Renderer line(int x1, int y1, int z1, int x2, int y2, int z2)  {
        glVertex3d(x1 - this.x, y1 - this.y, z1 - this.z);
        glVertex3d(x2 - this.x, y2 - this.y, z2 - this.z);
        return this;
    }

    public Renderer line(Vec3d pos1, Vec3d pos2)  {
        return this.line(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z);
    }

    public Renderer line(Vec3d pos1, Entity pos2)  {
        return this.line(pos1.x, pos1.y, pos1.z, pos2.posX, pos2.posY, pos2.posZ);
    }

    public Renderer line(Entity pos1, Entity pos2)  {
        return this.line(pos1.posX, pos1.posY, pos1.posZ, pos2.posX, pos2.posY, pos2.posZ);
    }
    
    public Renderer lineFromEyes(double x, double y, double z)  {
        glVertex3d(this.startX, this.startY, this.startZ);
        glVertex3d(x - this.x, y - this.y, z - this.z);
        return this;
    }

    public Renderer lineFromEyes(Vec3d pos)  {
        return this.lineFromEyes(pos.x, pos.y, pos.z);
    }

    public Renderer lineFromEyes(Entity entity, float partialTicks)  {
        if (partialTicks == 1.0F) {
            return this.lineFromEyes(entity.posX, entity.posY, entity.posZ);
        } else {
            double x = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
            double y = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks;
            double z = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
            return this.lineFromEyes(x, y, z);
        }
    }

    public Renderer lineFromEyes(Entity entity)  {
        return this.lineFromEyes(entity, this.partialTicks);
    }

    public Renderer outline(AxisAlignedBB bb)   {
        glVertex3d(bb.minX - this.x, bb.minY - this.y, bb.minZ - this.z);
        glVertex3d(bb.maxX - this.x, bb.minY - this.y, bb.minZ - this.z);

        glVertex3d(bb.maxX - this.x, bb.minY - this.y, bb.minZ - this.z);
        glVertex3d(bb.maxX - this.x, bb.minY - this.y, bb.maxZ - this.z);

        glVertex3d(bb.maxX - this.x, bb.minY - this.y, bb.maxZ - this.z);
        glVertex3d(bb.minX - this.x, bb.minY - this.y, bb.maxZ - this.z);

        glVertex3d(bb.minX - this.x, bb.minY - this.y, bb.maxZ - this.z);
        glVertex3d(bb.minX - this.x, bb.minY - this.y, bb.minZ - this.z);

        glVertex3d(bb.minX - this.x, bb.minY - this.y, bb.minZ - this.z);
        glVertex3d(bb.minX - this.x, bb.maxY - this.y, bb.minZ - this.z);

        glVertex3d(bb.maxX - this.x, bb.minY - this.y, bb.minZ - this.z);
        glVertex3d(bb.maxX - this.x, bb.maxY - this.y, bb.minZ - this.z);

        glVertex3d(bb.maxX - this.x, bb.minY - this.y, bb.maxZ - this.z);
        glVertex3d(bb.maxX - this.x, bb.maxY - this.y, bb.maxZ - this.z);

        glVertex3d(bb.minX - this.x, bb.minY - this.y, bb.maxZ - this.z);
        glVertex3d(bb.minX - this.x, bb.maxY - this.y, bb.maxZ - this.z);

        glVertex3d(bb.minX - this.x, bb.maxY - this.y, bb.minZ - this.z);
        glVertex3d(bb.maxX - this.x, bb.maxY - this.y, bb.minZ - this.z);

        glVertex3d(bb.maxX - this.x, bb.maxY - this.y, bb.minZ - this.z);
        glVertex3d(bb.maxX - this.x, bb.maxY - this.y, bb.maxZ - this.z);

        glVertex3d(bb.maxX - this.x, bb.maxY - this.y, bb.maxZ - this.z);
        glVertex3d(bb.minX - this.x, bb.maxY - this.y, bb.maxZ - this.z);

        glVertex3d(bb.minX - this.x, bb.maxY - this.y, bb.maxZ - this.z);
        glVertex3d(bb.minX - this.x, bb.maxY - this.y, bb.minZ - this.z);
        return this;
    }
}
