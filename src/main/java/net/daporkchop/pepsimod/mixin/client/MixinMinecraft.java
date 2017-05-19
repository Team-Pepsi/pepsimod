package net.daporkchop.pepsimod.mixin.client;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.profiler.ISnooperInfo;
import net.minecraft.util.IThreadListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft implements IThreadListener, ISnooperInfo {
    @Inject(method = "shutdown", at = @At("HEAD"))
    public void saveSettingsOnShutdown(CallbackInfo ci) {
        PepsiMod.INSTANCE.saveConfig();
    }

    @Inject(method = "runGameLoop", at = @At("RETURN"))
    public void onClientPreTick(CallbackInfo callbackInfo)  {
        if (PepsiMod.INSTANCE.mc.player != null) { // is ingame
            for (Module module : ModuleManager.ENABLED_MODULES) {
                module.tick();
            }
        }
    }
}
