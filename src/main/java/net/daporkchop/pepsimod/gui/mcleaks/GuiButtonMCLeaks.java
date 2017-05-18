package net.daporkchop.pepsimod.gui.mcleaks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonMCLeaks extends GuiButton {
    private ResourceLocation location;

    public GuiButtonMCLeaks(int buttonId, int x, int y, int widthIn, int heightIn) {
        super(buttonId, x, y, widthIn, heightIn, "");
        this.width = 20;
        this.height = 20;
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = widthIn;
        this.height = heightIn;
        this.location = new ResourceLocation("textures/gui/pepsibuttons.png");
    }


    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            mc.getTextureManager().bindTexture(location);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int k = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, (hovered ? 20 : 0), this.width, this.height);
            this.drawTexturedModalRect(this.xPosition + this.width, this.yPosition, 200 - this.width, k * 20, this.width, this.height);
            this.mouseDragged(mc, mouseX, mouseY);
        }
    }
}
