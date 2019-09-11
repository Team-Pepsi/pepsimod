/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2016-2019 DaPorkchop_
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

package net.daporkchop.pepsimod.asm.core.minecraft.client.gui;

import net.daporkchop.pepsimod.asm.PepsimodMixinLoader;
import net.daporkchop.pepsimod.util.render.text.RainbowTextRenderer;
import net.daporkchop.pepsimod.util.render.texture.Texture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static net.daporkchop.pepsimod.util.PepsiUtil.*;

/**
 * This makes lots of changes to the main menu, most notably replacing the Minecraft banner with the pepsimod logo and changing lots of text.
 *
 * @author DaPorkchop_
 */
@Mixin(GuiMainMenu.class)
abstract class MixinGuiMainMenu extends GuiScreen {
    protected String[] versionText;
    private   int      scaledBannerHeight;

    @Shadow
    private String splashText;

    @Inject(
            method = "Lnet/minecraft/client/gui/GuiMainMenu;<init>()V",
            at = @At("RETURN"))
    private void setSplashText(CallbackInfo ci) {
        this.versionText = new String[]{
                "pepsimod ", VERSION_FULL, null,
                "Made by DaPorkchop_"
        };

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
    private void preventSettingSplashInInitGui(GuiMainMenu menu, String val) {
    }

    @Redirect(
            method = "Lnet/minecraft/client/gui/GuiMainMenu;drawScreen(IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/client/ForgeHooksClient;renderMainMenu(Lnet/minecraft/client/gui/GuiMainMenu;Lnet/minecraft/client/gui/FontRenderer;IILjava/lang/String;)Ljava/lang/String;"
            ))
    private String skipForgeDrawMainMenu(GuiMainMenu gui, FontRenderer font, int width, int height, String splashText) {
        return this.splashText;
    }

    @Redirect(
            method = "Lnet/minecraft/client/gui/GuiMainMenu;drawScreen(IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiMainMenu;drawTexturedModalRect(IIIIII)V"
            ))
    private void removeMenuLogoRendering(GuiMainMenu guiMainMenu, int x, int y, int textureX, int textureY, int width, int height) {
    }

    @Redirect(
            method = "Lnet/minecraft/client/gui/GuiMainMenu;drawScreen(IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiMainMenu;drawModalRectWithCustomSizedTexture(IIFFIIFF)V"
            ))
    private void removeSubLogoRenderingAndDrawBanner(int x, int y, float a, float b, int c, int d, float e, float f) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        Texture banner = pepsimod.resources().mainMenu().banner();
        this.scaledBannerHeight = (int) (banner.height() * (300.0f / banner.width()));
        banner.draw(this.width / 2 - 150, (this.height / 4 + 48 - this.scaledBannerHeight) / 2, 300, this.scaledBannerHeight);
    }

    @Redirect(
            method = "Lnet/minecraft/client/gui/GuiMainMenu;drawScreen(IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiMainMenu;drawString(Lnet/minecraft/client/gui/FontRenderer;Ljava/lang/String;III)V"
            ))
    private void removeAllDrawStrings(GuiMainMenu guiMainMenu, FontRenderer fontRenderer1, String string, int i1, int i2, int i3) {
    }

    @Redirect(
            method = "Lnet/minecraft/client/gui/GuiMainMenu;drawScreen(IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiMainMenu;drawRect(IIIII)V",
                    ordinal = 0
            ))
    private void skipDrawCopyrightUnderline(int left, int top, int right, int bottom, int color) {
    }

    /**
     * Moves the splash text to a position that lines up better with the pepsimod logo.
     */
    @Redirect(
            method = "Lnet/minecraft/client/gui/GuiMainMenu;drawScreen(IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V"
            ))
    private void moveSplashText(float x, float y, float z) {
        GlStateManager.translate(this.width / 2 + 300.0f / 2.0f, this.height / 4 + this.scaledBannerHeight * 0.5f * 0.0f, 0.0f);
    }

    /**
     * Draws some text on the screen lol
     */
    @Inject(
            method = "Lnet/minecraft/client/gui/GuiMainMenu;drawScreen(IIF)V",
            at = @At("TAIL")
    )
    private void addDrawPepsiStuff(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        ((RainbowTextRenderer) TEXT_RENDERER).scale(0.003f);
        TEXT_RENDERER
                .renderLinesSmart(this.versionText, 2, this.height - 10 * 2)
                .render("Copyright Mojang AB. Do not distribute!", this.width - this.fontRenderer.getStringWidth("Copyright Mojang AB. Do not distribute!") - 2, this.height - 10);
    }

    @Redirect(
            method = "Lnet/minecraft/client/gui/GuiMainMenu;drawScreen(IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/fml/common/FMLCommonHandler;getBrandings(Z)Ljava/util/List;"
            ))
    private List<String> skipObtainingForgeBrandingList(FMLCommonHandler handler, boolean includeMC) {
        return Collections.emptyList();
    }

    @Redirect(
            method = "Lnet/minecraft/client/gui/GuiMainMenu;drawScreen(IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/google/common/collect/Lists;reverse(Ljava/util/List;)Ljava/util/List;"
            ))
    private List<String> skipReverseForgeBrandingList(List<String> list) {
        return list;
    }

    @Redirect(
            method = "Lnet/minecraft/client/gui/GuiMainMenu;actionPerformed(Lnet/minecraft/client/gui/GuiButton;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V",
                    ordinal = 1
            ),
            require = 0)
    private void debug_reloadRainbowTextOnLanguageButton(Minecraft mc, GuiScreen screen) {
        if (PepsimodMixinLoader.OBFUSCATED) {
            mc.displayGuiScreen(screen);
        } else {
            ((RainbowTextRenderer) TEXT_RENDERER).reloadShader();
        }
    }
}
