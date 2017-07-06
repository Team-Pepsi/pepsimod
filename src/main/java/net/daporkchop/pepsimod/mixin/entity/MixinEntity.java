package net.daporkchop.pepsimod.mixin.entity;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.impl.Velocity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public class MixinEntity {
    @Shadow
    public double motionX;
    @Shadow
    public double motionY;
    @Shadow
    public double motionZ;

    @Overwrite
    public void setVelocity(double x, double y, double z) {
        float strength = 1.0f;
        if (Entity.class.cast(this) == PepsiMod.INSTANCE.mc.player) {
            strength = Velocity.PROCENT;
        }
        this.motionX = x * strength;
        this.motionY = y * strength;
        this.motionZ = z * strength;
    }
}
