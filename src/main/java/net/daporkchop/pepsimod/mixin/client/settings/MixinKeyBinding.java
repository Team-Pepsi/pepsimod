package net.daporkchop.pepsimod.mixin.client.settings;

import net.daporkchop.pepsimod.optimization.OverrideCounter;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author DaPorkchop_
 */
@Mixin(KeyBinding.class)
public abstract class MixinKeyBinding implements OverrideCounter {
    public int overrideCounter = 0;

    @Override
    public void incrementOverride() {
        this.overrideCounter++;
    }

    @Override
    public void decrementOverride() {
        if (--this.overrideCounter < 0) {
            this.overrideCounter = 0;
        }
    }

    @Override
    public int getOverride() {
        return this.overrideCounter;
    }
}
