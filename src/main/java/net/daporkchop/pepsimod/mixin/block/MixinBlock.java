package net.daporkchop.pepsimod.mixin.block;

import net.daporkchop.pepsimod.module.impl.misc.FreecamMod;
import net.daporkchop.pepsimod.module.impl.render.XrayMod;
import net.daporkchop.pepsimod.util.XrayUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class MixinBlock extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<Block> {
    /**
     * used for superfast accessing of block ids
     */
    public int pepsimod_id = 0;

    @Overwrite
    public boolean isFullCube(IBlockState state) {
        if (XrayMod.INSTANCE.isEnabled) {
            return XrayUtils.isTargeted(Block.class.cast(this));
        }
        return FreecamMod.INSTANCE.isEnabled;
        //TODO: add some other modules so i can put stuff here
        //https://github.com/Wurst-Imperium/Wurst-MC-1.12/blob/979c016f60f19b158c35d3c48956208c6840ac38/patch/minecraft.patch#L167-L168
    }

    /**
     * called on pepsimod init
     */
    public void setPepsimod_id() {
        pepsimod_id = Block.REGISTRY.getIDForObject(Block.class.cast(this));
    }

    @Inject(method = "shouldSideBeRendered", at = @At("HEAD"), cancellable = true)
    public void preShouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (XrayMod.INSTANCE.isEnabled) {
            callbackInfo.setReturnValue(true);
            callbackInfo.cancel();
        }
        //vanilla code follows
    }
}
