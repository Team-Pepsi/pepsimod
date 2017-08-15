package net.daporkchop.pepsimod.mixin.network.handshake.client;

import net.daporkchop.pepsimod.util.PepsiUtils;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.handshake.client.C00Handshake;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(C00Handshake.class)
public abstract class MixinC00Handshake {
    @Shadow
    private int protocolVersion;

    @Inject(method = "readPacketData", at = @At("RETURN"))
    public void preReadPacketData(PacketBuffer buffer, CallbackInfo callbackInfo) {
        this.protocolVersion = PepsiUtils.protocolVersion;
    }

    @Inject(method = "writePacketData", at = @At("HEAD"))
    public void preWritePacketData(PacketBuffer buffer, CallbackInfo callbackInfo) {
        this.protocolVersion = PepsiUtils.protocolVersion;
    }
}
