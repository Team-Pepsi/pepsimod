/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017 Team Pepsi
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from Team Pepsi.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: Team Pepsi), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.daporkchop.pepsimod.mixin.block;

import net.daporkchop.pepsimod.module.impl.misc.AnnouncerMod;
import net.daporkchop.pepsimod.module.impl.misc.FreecamMod;
import net.daporkchop.pepsimod.module.impl.render.XrayMod;
import net.daporkchop.pepsimod.util.module.XrayUtils;
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

@Mixin(Block.class)
public abstract class MixinBlock extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<Block> {
    /**
     * used for superfast accessing of block ids
     */
    public int pepsimod_id = 0;

    @Inject(method = "isFullCube", at = @At("HEAD"), cancellable = true)
    public void preIsFullCube(IBlockState state, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (XrayMod.INSTANCE.isEnabled) {
            callbackInfoReturnable.setReturnValue(XrayUtils.isTargeted(Block.class.cast(this)));
            return;
        }
        if (FreecamMod.INSTANCE.isEnabled) {
            callbackInfoReturnable.setReturnValue(false);
            return;
        }
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

    @Inject(method = "onBlockDestroyedByPlayer", at = @At("HEAD"))
    public void preOnBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state, CallbackInfo callbackInfo) {
        if (worldIn.isRemote) {
            AnnouncerMod.INSTANCE.onBreakBlock(state);
        }
    }
}
