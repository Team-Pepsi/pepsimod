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

package net.daporkchop.pepsimod.mixin.entity;

import net.daporkchop.pepsimod.module.impl.misc.FreecamMod;
import net.daporkchop.pepsimod.module.impl.movement.NoClipMod;
import net.daporkchop.pepsimod.module.impl.movement.VelocityMod;
import net.daporkchop.pepsimod.module.impl.render.AntiInvisibleMod;
import net.daporkchop.pepsimod.module.impl.render.ESPMod;
import net.daporkchop.pepsimod.util.config.impl.ESPTranslator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.daporkchop.pepsimod.util.PepsiConstants.mc;

@Mixin(Entity.class)
public abstract class MixinEntity {
    @Shadow
    public double motionX;
    @Shadow
    public double motionY;
    @Shadow
    public double motionZ;
    @Shadow
    private AxisAlignedBB boundingBox;

    @Shadow
    public abstract void resetPositionToBB();

    @Inject(
            method = "Lnet/minecraft/entity/Entity;setVelocity(DDD)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void preSetVelocity(double x, double y, double z, CallbackInfo callbackInfo) {
        float strength = 1.0f;
        if (Entity.class.cast(this) == mc.player) {
            strength = VelocityMod.INSTANCE.getVelocity();
        }
        this.motionX = x * strength;
        this.motionY = y * strength;
        this.motionZ = z * strength;
        callbackInfo.cancel();
    }

    @Inject(
            method = "Lnet/minecraft/entity/Entity;move(Lnet/minecraft/entity/MoverType;DDD)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void preMove(MoverType type, double x, double y, double z, CallbackInfo callbackInfo) {
        Entity thisAsEntity = Entity.class.cast(this);
        if ((FreecamMod.INSTANCE.state.enabled || NoClipMod.INSTANCE.state.enabled) && thisAsEntity instanceof EntityPlayer) {
            this.boundingBox = this.boundingBox.offset(x, y, z);
            this.resetPositionToBB();
            callbackInfo.cancel();
        }
    }

    @Inject(
            method = "Lnet/minecraft/entity/Entity;isInvisibleToPlayer(Lnet/minecraft/entity/player/EntityPlayer;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    public void preIsInvisibleToPlayer(EntityPlayer player, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (AntiInvisibleMod.INSTANCE.state.enabled) {
            callbackInfoReturnable.setReturnValue(false);
            callbackInfoReturnable.cancel();
        }
    }

    @Inject(
            method = "Lnet/minecraft/entity/Entity;isGlowing()Z",
            at = @At("HEAD"),
            cancellable = true
    )
    public void preisGlowing(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (ESPMod.INSTANCE.state.enabled && !ESPTranslator.INSTANCE.box) {
            Entity this_ = Entity.class.cast(this);
            if (this_.isInvisible()) {
                if (!ESPTranslator.INSTANCE.invisible) {
                    return;
                }
            }
            if (ESPTranslator.INSTANCE.animals && this_ instanceof EntityAnimal) {
                callbackInfoReturnable.setReturnValue(true);
            } else if (ESPTranslator.INSTANCE.monsters && this_ instanceof EntityMob) {
                callbackInfoReturnable.setReturnValue(true);
            } else if (ESPTranslator.INSTANCE.players && this_ instanceof EntityPlayer) {
                callbackInfoReturnable.setReturnValue(true);
            } else if (ESPTranslator.INSTANCE.golems && this_ instanceof EntityGolem) {
                callbackInfoReturnable.setReturnValue(true);
            }
        }
    }

    @Inject(
            method = "Lnet/minecraft/entity/Entity;isInWater()Z",
            at = @At("HEAD"),
            cancellable = true
    )
    public void preIsInWater(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (FreecamMod.INSTANCE.state.enabled) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }
}
