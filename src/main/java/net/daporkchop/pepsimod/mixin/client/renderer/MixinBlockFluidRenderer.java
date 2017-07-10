package net.daporkchop.pepsimod.mixin.client.renderer;

import net.daporkchop.pepsimod.module.impl.render.Xray;
import net.daporkchop.pepsimod.util.XrayUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockFluidRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockFluidRenderer.class)
public abstract class MixinBlockFluidRenderer {
    @Inject(method = "renderFluid", at = @At("HEAD"), cancellable = true)
    public void preRenderFluid(IBlockAccess blockAccess, IBlockState blockStateIn, BlockPos blockPosIn, BufferBuilder worldRendererIn, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (Xray.INSTANCE.isEnabled) {
            if (!XrayUtils.isTargeted(blockStateIn.getBlock())) {
                callbackInfoReturnable.setReturnValue(false);
                callbackInfoReturnable.cancel();
            }
        }
        //vanilla code follows
    }
}
