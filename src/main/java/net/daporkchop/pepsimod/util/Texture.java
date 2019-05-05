/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2019 DaPorkchop_
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

package net.daporkchop.pepsimod.util;

import net.daporkchop.lib.unsafe.PCleaner;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static org.lwjgl.opengl.GL11.GL_QUADS;

/**
 * Simple texture class, originally loaded a bufferedimage from file and stored a texture id like you would expect, but I decided to take advantage of Minecraft's resource shiz.
 * There's a slight chance that this is skidded from Huzuni.
 * kek
 */
public class Texture extends PepsiConstants implements AutoCloseable {
    public final ResourceLocation texture;
    protected final PCleaner cleaner;

    public Texture(InputStream in) throws IOException {
        this(ImageIO.read(in));
    }

    public Texture(BufferedImage img) {
        this(mc.getTextureManager().getDynamicTextureLocation(UUID.randomUUID().toString(), new DynamicTexture(img)), true);
    }

    public Texture(ResourceLocation texture) {
        this(texture, false);
    }

    public Texture(ResourceLocation texture, boolean clean) {
        this.texture = texture;
        this.cleaner = clean ? PCleaner.cleaner(this, () -> mc.getTextureManager().deleteTexture(texture)) : null;
    }

    public void render(float x, float y, float width, float height) {
        this.bindTexture();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder renderer = tessellator.getBuffer();
        renderer.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        renderer.pos(x, y + height, 0F).tex(0, 1).endVertex();
        renderer.pos(x + width, y + height, 0F).tex(1, 1).endVertex();
        renderer.pos(x + width, y, 0F).tex(1, 0).endVertex();
        renderer.pos(x, y, 0F).tex(0, 0).endVertex();
        tessellator.draw();
    }

    public void bindTexture() {
        mc.getTextureManager().bindTexture(this.texture);
        GlStateManager.enableTexture2D();
    }

    @Override
    public void close() {
        if (this.cleaner != null) {
            this.cleaner.clean();
        }
    }

    @Override
    public String toString() {
        return this.texture.getPath();
    }
}
