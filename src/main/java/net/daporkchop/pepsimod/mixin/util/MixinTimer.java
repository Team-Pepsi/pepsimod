package net.daporkchop.pepsimod.mixin.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Timer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Timer.class)
public class MixinTimer {
    @Shadow
    public int elapsedTicks;

    @Shadow
    public float renderPartialTicks;

    @Shadow
    public float elapsedPartialTicks;

    @Shadow
    private long lastSyncSysClock;

    @Shadow
    private float tickLength;

    @Overwrite
    public void updateTimer() {
        float timerSpeed = (net.daporkchop.pepsimod.module.impl.Timer.INSTANCE == null ?
                1.0f :
                net.daporkchop.pepsimod.module.impl.Timer.INSTANCE.getMultiplier());

        long i = Minecraft.getSystemTime();
        this.elapsedPartialTicks = (float) (i - this.lastSyncSysClock) / this.tickLength * timerSpeed;
        this.lastSyncSysClock = i;
        this.renderPartialTicks += elapsedPartialTicks;
        this.elapsedTicks = (int) this.renderPartialTicks;
        this.renderPartialTicks -= this.elapsedTicks;
    }
}
