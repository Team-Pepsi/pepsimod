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
import net.daporkchop.pepsimod.module.impl.render.AntiBlindMod;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

    @Shadow
    public boolean isElytraFlying() {
        return false;
    }

    @Overwrite
    public void travel(float p_191986_1_, float p_191986_2_, float p_191986_3_) {
        EntityLivingBase thisAsEntity = EntityLivingBase.class.cast(this);
        if (this.isServerWorld() || this.canPassengerSteer()) {
            if (!this.isInWater() || FreecamMod.INSTANCE.isEnabled || thisAsEntity instanceof EntityPlayer && ((EntityPlayer) thisAsEntity).capabilities.isFlying) {
                if (!this.isInLava() || thisAsEntity instanceof EntityPlayer && ((EntityPlayer) thisAsEntity).capabilities.isFlying) {
                    if (this.isElytraFlying()) {
                        if (this.motionY > -0.5D) {
                            this.fallDistance = 1.0F;
                        }

                        Vec3d vec3d = this.getLookVec();
                        float f = this.rotationPitch * 0.017453292F;
                        double d6 = Math.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
                        double d8 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                        double d1 = vec3d.lengthVector();
                        float f4 = MathHelper.cos(f);
                        f4 = (float) ((double) f4 * (double) f4 * Math.min(1.0D, d1 / 0.4D));
                        this.motionY += -0.08D + (double) f4 * 0.06D;

                        if (this.motionY < 0.0D && d6 > 0.0D) {
                            double d2 = this.motionY * -0.1D * (double) f4;
                            this.motionY += d2;
                            this.motionX += vec3d.x * d2 / d6;
                            this.motionZ += vec3d.z * d2 / d6;
                        }

                        if (f < 0.0F) {
                            double d10 = d8 * (double) (-MathHelper.sin(f)) * 0.04D;
                            this.motionY += d10 * 3.2D;
                            this.motionX -= vec3d.x * d10 / d6;
                            this.motionZ -= vec3d.z * d10 / d6;
                        }

                        if (d6 > 0.0D) {
                            this.motionX += (vec3d.x / d6 * d8 - this.motionX) * 0.1D;
                            this.motionZ += (vec3d.z / d6 * d8 - this.motionZ) * 0.1D;
                        }

                        this.motionX *= 0.9900000095367432D;
                        this.motionY *= 0.9800000190734863D;
                        this.motionZ *= 0.9900000095367432D;
                        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

                        if (this.isCollidedHorizontally && !this.world.isRemote) {
                            double d11 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                            double d3 = d8 - d11;
                            float f5 = (float) (d3 * 10.0D - 3.0D);

                            if (f5 > 0.0F) {
                                this.playSound(this.getFallSound((int) f5), 1.0F, 1.0F);
                                this.attackEntityFrom(DamageSource.FLY_INTO_WALL, f5);
                            }
                        }

                        if (this.onGround && !this.world.isRemote) {
                            this.setFlag(7, false);
                        }
                    } else {
                        float f6 = 0.91F;
                        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(this.posX, this.getEntityBoundingBox().minY - 1.0D, this.posZ);

                        if (this.onGround) {
                            f6 = this.world.getBlockState(blockpos$pooledmutableblockpos).getBlock().slipperiness * 0.91F;
                        }

                        float f7 = 0.16277136F / (f6 * f6 * f6);
                        float f8;

                        if (this.onGround) {
                            f8 = this.getAIMoveSpeed() * f7;
                        } else {
                            f8 = this.jumpMovementFactor;
                        }

                        this.moveRelative(p_191986_1_, p_191986_2_, p_191986_3_, f8);
                        f6 = 0.91F;

                        if (this.onGround) {
                            f6 = this.world.getBlockState(blockpos$pooledmutableblockpos.setPos(this.posX, this.getEntityBoundingBox().minY - 1.0D, this.posZ)).getBlock().slipperiness * 0.91F;
                        }

                        if (this.isOnLadder()) {
                            float f9 = 0.15F;
                            this.motionX = MathHelper.clamp(this.motionX, -0.15000000596046448D, 0.15000000596046448D);
                            this.motionZ = MathHelper.clamp(this.motionZ, -0.15000000596046448D, 0.15000000596046448D);
                            this.fallDistance = 0.0F;

                            if (this.motionY < -0.15D) {
                                this.motionY = -0.15D;
                            }

                            boolean flag = this.isSneaking() && thisAsEntity instanceof EntityPlayer;

                            if (flag && this.motionY < 0.0D) {
                                this.motionY = 0.0D;
                            }
                        }

                        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

                        if (this.isCollidedHorizontally && this.isOnLadder()) {
                            this.motionY = 0.2D;
                        }

                        if (this.isPotionActive(MobEffects.LEVITATION)) {
                            this.motionY += (0.05D * (double) (this.getActivePotionEffect(MobEffects.LEVITATION).getAmplifier() + 1) - this.motionY) * 0.2D;
                        } else {
                            blockpos$pooledmutableblockpos.setPos(this.posX, 0.0D, this.posZ);

                            if (!this.world.isRemote || this.world.isBlockLoaded(blockpos$pooledmutableblockpos) && this.world.getChunkFromBlockCoords(blockpos$pooledmutableblockpos).isLoaded()) {
                                if (!this.hasNoGravity()) {
                                    this.motionY -= 0.08D;
                                }
                            } else if (this.posY > 0.0D) {
                                this.motionY = -0.1D;
                            } else {
                                this.motionY = 0.0D;
                            }
                        }

                        this.motionY *= 0.9800000190734863D;
                        this.motionX *= (double) f6;
                        this.motionZ *= (double) f6;
                        blockpos$pooledmutableblockpos.release();
                    }
                } else {
                    double d4 = this.posY;
                    this.moveRelative(p_191986_1_, p_191986_2_, p_191986_3_, 0.02F);
                    this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                    this.motionX *= 0.5D;
                    this.motionY *= 0.5D;
                    this.motionZ *= 0.5D;

                    if (!this.hasNoGravity()) {
                        this.motionY -= 0.02D;
                    }

                    if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d4, this.motionZ)) {
                        this.motionY = 0.30000001192092896D;
                    }
                }
            } else {
                double d0 = this.posY;
                float f1 = this.getWaterSlowDown();
                float f2 = 0.02F;
                float f3 = (float) EnchantmentHelper.getDepthStriderModifier(thisAsEntity);

                if (f3 > 3.0F) {
                    f3 = 3.0F;
                }

                if (!this.onGround) {
                    f3 *= 0.5F;
                }

                if (f3 > 0.0F) {
                    f1 += (0.54600006F - f1) * f3 / 3.0F;
                    f2 += (this.getAIMoveSpeed() - f2) * f3 / 3.0F;
                }

                this.moveRelative(p_191986_1_, p_191986_2_, p_191986_3_, f2);
                this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                this.motionX *= (double) f1;
                this.motionY *= 0.800000011920929D;
                this.motionZ *= (double) f1;

                if (!this.hasNoGravity()) {
                    this.motionY -= 0.02D;
                }

                if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d0, this.motionZ)) {
                    this.motionY = 0.30000001192092896D;
                }
            }
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d5 = this.posX - this.prevPosX;
        double d7 = this.posZ - this.prevPosZ;
        double d9 = this instanceof net.minecraft.entity.passive.EntityFlying ? this.posY - this.prevPosY : 0.0D;
        float f10 = MathHelper.sqrt(d5 * d5 + d9 * d9 + d7 * d7) * 4.0F;

        if (f10 > 1.0F) {
            f10 = 1.0F;
        }

        this.limbSwingAmount += (f10 - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
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
