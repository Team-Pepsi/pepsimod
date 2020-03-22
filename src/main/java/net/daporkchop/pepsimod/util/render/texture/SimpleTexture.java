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

package net.daporkchop.pepsimod.util.render.texture;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import net.daporkchop.lib.unsafe.PCleaner;
import net.daporkchop.pepsimod.util.PepsiConstants;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.lwjgl.opengl.GL11.GL_QUADS;

/**
 * @author DaPorkchop_
 */
@Getter
public final class SimpleTexture implements PepsiConstants, Texture {
    @Getter(AccessLevel.NONE)
    protected final ResourceLocation location;
    @Getter(AccessLevel.NONE)
    protected final PCleaner         cleaner;

    protected final int width;
    protected final int height;

    public SimpleTexture(@NonNull BufferedImage img) {
        DynamicTexture tex;
        try {
            tex = new DynamicTexture(img);
        } catch (RuntimeException e) {
            if ("No OpenGL context found in the current thread.".equalsIgnoreCase(e.getMessage())) {
                //load async
                try {
                    tex = mc.addScheduledTask(() -> new DynamicTexture(img)).get();
                } catch (InterruptedException | ExecutionException e1) {
                    throw new RuntimeException(e1);
                }
            } else {
                throw e;
            }
        }
        ResourceLocation location = this.location = mc.getTextureManager().getDynamicTextureLocation(UUID.randomUUID().toString(), tex);
        this.cleaner = PCleaner.cleaner(this, () -> mc.addScheduledTask(() -> mc.getTextureManager().deleteTexture(location)));

        this.width = img.getWidth();
        this.height = img.getHeight();
    }

    @Override
    public void draw(int x, int y, int width, int height) {
        mc.getTextureManager().bindTexture(this.location);
        GlStateManager.enableTexture2D();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + height, 0.0d).tex(0.0d, 1.0d).endVertex();
        buffer.pos(x + width, y + height, 0.0d).tex(1.0d, 1.0d).endVertex();
        buffer.pos(x + width, y, 0.0d).tex(1.0d, 0.0d).endVertex();
        buffer.pos(x, y, 0.0d).tex(0.0d, 0.0d).endVertex();
        tessellator.draw();
    }

    @Override
    public void close() {
        this.cleaner.clean();
    }
}
