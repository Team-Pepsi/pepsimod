package net.daporkchop.pepsimod.mixin.client.gui;

import net.daporkchop.pepsimod.PepsiInjectMethods;
import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.Texture;
import net.daporkchop.pepsimod.util.colors.ColorizedText;
import net.daporkchop.pepsimod.util.colors.rainbow.RainbowText;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu extends GuiScreen {

    public final ColorizedText PEPSIMOD_TEXT_GRADIENT = PepsiUtils.getGradientFromStringThroughColor("PepsiMod 11.0 for Minecraft 1.11.2", new Color(255, 0, 0), new Color(0, 0, 255), new Color(255, 255, 255));
    public final ColorizedText PEPSIMOD_AUTHOR_GRADIENT = new RainbowText("Made by Team Pepsi's awesome developer team");
    @Shadow
    private String splashText;
    private Texture TITLE;

    @Inject(method = "initGui", at = @At("RETURN"))
    public void addPepsiIconAndChangeSplash(CallbackInfo ci) {
        TITLE = new Texture(new ResourceLocation("textures/gui/pepsimod.png"));
        this.splashText = "";
    }

    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindTexture(Lnet/minecraft/util/ResourceLocation;)V"))
    public void removeMenuLogoInit(TextureManager textureManager, ResourceLocation resource) {
        float titleX = width / 2 - 150, titleY = 20;
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        TITLE.render(titleX, titleY + 10, 300, 100);
    }

    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiMainMenu;drawTexturedModalRect(IIIIII)V"))
    public void removeMenuLogoRendering(GuiMainMenu guiMainMenu, int x, int y, int textureX, int textureY, int width, int height) {
    }

    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiMainMenu;drawString(Lnet/minecraft/client/gui/FontRenderer;Ljava/lang/String;III)V"))
    public void removeAllDrawStrings(GuiMainMenu guiMainMenu, FontRenderer fontRenderer1, String string, int i1, int i2, int i3) {
    }

    @Inject(method = "drawScreen", at = @At("RETURN"))
    public void addDrawPepsiStuff(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        this.drawString(this.fontRenderer, PepsiUtils.COLOR_ESCAPE + "cCopyright Mojang AB. Do not distribute!", this.width - this.fontRenderer.getStringWidth("Copyright Mojang AB. Do not distribute!") - 2, this.height - 10, -1);
        PepsiInjectMethods.drawPepsiStuffToMainMenu(mouseX, mouseY, partialTicks, this);
        PEPSIMOD_TEXT_GRADIENT.drawAtPos(this, 2, this.height - 20);
        PEPSIMOD_AUTHOR_GRADIENT.drawAtPos(this, 2, this.height - 10);
    }
}
