/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017 Team Pepsi
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from Team Pepsi.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: Team Pepsi), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.daporkchop.pepsimod.wdl.gui;

import net.daporkchop.pepsimod.wdl.WDL;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended.IGuiListEntry;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

import static org.lwjgl.opengl.GL11.*;

class LocalUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();

    private LocalUtils() {
        throw new AssertionError();
    }

    /**
     * Draws the given button (for multi-version compatability, this is needed for lists)
     *
     * @param button The button to draw.  Should already have been positioned.
     */
    public static void drawButton(GuiButton button, Minecraft mc, int mouseX, int mouseY) {
        button.drawButton(mc, mouseX, mouseY, 0 /* partialTicks */);
    }

    /**
     * Creates a new instance of {@link EntityPlayerSP}.
     */
    public static EntityPlayerSP makePlayer() {
        return new EntityPlayerSP(WDL.minecraft, WDL.worldClient,
                WDL.thePlayer.connection, WDL.thePlayer.getStatFileWriter(),
                WDL.thePlayer.getRecipeBook());
    }

    /**
     * Draws a dark background, similar to {@link GuiScreen#drawBackground(int)} but darker.
     * Same appearance as the background in lists.
     *
     * @param top    Where to start drawing (usually, 0)
     * @param left   Where to start drawing (usually, 0)
     * @param bottom Where to stop drawing (usually, height).
     * @param right  Where to stop drawing (usually, width)
     */
    public static void drawDarkBackground(int top, int left, int bottom, int right) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();

        Tessellator t = Tessellator.getInstance();
        BufferBuilder b = t.getBuffer();

        mc.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        float textureSize = 32.0F;
        b.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        b.pos(0, bottom, 0).tex(0 / textureSize,
                bottom / textureSize).color(32, 32, 32, 255).endVertex();
        b.pos(right, bottom, 0).tex(right / textureSize,
                bottom / textureSize).color(32, 32, 32, 255).endVertex();
        b.pos(right, top, 0).tex(right / textureSize,
                top / textureSize).color(32, 32, 32, 255).endVertex();
        b.pos(left, top, 0).tex(left / textureSize,
                top / textureSize).color(32, 32, 32, 255).endVertex();
        t.draw();
    }

    /**
     * Draws the top and bottom borders found on gui lists (but no background).
     * <br/>
     * Based off of
     * {@link net.minecraft.client.gui.GuiSlot#overlayBackground(int, int, int, int)}.
     * <p>
     * Note that there is an additional 4-pixel padding on the margins for the gradient.
     *
     * @param topMargin    Amount of space to give for the upper box.
     * @param bottomMargin Amount of space to give for the lower box.
     * @param top          Where to start drawing (usually, 0)
     * @param left         Where to start drawing (usually, 0)
     * @param bottom       Where to stop drawing (usually, height).
     * @param right        Where to stop drawing (usually, width)
     */
    public static void drawBorder(int topMargin, int bottomMargin, int top, int left, int bottom, int right) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        GlStateManager.disableDepth();
        byte padding = 4;

        mc.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        float textureSize = 32.0F;

        Tessellator t = Tessellator.getInstance();
        BufferBuilder b = t.getBuffer();

        //Box code is GuiSlot.overlayBackground
        //Upper box
        int upperBoxEnd = top + topMargin;

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        b.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        b.pos(left, upperBoxEnd, 0.0D).tex(0.0D, upperBoxEnd
                / textureSize).color(64, 64, 64, 255).endVertex();
        b.pos(right, upperBoxEnd, 0.0D).tex(right / textureSize,
                upperBoxEnd / textureSize).color(64, 64, 64, 255).endVertex();
        b.pos(right, top, 0.0D).tex(right / textureSize, top / textureSize)
                .color(64, 64, 64, 255).endVertex();
        b.pos(left, top, 0.0D).tex(0.0D, top / textureSize)
                .color(64, 64, 64, 255).endVertex();
        t.draw();

        // Lower box
        int lowerBoxStart = bottom - bottomMargin;

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        b.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        b.pos(left, bottom, 0.0D).tex(0.0D, bottom / textureSize)
                .color(64, 64, 64, 255).endVertex();
        b.pos(right, bottom, 0.0D).tex(right / textureSize, bottom
                / textureSize).color(64, 64, 64, 255).endVertex();
        b.pos(right, lowerBoxStart, 0.0D)
                .tex(right / textureSize, lowerBoxStart / textureSize)
                .color(64, 64, 64, 255).endVertex();
        b.pos(left, lowerBoxStart, 0.0D).tex(0.0D, lowerBoxStart
                / textureSize).color(64, 64, 64, 255).endVertex();
        t.draw();

        //Gradients
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA,
                GL_ONE_MINUS_SRC_ALPHA, 0, 1);
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(GL_SMOOTH);
        GlStateManager.disableTexture2D();
        b.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        b.pos(left, upperBoxEnd + padding, 0.0D).tex(0.0D, 1.0D)
                .color(0, 0, 0, 0).endVertex();
        b.pos(right, upperBoxEnd + padding, 0.0D).tex(1.0D, 1.0D)
                .color(0, 0, 0, 0).endVertex();
        b.pos(right, upperBoxEnd, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255)
                .endVertex();
        b.pos(left, upperBoxEnd, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255)
                .endVertex();
        t.draw();
        b.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        b.pos(left, lowerBoxStart, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255)
                .endVertex();
        b.pos(right, lowerBoxStart, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255)
                .endVertex();
        b.pos(right, lowerBoxStart - padding, 0.0D).tex(1.0D, 0.0D)
                .color(0, 0, 0, 0).endVertex();
        b.pos(left, lowerBoxStart - padding, 0.0D).tex(0.0D, 0.0D)
                .color(0, 0, 0, 0).endVertex();
        t.draw();

        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(GL_FLAT);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
    }
}

/**
 * Version-agnostic implementation of IGuiListEntry.
 */
abstract class GuiListEntry implements IGuiListEntry {
    @Override
    public void updatePosition(int p_192633_1_, int p_192633_2_,
                               int p_192633_3_, float p_192633_4_) {
        setSelected(p_192633_1_, p_192633_2_, p_192633_3_);
    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth,
                          int slotHeight, int mouseX, int mouseY, boolean isSelected,
                          float partialTicks) {
        drawEntry(slotIndex, x, y, listWidth, slotHeight, mouseX, mouseY, isSelected);
    }

    public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {
    }

    public abstract void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected);

    @Override
    public abstract boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY);

    @Override
    public abstract void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY);
}

/**
 * Extendable button, to deal with changing method names between versions
 *
 * @author Pokechu22
 */
abstract class ExtButton extends GuiButton {
    public ExtButton(int buttonId, int x, int y, int widthIn, int heightIn,
                     String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
    }

    public abstract void beforeDraw();

    public abstract void afterDraw();

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY,
                           float partialTicks) {
        beforeDraw();
        super.drawButton(mc, mouseX, mouseY, partialTicks);
        afterDraw();
    }
}
