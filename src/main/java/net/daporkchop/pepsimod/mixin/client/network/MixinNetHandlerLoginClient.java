package net.daporkchop.pepsimod.mixin.client.network;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.util.MCLeaks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.network.login.server.SPacketEncryptionRequest;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerLoginClient.class)
public abstract class MixinNetHandlerLoginClient implements INetHandlerLoginClient {
    @Shadow
    private final NetworkManager networkManager;

    @Inject(method = "handleEncryptionRequest", at = @At("HEAD"), cancellable = true)
    // setting cancellable to true so that we can make the method return prematurely if need be
    public void handleEncryptionRequest(SPacketEncryptionRequest packetIn, CallbackInfo ci) {
        if (PepsiMod.INSTANCE.isMcLeaksAccount) {
            MCLeaks.joinServerStuff(packetIn, this.networkManager);
            ci.cancel(); // prevent vanilla auth code from running afterwards
            // https://github.com/SpongePowered/Mixin/wiki/Advanced-Mixin-Usage---Callback-Injectors#3-cancellable-injections
        }
    }

    { // kek look at this hack
        this.networkManager = null;
    }
}
