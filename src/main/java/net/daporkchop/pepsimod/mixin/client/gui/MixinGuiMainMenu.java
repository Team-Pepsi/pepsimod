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

package net.daporkchop.pepsimod.mixin.client.gui;

import net.daporkchop.pepsimod.util.render.Rainbow;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.ThreadLocalRandom;

import static net.daporkchop.pepsimod.util.PepsiUtil.*;

/**
 * @author DaPorkchop_
 */
@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu extends GuiScreen {
    @Shadow
    private String splashText;

    @Inject(
            method = "Lnet/minecraft/client/gui/GuiMainMenu;initGui()V",
            at = @At("TAIL"))
    private void setSplashText(CallbackInfo ci) {
        this.splashText = String.format(
                "§%c%s",
                RANDOM_COLORS[ThreadLocalRandom.current().nextInt(RANDOM_COLORS.length)],
                pepsimod.resources().mainMenu().randomSplash()
        );
    }

    @Redirect(
            method = "Lnet/minecraft/client/gui/GuiMainMenu;initGui()V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/gui/GuiMainMenu;splashText:Ljava/lang/String;",
                    opcode = Opcodes.PUTFIELD
            ))
    public void preventSettingSplashInInitGui(GuiMainMenu menu, String val) {
    }

    @Redirect(
            method = "Lnet/minecraft/client/gui/GuiMainMenu;drawScreen(IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/client/ForgeHooksClient;renderMainMenu(Lnet/minecraft/client/gui/GuiMainMenu;Lnet/minecraft/client/gui/FontRenderer;IILjava/lang/String;)Ljava/lang/String;"
            ))
    public String skipForgeDrawMainMenu(GuiMainMenu gui, FontRenderer font, int width, int height, String splashText) {
        return null;
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

    @Redirect(
            method = "Lnet/minecraft/client/gui/GuiMainMenu;drawScreen(IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiMainMenu;drawString(Lnet/minecraft/client/gui/FontRenderer;Ljava/lang/String;III)V"
            ))
    public void removeAllDrawStrings(GuiMainMenu guiMainMenu, FontRenderer fontRenderer1, String string, int i1, int i2, int i3) {
    }

    @Redirect(
            method = "Lnet/minecraft/client/gui/GuiMainMenu;drawScreen(IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiMainMenu;drawRect(IIIII)V",
                    ordinal = 0
            ))
    public void skipDrawCopyrightUnderline(int left, int top, int right, int bottom, int color) {
    }

    @Redirect(
            method = "Lnet/minecraft/client/gui/GuiMainMenu;drawScreen(IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindTexture(Lnet/minecraft/util/ResourceLocation;)V",
                    ordinal = 0
            ))
    public void drawBanner(TextureManager textureManager, ResourceLocation resource) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        pepsimod.resources().mainMenu().banner().draw(this.width / 2 - 150, 50, 300);
    }

    @Inject(
            method = "Lnet/minecraft/client/gui/GuiMainMenu;drawScreen(IIF)V",
            at = @At("RETURN")
    )
    public void addDrawPepsiStuff(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        RAINBOW.update()
                .renderString("Hello World!", 2, this.height - 10 * 4)
                .renderString("Lorem Ipsum dolor set amit", 2, this.height - 10 * 3)
                .renderString("Other placeholder text", 2, this.height - 10 * 2)
                .renderString("Made by DaPorkchop_", 2, this.height - 10)
                .renderString("Copyright Mojang AB. Do not distribute!", this.width - this.fontRenderer.getStringWidth("Copyright Mojang AB. Do not distribute!") - 2, this.height - 10);
    }
}
