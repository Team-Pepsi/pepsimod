package net.daporkchop.pepsimod.mixin.client.renderer.chunk;

import net.daporkchop.pepsimod.module.impl.misc.FreecamMod;
import net.daporkchop.pepsimod.module.impl.render.XrayMod;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VisGraph.class)
public abstract class MixinVisGraph {
    @Inject(method = "setOpaqueCube", at = @At("HEAD"), cancellable = true)
    public void preSetOpaqueCube(BlockPos pos, CallbackInfo callbackInfo) {
        if (XrayMod.INSTANCE.isEnabled || FreecamMod.INSTANCE.isEnabled) {
            callbackInfo.cancel();
        }
        //vanilla code follows
    }
}
