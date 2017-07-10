package net.daporkchop.pepsimod.mixin.client.multiplayer;

import net.daporkchop.pepsimod.module.impl.misc.FreecamMod;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldClient.class)
public abstract class MixinWorldClient extends World {
    public MixinWorldClient() {
        super(null, null, null, null, false);
    }

    @Inject(method = "doVoidFogParticles", at = @At("HEAD"), cancellable = true)
    public void preDoVoidFogParticles(int posX, int posY, int posZ, CallbackInfo callbackInfo) {
        if (FreecamMod.INSTANCE.isEnabled) {
            callbackInfo.cancel();
        }
    }
}
