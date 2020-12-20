/*
 * Adapted from The MIT License (MIT)
 *
 * Copyright (c) 2016-2020 DaPorkchop_
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * Any persons and/or organizations using this software must include the above copyright notice and this permission notice,
 * provide sufficient credit to the original authors of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.daporkchop.pepsimod.asm.core.block;

import net.daporkchop.pepsimod.module.impl.movement.JesusMod;
import net.daporkchop.pepsimod.module.impl.render.XrayMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.daporkchop.pepsimod.util.PepsiConstants.pepsimod;

@Mixin(BlockLiquid.class)
public abstract class MixinBlockLiquid extends Block {
    protected MixinBlockLiquid() {
        super(null);
    }

    @Inject(
            method = "Lnet/minecraft/block/BlockLiquid;isPassable(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    public void preIsPassable(IBlockAccess worldIn, BlockPos pos, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (pepsimod.hasInitializedModules) {
            if (XrayMod.INSTANCE.state.enabled) {
                callbackInfoReturnable.setReturnValue(true);
            }
        }
    }

    @Inject(
            method = "Lnet/minecraft/block/BlockLiquid;getCollisionBoundingBox(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/AxisAlignedBB;",
            at = @At("HEAD"),
            cancellable = true
    )
    public void preGetCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, CallbackInfoReturnable<AxisAlignedBB> callbackInfoReturnable) {
        if (pepsimod.hasInitializedModules) {
            if (JesusMod.INSTANCE.shouldBeSolid()) {
                callbackInfoReturnable.setReturnValue(FULL_BLOCK_AABB);
            }
        }
    }
}
