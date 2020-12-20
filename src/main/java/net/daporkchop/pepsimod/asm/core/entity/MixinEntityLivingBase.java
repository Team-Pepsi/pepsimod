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

package net.daporkchop.pepsimod.asm.core.entity;

import net.daporkchop.pepsimod.module.impl.movement.ElytraFlyMod;
import net.daporkchop.pepsimod.module.impl.render.AntiBlindMod;
import net.daporkchop.pepsimod.util.config.impl.ElytraFlyTranslator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.daporkchop.pepsimod.util.PepsiConstants.mc;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity {
    @Shadow
    public float jumpMovementFactor;
    @Shadow
    public float prevLimbSwingAmount;
    @Shadow
    public float limbSwingAmount;
    @Shadow
    public float limbSwing;

    public MixinEntityLivingBase() {
        super(null);
    }

    @Inject(
            method = "Lnet/minecraft/entity/EntityLivingBase;isPotionActive(Lnet/minecraft/potion/Potion;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    public void preIsPotionActive(Potion potionIn, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (potionIn == MobEffects.BLINDNESS && AntiBlindMod.INSTANCE.state.enabled) {
            callbackInfoReturnable.setReturnValue(false);
            callbackInfoReturnable.cancel();
        }
    }

    @Inject(
            method = "Lnet/minecraft/entity/EntityLivingBase;onLivingUpdate()V",
            at = @At("HEAD")
    )
    public void preOnLivingUpdate(CallbackInfo callbackInfo) {
        EntityLivingBase thisAsEntity = EntityLivingBase.class.cast(this);
        if (thisAsEntity == mc.player && ElytraFlyMod.INSTANCE.state.enabled && ElytraFlyTranslator.INSTANCE.mode == ElytraFlyTranslator.ElytraFlyMode.PACKET) {
            this.motionY = 0;
        }
    }

    @Inject(
            method = "Lnet/minecraft/entity/EntityLivingBase;travel(FFF)V",
            at = @At("HEAD")
    )
    public void preTravel(float x, float y, float z, CallbackInfo callbackInfo) {
        EntityLivingBase thisAsEntity = EntityLivingBase.class.cast(this);
        if (thisAsEntity == mc.player && ElytraFlyMod.INSTANCE.state.enabled && ElytraFlyTranslator.INSTANCE.mode == ElytraFlyTranslator.ElytraFlyMode.PACKET) {
            this.motionY = 0;
        }
    }
}
