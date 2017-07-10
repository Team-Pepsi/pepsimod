package net.daporkchop.pepsimod.mixin.block;

import net.daporkchop.pepsimod.module.impl.misc.FreecamMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(BlockSlab.class)
public abstract class MixinBlockSlab extends Block {
    protected MixinBlockSlab() {
        super(null);
    }

    @Shadow
    public abstract boolean isDouble();

    @Overwrite
    public boolean isFullCube(IBlockState state) {
        return this.isDouble() && !FreecamMod.INSTANCE.isEnabled;
    }
}
