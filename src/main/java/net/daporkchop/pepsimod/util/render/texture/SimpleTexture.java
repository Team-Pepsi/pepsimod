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
@Accessors(fluent = true)
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
