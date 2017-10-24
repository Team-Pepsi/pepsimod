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

package net.daporkchop.pepsimod.mixin.entity;

import net.daporkchop.pepsimod.module.impl.misc.FreecamMod;
import net.daporkchop.pepsimod.module.impl.movement.ElytraFlyMod;
import net.daporkchop.pepsimod.module.impl.render.AntiBlindMod;
import net.daporkchop.pepsimod.util.module.ElytraFlyMode;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.daporkchop.pepsimod.util.misc.Default.mc;
import static net.daporkchop.pepsimod.util.misc.Default.pepsiMod;

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

    @Inject(method = "isPotionActive", at = @At("HEAD"), cancellable = true)
    public void preIsPotionActive(Potion potionIn, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (potionIn == MobEffects.BLINDNESS && AntiBlindMod.INSTANCE.isEnabled) {
            callbackInfoReturnable.setReturnValue(false);
            callbackInfoReturnable.cancel();
        }
    }

    @Inject(method = "onLivingUpdate", at = @At("HEAD"))
    public void preOnLivingUpdate(CallbackInfo callbackInfo) {
        EntityLivingBase thisAsEntity = EntityLivingBase.class.cast(this);
        if (thisAsEntity == mc.player && ElytraFlyMod.INSTANCE.isEnabled && pepsiMod.elytraFlySettings.mode == ElytraFlyMode.PACKET) {
            motionY = 0;
        }
        if (FreecamMod.INSTANCE.isEnabled) {
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.player.motionY = FreecamMod.SPEED;
            } else if (mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.player.motionY = -FreecamMod.SPEED;
            } else {
                mc.player.motionY = 0;
            }
        }
    }

    @Shadow
    public boolean isElytraFlying() {
        return false;
    }

    @Inject(method = "travel", at = @At("HEAD"))
    public void preTravel(float x, float y, float z, CallbackInfo callbackInfo)    {
        EntityLivingBase thisAsEntity = EntityLivingBase.class.cast(this);
        if (thisAsEntity == mc.player && ElytraFlyMod.INSTANCE.isEnabled && pepsiMod.elytraFlySettings.mode == ElytraFlyMode.PACKET) {
            motionY = 0;
        }
    }

    @Shadow
    protected SoundEvent getFallSound(int heightIn) {
        return null;
    }

    @Shadow
    public boolean isServerWorld() {
        return true;
    }

    @Shadow
    public float getAIMoveSpeed() {
        return 0.0f;
    }

    @Shadow
    public boolean isOnLadder() {
        return false;
    }

    @Shadow
    public boolean isPotionActive(Potion potionIn) {
        return false;
    }

    @Shadow
    public PotionEffect getActivePotionEffect(Potion potionIn) {
        return null;
    }

    @Shadow
    protected float getWaterSlowDown() {
        return 0.0f;
    }
}
