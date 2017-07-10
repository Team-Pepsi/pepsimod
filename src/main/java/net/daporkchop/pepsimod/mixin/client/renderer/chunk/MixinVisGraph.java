package net.daporkchop.pepsimod.mixin.client.renderer.chunk;

import net.daporkchop.pepsimod.module.impl.render.Xray;
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
        if (Xray.INSTANCE.isEnabled) {
            callbackInfo.cancel();
        }
        //TODO: https://github.com/Wurst-Imperium/Wurst-MC-1.12/blob/979c016f60f19b158c35d3c48956208c6840ac38/patch/minecraft.patch#L3710
        //vanilla code follows
    }
}
