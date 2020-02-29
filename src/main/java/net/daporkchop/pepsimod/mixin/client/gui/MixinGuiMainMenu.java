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

package net.daporkchop.pepsimod.mixin.client.gui;

import net.daporkchop.pepsimod.Pepsimod;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.colors.ColorizedText;
import net.daporkchop.pepsimod.util.colors.rainbow.RainbowText;
import net.daporkchop.pepsimod.util.config.impl.GeneralTranslator;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

import static net.daporkchop.pepsimod.util.PepsiConstants.mcStartedSuccessfully;
import static net.daporkchop.pepsimod.util.PepsiConstants.pepsimod;

@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu extends GuiScreen {
    public ColorizedText PEPSIMOD_TEXT_GRADIENT;
    public ColorizedText PEPSIMOD_AUTHOR_GRADIENT;
    @Shadow
    private String splashText;

    @Inject(
            method = "Lnet/minecraft/client/gui/GuiMainMenu;<init>()V",
            at = @At("RETURN")
    )
    public void postConstructor(CallbackInfo callbackInfo)  {
        if (mcStartedSuccessfully) {
            this.PEPSIMOD_TEXT_GRADIENT = PepsiUtils.getGradientFromStringThroughColor(Pepsimod.NAME_VERSION + " for Minecraft " + MinecraftForge.MC_VERSION, new Color(255, 0, 0), new Color(0, 0, 255), new Color(255, 255, 255));
            this.PEPSIMOD_AUTHOR_GRADIENT = new RainbowText("Made by DaPorkchop_");

            this.splashText = pepsimod.data.mainMenu.getRandomSplash();
        }
    }

    @Inject(
            method = "Lnet/minecraft/client/gui/GuiMainMenu;initGui()V",
            at = @At("RETURN")
    )
    public void addPepsiIconAndChangeSplash(CallbackInfo ci) {
        if (mcStartedSuccessfully) {
            pepsimod.isInitialized = true;
            if (!pepsimod.hasInitializedModules) {
                for (Module module : ModuleManager.AVALIBLE_MODULES) {
                    module.doInit();
                }
                PepsiUtils.setBlockIdFields();
                pepsimod.hasInitializedModules = true;
            }
            ModuleManager.sortModules(GeneralTranslator.INSTANCE.sortType);
        }
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
                    target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindTexture(Lnet/minecraft/util/ResourceLocation;)V",
                    ordinal = 0
            ))
    public void removeMenuLogoInit(TextureManager textureManager, ResourceLocation resource) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        pepsimod.data.mainMenu.banner.render(this.width / 2 - 150, 10, 300, 100);
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

    @Inject(
            method = "Lnet/minecraft/client/gui/GuiMainMenu;drawScreen(IIF)V",
            at = @At("RETURN")
    )
    public void addDrawPepsiStuff(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        this.drawString(this.fontRenderer, PepsiUtils.COLOR_ESCAPE + "cCopyright Mojang AB. Do not distribute!", this.width - this.fontRenderer.getStringWidth("Copyright Mojang AB. Do not distribute!") - 2, this.height - 10, -1);
        if (this.PEPSIMOD_TEXT_GRADIENT != null && this.PEPSIMOD_AUTHOR_GRADIENT != null)    {
            this.PEPSIMOD_TEXT_GRADIENT.drawAtPos(this, 2, this.height - 20);
            this.PEPSIMOD_AUTHOR_GRADIENT.drawAtPos(this, 2, this.height - 10);
        }
    }
}
