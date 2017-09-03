package net.daporkchop.pepsimod.mixin.client;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.util.Friends;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Shadow
    public EntityPlayerSP player;

    @Inject(method = "shutdown()V", at = @At("HEAD"))
    public void saveSettingsOnShutdown(CallbackInfo ci) {
        PepsiMod.INSTANCE.saveConfig();
    }

    @Inject(method = "runGameLoop", at = @At("RETURN"))
    public void onClientPreTick(CallbackInfo callbackInfo)  {
        if (PepsiMod.INSTANCE.mc.player != null) { // is ingame
            for (Module module : ModuleManager.AVALIBLE_MODULES) {
                if (module.shouldTick()) {
                    module.tick();
                }
            }
        }
    }

    @Inject(method = "runTickMouse", at = @At(value = "INVOKE_ASSIGN", target = "Lorg/lwjgl/input/Mouse;getEventButton()I"))
    public void onMouseClick(CallbackInfo ci) {
        try {
            if (Mouse.getEventButtonState()) {
                int buttonID = Mouse.getEventButton();
                switch (buttonID) {
                    case 2:
                        if (Minecraft.getMinecraft().objectMouseOver != null) {
                            RayTraceResult result = Minecraft.getMinecraft().objectMouseOver;
                            if (result.typeOfHit == RayTraceResult.Type.ENTITY && result.entityHit instanceof EntityPlayer) {
                                if (Friends.isFriend(result.entityHit.getUniqueID().toString())) {
                                    player.sendMessage(new TextComponentString(PepsiMod.chatPrefix + "Removed \u00A7c" + result.entityHit.getName() + "\u00A7r as a friend"));
                                    Friends.removeFriend(result.entityHit.getUniqueID().toString());
                                } else {
                                    player.sendMessage(new TextComponentString(PepsiMod.chatPrefix + "Added \u00A79" + result.entityHit.getName() + "\u00A7r as a friend"));
                                    Friends.addFriend(result.entityHit.getUniqueID().toString(), result.entityHit.getName());
                                }
                            }
                            System.out.println(result.entityHit.getClass().getCanonicalName());
                        }
                        break;
                }
            }
        } catch (NullPointerException e) {
            //wtf who cares
        }
    }
}
