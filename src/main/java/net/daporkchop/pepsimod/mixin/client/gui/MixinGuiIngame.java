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

import net.daporkchop.pepsimod.module.impl.render.NoOverlayMod;
import net.daporkchop.pepsimod.util.config.impl.HUDTranslator;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public abstract class MixinGuiIngame extends Gui {
    @Inject(
            method = "Lnet/minecraft/client/gui/GuiIngame;renderPumpkinOverlay(Lnet/minecraft/client/gui/ScaledResolution;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    protected void preRenderPumpkinOverlay(ScaledResolution scaledRes, CallbackInfo callbackInfo) {
        if (NoOverlayMod.INSTANCE.state.enabled) {
            callbackInfo.cancel();
        }
    }

    @Inject(
            method = "Lnet/minecraft/client/gui/GuiIngame;renderPotionEffects(Lnet/minecraft/client/gui/ScaledResolution;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void prerenderPotionEffects(ScaledResolution resolution, CallbackInfo callbackInfo) {
        if (!HUDTranslator.INSTANCE.effects) {
            callbackInfo.cancel();
        }
    }
}
