package net.daporkchop.pepsimod.mixin.client.renderer;

import net.daporkchop.pepsimod.module.impl.render.NoOverlayMod;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class MixinItemRenderer {
    @Inject(method = "renderWaterOverlayTexture", at = @At("HEAD"), cancellable = true)
    public void preRenderWaterOverlayTexture(float partialTicks, CallbackInfo callbackInfo) {
        if (NoOverlayMod.INSTANCE.isEnabled) {
            callbackInfo.cancel();
        }
    }

    @Inject(method = "renderFireInFirstPerson", at = @At("HEAD"), cancellable = true)
    public void preRenderFireInFirstPerson(CallbackInfo callbackInfo) {
        if (NoOverlayMod.INSTANCE.isEnabled) {
            callbackInfo.cancel();
        }
    }
}
