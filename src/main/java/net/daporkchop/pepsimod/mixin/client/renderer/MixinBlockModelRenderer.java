package net.daporkchop.pepsimod.mixin.client.renderer;

import net.daporkchop.pepsimod.module.impl.render.XrayMod;
import net.daporkchop.pepsimod.util.module.XrayUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockModelRenderer.class)
public abstract class MixinBlockModelRenderer {
    @Inject(method = "renderModel", at = @At("HEAD"), cancellable = true)
    public void preRenderModel(IBlockAccess blockAccessIn, IBakedModel modelIn, IBlockState blockStateIn, BlockPos blockPosIn, BufferBuilder buffer, boolean checkSides, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (XrayMod.INSTANCE.isEnabled) {
            if (!XrayUtils.isTargeted(blockStateIn.getBlock())) {
                callbackInfoReturnable.setReturnValue(false);
                callbackInfoReturnable.cancel();
            }
        }
        //vanilla code follows
    }

    @Inject(method = "renderModelSmooth", at = @At("HEAD"), cancellable = true)
    public void preRenderQuadsSmooth(IBlockAccess access, IBakedModel model, IBlockState stateIn, BlockPos pos, BufferBuilder bufferBuilder, boolean idk, long ok, CallbackInfoReturnable<Boolean> returnable) {
        if (XrayMod.INSTANCE.isEnabled) {
            if (!XrayUtils.isTargeted(stateIn.getBlock())) {
                returnable.setReturnValue(false);
                returnable.cancel();
            }
        }
        //vanilla code follows
    }
}
