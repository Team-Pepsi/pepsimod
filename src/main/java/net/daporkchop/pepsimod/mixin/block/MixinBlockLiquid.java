package net.daporkchop.pepsimod.mixin.block;

import net.daporkchop.pepsimod.module.impl.render.XrayMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BlockLiquid.class)
public abstract class MixinBlockLiquid extends Block {
    protected MixinBlockLiquid() {
        super(null);
    }

    @Overwrite
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return XrayMod.INSTANCE.isEnabled || this.blockMaterial != Material.LAVA;
    }

    @Overwrite
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
        //TODO: https://github.com/Wurst-Imperium/Wurst-MC-1.12/blob/979c016f60f19b158c35d3c48956208c6840ac38/patch/minecraft.patch#L267-L268
    }
}
