/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2019 DaPorkchop_
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from DaPorkchop_.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original author of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.daporkchop.pepsimod.mixin.block;

import net.daporkchop.pepsimod.module.impl.misc.AnnouncerMod;
import net.daporkchop.pepsimod.module.impl.misc.FreecamMod;
import net.daporkchop.pepsimod.module.impl.movement.NoClipMod;
import net.daporkchop.pepsimod.module.impl.render.XrayMod;
import net.daporkchop.pepsimod.optimization.BlockID;
import net.daporkchop.pepsimod.util.config.impl.XrayTranslator;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.daporkchop.pepsimod.util.PepsiConstants.pepsimod;

@Mixin(Block.class)
public abstract class MixinBlock extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<Block> implements BlockID {
    public int pepsimod_id = 0;

    @Override
    public int getBlockId() {
        return this.pepsimod_id;
    }

    @Override
    public void internal_setBlockId(int id) {
        this.pepsimod_id = id;
    }

    @Inject(
            method = "Lnet/minecraft/block/Block;isFullCube(Lnet/minecraft/block/state/IBlockState;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    public void preIsFullCube(IBlockState state, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (pepsimod.hasInitializedModules) {
            if (XrayMod.INSTANCE.state.enabled) {
                callbackInfoReturnable.setReturnValue(XrayTranslator.INSTANCE.isTargeted(this));
            } else if (FreecamMod.INSTANCE.state.enabled || NoClipMod.INSTANCE.state.enabled) {
                callbackInfoReturnable.setReturnValue(false);
            }
        }
    }

    @Inject(
            method = "Lnet/minecraft/block/Block;shouldSideBeRendered(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/EnumFacing;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    public void preShouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (pepsimod.hasInitializedModules) {
            if (XrayMod.INSTANCE.state.enabled) {
                callbackInfo.setReturnValue(true);
                callbackInfo.cancel();
            }
        }
        //vanilla code follows
    }

    @Inject(
            method = "Lnet/minecraft/block/Block;onPlayerDestroy(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/state/IBlockState;)V",
            at = @At("HEAD")
    )
    public void preOnPlayerDestroy(World worldIn, BlockPos pos, IBlockState state, CallbackInfo callbackInfo) {
        if (worldIn.isRemote) {
            AnnouncerMod.INSTANCE.onBreakBlock(state);
        }
    }
}
