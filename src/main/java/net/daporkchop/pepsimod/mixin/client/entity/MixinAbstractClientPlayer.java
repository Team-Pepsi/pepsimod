package net.daporkchop.pepsimod.mixin.client.entity;

import net.daporkchop.pepsimod.module.impl.misc.FreecamMod;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public abstract class MixinAbstractClientPlayer extends EntityPlayer {
    public MixinAbstractClientPlayer() {
        super(null, null);
    }

    @Inject(method = "isSpectator", at = @At("HEAD"), cancellable = true)
    public void preIsSpectator(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (FreecamMod.INSTANCE.isEnabled) {
            callbackInfoReturnable.setReturnValue(true);
            callbackInfoReturnable.cancel();
        }
    }
}
