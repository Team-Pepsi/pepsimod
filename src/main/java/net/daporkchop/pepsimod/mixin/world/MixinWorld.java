package net.daporkchop.pepsimod.mixin.world;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.impl.render.NoWeatherMod;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class MixinWorld {
    @Inject(method = "getCelestialAngle", at = @At("HEAD"), cancellable = true)
    public void preGetCelestialAngle(float partialTicks, CallbackInfoReturnable<Float> callbackInfoReturnable)  {
        if (NoWeatherMod.INSTANCE.isEnabled && PepsiMod.INSTANCE.noWeatherSettings.changeTime)    {
            callbackInfoReturnable.setReturnValue(PepsiMod.INSTANCE.noWeatherSettings.time);
            callbackInfoReturnable.cancel();
        }
    }

    @Inject(method = "getMoonPhase", at = @At("HEAD"), cancellable = true)
    public void preGetMoonPhase(CallbackInfoReturnable<Integer> callbackInfoReturnable) {
        if (NoWeatherMod.INSTANCE.isEnabled && PepsiMod.INSTANCE.noWeatherSettings.changeMoon)  {
            callbackInfoReturnable.setReturnValue(PepsiMod.INSTANCE.noWeatherSettings.moonPhase);
            callbackInfoReturnable.cancel();
        }
    }

    @Inject(method = "getRainStrength", at = @At("HEAD"), cancellable = true)
    public void preGetRainStrength(float partialTicks, CallbackInfoReturnable<Float> callbackInfoReturnable)    {
        if (NoWeatherMod.INSTANCE.isEnabled && PepsiMod.INSTANCE.noWeatherSettings.disableRain) {
            callbackInfoReturnable.setReturnValue(0.0f);
            callbackInfoReturnable.cancel();
        }
    }
}
