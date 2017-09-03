package net.daporkchop.pepsimod.gui.misc;

import net.daporkchop.pepsimod.util.ImageUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonTooBeeTooTee extends GuiButton {
    private ResourceLocation location;

    public GuiButtonTooBeeTooTee(int buttonId, int x, int y, int widthIn, int heightIn) {
        super(buttonId, x, y, widthIn, heightIn, "");
        this.width = 20;
        this.height = 20;
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.x = x;
        this.y = y;
        this.width = widthIn;
        this.height = heightIn;
        this.location = null;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (location == null) {
            location = ImageUtils.imgs.get(0);
        }
        if (this.visible) {
            mc.getTextureManager().bindTexture(location);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int k = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            this.drawTexturedModalRect(this.x, this.y, 20, (hovered ? 20 : 0), this.width, this.height);
            this.drawTexturedModalRect(this.x + this.width, this.y, 200 - this.width, k * 20, this.width, this.height);
            this.mouseDragged(mc, mouseX, mouseY);
        }
    }
}
