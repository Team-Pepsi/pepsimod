package net.daporkchop.pepsimod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;

/**
 * Simple texture class, originally loaded a bufferedimage from file and stored a texture id like you would expect, but I decided to take advantage of Minecraft's resource shiz.
 * There's a slight chance that this is skidded from Huzuni.
 * kek
 */
public class Texture {

    private final ResourceLocation texture;

    public Texture(ResourceLocation textureURL) {
        texture = textureURL;
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
    }

    public void render(float x, float y, float width, float height) {
        render(x, y, width, height, 0F, 0F, 1F, 1F);
    }

    public void render(float x, float y, float width, float height, float u, float v, float t, float s) {
        bindTexture();
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer renderer = tessellator.getBuffer();
        renderer.begin(GL_TRIANGLES, DefaultVertexFormats.POSITION_TEX);
        renderer.pos(x + width, y, 0F).tex(t, v).endVertex();
        renderer.pos(x, y, 0F).tex(u, v).endVertex();
        renderer.pos(x, y + height, 0F).tex(u, s).endVertex();
        renderer.pos(x, y + height, 0F).tex(u, s).endVertex();
        renderer.pos(x + width, y + height, 0F).tex(t, s).endVertex();
        renderer.pos(x + width, y, 0F).tex(t, v).endVertex();
        tessellator.draw();
    }

    public void bindTexture() {
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        GlStateManager.enableTexture2D();
    }

    @Override
    public String toString() {
        return texture.getResourcePath();
    }
}
