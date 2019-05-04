package net.daporkchop.pepsimod.mixin.entity;

import net.daporkchop.pepsimod.optimization.SizeSettable;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author DaPorkchop_
 */
@Mixin(EntityAgeable.class)
public abstract class MixinEntityAgeable extends EntityCreature implements SizeSettable {
    public MixinEntityAgeable() {
        super(null);
    }

    @Override
    public void forceSetSize(float width, float height) {
        super.setSize(width, height);
    }
}
