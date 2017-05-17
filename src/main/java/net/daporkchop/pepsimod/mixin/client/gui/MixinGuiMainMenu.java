package net.daporkchop.pepsimod.mixin.client.gui;

import net.daporkchop.pepsimod.PepsiInjectMethods;
import net.daporkchop.pepsimod.util.Texture;
import net.minecraft.client.gui.FontRenderer;
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

@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu extends GuiScreen {

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
    public void addDrawPepsiStuff(int mouseX, int mouseY, float partialTicks, CallbackInfo ci)  {
        PepsiInjectMethods.drawPepsiStuffToMainMenu(mouseX, mouseY, partialTicks, this);
    }
}
