package net.daporkchop.pepsimod.mixin.entity;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.impl.misc.FreecamMod;
import net.daporkchop.pepsimod.module.impl.movement.VelocityMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class MixinEntity {
    @Shadow
    public double motionX;
    @Shadow
    public double motionY;
    @Shadow
    public double motionZ;

    /**
     * this hides a stupid warning
     */
    @Overwrite
    public void setVelocity(double x, double y, double z) {
        float strength = 1.0f;
        if (Entity.class.cast(this) == PepsiMod.INSTANCE.mc.player) {
            strength = VelocityMod.INSTANCE.getVelocity();
        }
        this.motionX = x * strength;
        this.motionY = y * strength;
        this.motionZ = z * strength;
    }

    @Inject(method = "move", at = @At("HEAD"))
    public void move(MoverType type, double x, double y, double z, CallbackInfo callbackInfo) {
        Entity thisAsEntity = Entity.class.cast(this);
        if (FreecamMod.INSTANCE.isEnabled && thisAsEntity instanceof EntityPlayer) {
            if (thisAsEntity == PepsiMod.INSTANCE.mc.player) {
                this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x * 10, y, z * 10));
            } else {
                this.setEntityBoundingBox(this.getEntityBoundingBox().offset(x, y, z));
            }
            this.resetPositionToBB();
        }
    }

    @Shadow
    public AxisAlignedBB getEntityBoundingBox() {
        return null;
    }

    @Shadow
    public void setEntityBoundingBox(AxisAlignedBB bb) {

    }

    @Shadow
    public void resetPositionToBB() {

    }
}
