package net.daporkchop.pepsimod.mixin.client.gui;

import net.daporkchop.pepsimod.module.impl.render.NoOverlayMod;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class MixinGuiIngame extends Gui {
    @Inject(method = "renderPumpkinOverlay", at = @At("HEAD"), cancellable = true)
    protected void preRenderPumpkinOverlay(ScaledResolution scaledRes, CallbackInfo callbackInfo) {
        if (NoOverlayMod.INSTANCE.isEnabled) {
            callbackInfo.cancel();
        }
    }
}
