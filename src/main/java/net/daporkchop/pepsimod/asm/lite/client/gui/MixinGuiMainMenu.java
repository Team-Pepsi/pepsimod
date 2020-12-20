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

package net.daporkchop.pepsimod.asm.lite.client.gui;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.daporkchop.pepsimod.Lite.*;

/**
 * @author DaPorkchop_
 */
@Mixin(GuiMainMenu.class)
abstract class MixinGuiMainMenu extends GuiScreen {
    @Shadow
    private String splashText;

    @Inject(
            method = "Lnet/minecraft/client/gui/GuiMainMenu;<init>()V",
            at = @At("RETURN")
    )
    public void postConstructor(CallbackInfo callbackInfo)  {
            this.splashText = DATA.mainMenu.getRandomSplash();
    }

    @Redirect(
            method = "Lnet/minecraft/client/gui/GuiMainMenu;drawScreen(IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindTexture(Lnet/minecraft/util/ResourceLocation;)V",
                    ordinal = 0
            ))
    public void removeMenuLogoInit(TextureManager textureManager, ResourceLocation resource) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        DATA.mainMenu.banner.render(this.width / 2 - 150, 10, 300, 100);
    }

    @Redirect(
            method = "Lnet/minecraft/client/gui/GuiMainMenu;initGui()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/GuiMainMenu;splashText:Ljava/lang/String;",
                    opcode = Opcodes.PUTFIELD
            ))
    public void preventSettingSplashInInitGui(GuiMainMenu menu, String val)   {
    }

    @Redirect(
            method = "Lnet/minecraft/client/gui/GuiMainMenu;drawScreen(IIF)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/GuiMainMenu;splashText:Ljava/lang/String;",
                    opcode = Opcodes.PUTFIELD
            ))
    public void preventSettingSplashInDrawScreen(GuiMainMenu menu, String val)   {
    }

    @Redirect(
            method = "Lnet/minecraft/client/gui/GuiMainMenu;drawScreen(IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiMainMenu;drawTexturedModalRect(IIIIII)V"
            ))
    public void removeMenuLogoRendering(GuiMainMenu guiMainMenu, int x, int y, int textureX, int textureY, int width, int height) {
    }

    @Redirect(
            method = "Lnet/minecraft/client/gui/GuiMainMenu;drawScreen(IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiMainMenu;drawModalRectWithCustomSizedTexture(IIFFIIFF)V"
            ))
    public void removeSubLogoRendering(int x, int y, float a, float b, int c, int d, float e, float f) {
    }
}
