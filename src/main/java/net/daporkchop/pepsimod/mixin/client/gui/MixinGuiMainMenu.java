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

package net.daporkchop.pepsimod.mixin.client.gui;

import net.daporkchop.pepsimod.PepsiInjectMethods;
import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.util.ImageUtils;
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
    public final ColorizedText PEPSIMOD_TEXT_GRADIENT = PepsiUtils.getGradientFromStringThroughColor("PepsiMod 11.0 for Minecraft 1.12.1", new Color(255, 0, 0), new Color(0, 0, 255), new Color(255, 255, 255));
    public final ColorizedText PEPSIMOD_AUTHOR_GRADIENT = new RainbowText("Made by Team Pepsi's awesome developer team");
    @Shadow
    private String splashText;
    private Texture TITLE;

    @Inject(method = "initGui", at = @At("RETURN"))
    public void addPepsiIconAndChangeSplash(CallbackInfo ci) {
        PepsiMod.INSTANCE.isInitialized = true;
        if (!PepsiMod.INSTANCE.hasInitializedModules) {
            for (Module module : ModuleManager.AVALIBLE_MODULES) {
                module.doInit();
            }
            PepsiUtils.setBlockIdFields();
            PepsiMod.INSTANCE.hasInitializedModules = true;
        }
        TITLE = new Texture(ImageUtils.imgs.get(1));
        this.splashText = PepsiUtils.COLOR_ESCAPE + "c" + PepsiUtils.COLOR_ESCAPE + "lpepsi" + PepsiUtils.COLOR_ESCAPE + "9" + PepsiUtils.COLOR_ESCAPE + "lmod";
        ModuleManager.sortModules(ModuleManager.sortType);
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

    @Inject(method = "addSingleplayerMultiplayerButtons", at = @At("RETURN"))
    public void preAddSingleplayerMultiplayerButtons(int i, int j, CallbackInfo callbackInfo)   {
        this.buttonList.add(new GuiButton(12345, this.width / 2 + 102, i + j * 1, 98, 20, "Password"));
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
    public void preActionPerformed(GuiButton button, CallbackInfo callbackInfo) {
        if (button.id == 12345) {
            try {
                Class.forName("team.pepsi.pepsimod.launcher.PepsiModServerManager").getDeclaredMethod("setPassword").invoke(null);
            } catch (Exception e)   {
                e.printStackTrace();
            }
            callbackInfo.cancel();
        }
    }
}
