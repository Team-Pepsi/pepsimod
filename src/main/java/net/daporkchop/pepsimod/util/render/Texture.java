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

import net.daporkchop.lib.unsafe.PCleaner;
import net.daporkchop.pepsimod.util.PepsiConstants;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.lwjgl.opengl.GL11.GL_QUADS;

/**
 * Simple texture class, originally loaded a bufferedimage from file and stored a texture id like you would expect, but I decided to take advantage of Minecraft's resource shiz.
 * There's a slight chance that this is skidded from Huzuni.
 * kek
 */
public class Texture extends PepsiConstants implements AutoCloseable {
    protected static DynamicTexture loadTexture(BufferedImage image)  {
        try {
            return new DynamicTexture(image);
        } catch (RuntimeException e)    {
            if ("No OpenGL context found in the current thread.".equalsIgnoreCase(e.getMessage()))  {
                //load async
                try {
                    return mc.addScheduledTask(() -> new DynamicTexture(image)).get();
                } catch (InterruptedException | ExecutionException e1)   {
                    throw new RuntimeException(e1);
                }
            } else {
                throw e;
            }
        }
    }

    public final ResourceLocation texture;
    protected final PCleaner cleaner;

    public Texture(byte[] in) throws IOException {
        this(new ByteArrayInputStream(in));
    }

    public Texture(InputStream in) throws IOException {
        this(ImageIO.read(in));
    }

    public Texture(BufferedImage img) {
        this(mc.getTextureManager().getDynamicTextureLocation(UUID.randomUUID().toString(), loadTexture(img)), true);
    }

    public Texture(ResourceLocation texture) {
        this(texture, false);
    }

    public Texture(ResourceLocation texture, boolean clean) {
        this.texture = texture;
        this.cleaner = clean ? PCleaner.cleaner(this, () -> mc.addScheduledTask(() -> mc.getTextureManager().deleteTexture(texture))) : null;
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
