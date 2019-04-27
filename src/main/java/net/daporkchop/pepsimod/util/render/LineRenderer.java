package net.daporkchop.pepsimod.util.render;

import net.daporkchop.lib.math.vector.f.Vec3f;
import net.daporkchop.pepsimod.util.RenderColor;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

import static org.lwjgl.opengl.GL11.*;

/**
 * Helps with drawing lines in the world.
 * 
 * @author DaPorkchop_
 */
public class LineRenderer implements AutoCloseable {
    protected final double startX;
    protected final double startY;
    protected final double startZ;

    protected final double x;
    protected final double y;
    protected final double z;

    protected final float partialTicks;

    public LineRenderer(double startX, double startY, double startZ, double x, double y, double z, float partialTicks) {
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.x = x;
        this.y = y;
        this.z = z;
        this.partialTicks = partialTicks;

        glEnable(GL11.GL_BLEND);
        glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL11.GL_LINE_SMOOTH);
        glLineWidth(2.0f);
        glDisable(GL11.GL_TEXTURE_2D);
        glEnable(GL11.GL_CULL_FACE);
        glDisable(GL11.GL_DEPTH_TEST);

        GL11.glBegin(GL11.GL_LINES);
    }

    public LineRenderer(Vec3d start, double x, double y, double z, float partialTicks) {
        this(start.x, start.y, start.z, x, y, z, partialTicks);
    }

    public LineRenderer(Vec3d start, Vec3d pos, float partialTicks) {
        this(start.x, start.y, start.z, pos.x, pos.y, pos.z, partialTicks);
    }

    public LineRenderer color(float r, float g, float b) {
        glColor4f(r, g, b, 1.0f);
        return this;
    }

    public LineRenderer color(float r, float g, float b, float a) {
        glColor4f(r, g, b, a);
        return this;
    }

    public LineRenderer color(int r, int g, int b) {
        glColor4f(r * 0.003921569f, g * 0.003921569f, b * 0.003921569f, 1.0f);
        return this;
    }

    public LineRenderer color(int r, int g, int b, int a) {
        glColor4f(r * 0.003921569f, g * 0.003921569f, b * 0.003921569f, a * 0.003921569f);
        return this;
    }

    public LineRenderer color(RenderColor color)    {
        glColor4b(color.r, color.g, color.b, color.a);
        return this;
    }

    public LineRenderer color(Color color)    {
        glColor4f(color.getRed() * 0.003921569f, color.getGreen() * 0.003921569f, color.getBlue() * 0.003921569f, color.getAlpha() * 0.003921569f);
        return this;
    }

    public LineRenderer width(float width)  {
        glEnd();
        glLineWidth(width);
        glBegin(GL11.GL_LINES);
        return this;
    }

    public LineRenderer line(double x1, double y1, double z1, double x2, double y2, double z2)  {
        glVertex3d(x1 - this.x, y1 - this.y, z1 - this.z);
        glVertex3d(x2 - this.x, y2 - this.y, z2 - this.z);
        return this;
    }

    public LineRenderer line(float x1, float y1, float z1, float x2, float y2, float z2)  {
        glVertex3d(x1 - this.x, y1 - this.y, z1 - this.z);
        glVertex3d(x2 - this.x, y2 - this.y, z2 - this.z);
        return this;
    }

    public LineRenderer line(int x1, int y1, int z1, int x2, int y2, int z2)  {
        glVertex3d(x1 - this.x, y1 - this.y, z1 - this.z);
        glVertex3d(x2 - this.x, y2 - this.y, z2 - this.z);
        return this;
    }

    public LineRenderer line(Vec3d pos1, Vec3d pos2)  {
        return this.line(pos1.x, pos1.y, pos1.z, pos2.x, pos2.y, pos2.z);
    }

    public LineRenderer line(Vec3d pos1, Entity pos2)  {
        return this.line(pos1.x, pos1.y, pos1.z, pos2.posX, pos2.posY, pos2.posZ);
    }

    public LineRenderer line(Entity pos1, Entity pos2)  {
        return this.line(pos1.posX, pos1.posY, pos1.posZ, pos2.posX, pos2.posY, pos2.posZ);
    }
    
    public LineRenderer lineFromEyes(double x, double y, double z)  {
        glVertex3d(this.startX, this.startY, this.startZ);
        glVertex3d(x - this.x, y - this.y, z - this.z);
        return this;
    }

    public LineRenderer lineFromEyes(Vec3d pos)  {
        return this.lineFromEyes(pos.x, pos.y, pos.z);
    }

    public LineRenderer lineFromEyes(Entity entity, float partialTicks)  {
        if (partialTicks == 1.0F) {
            return this.lineFromEyes(entity.posX, entity.posY, entity.posZ);
        } else {
            double x = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
            double y = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks;
            double z = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
            return this.lineFromEyes(x, y, z);
        }
    }

    public LineRenderer lineFromEyes(Entity entity)  {
        return this.lineFromEyes(entity, this.partialTicks);
    }

    @Override
    public void close() {
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        glEnd();

        glEnable(GL11.GL_DEPTH_TEST);
        glEnable(GL11.GL_TEXTURE_2D);
        glDisable(GL11.GL_CULL_FACE);
        glDisable(GL11.GL_LINE_SMOOTH);
        glDisable(GL11.GL_BLEND);
    }
}
