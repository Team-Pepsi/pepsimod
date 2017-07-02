package net.daporkchop.pepsimod.mixin.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

/**
 * inspired by fr1kin's PacketListener
 */
@Mixin(NetworkManager.class)
public abstract class MixinNetworkManager {
    @Inject(method = "dispatchPacket", at = @At("HEAD"), cancellable = true)
    public void preSend(final Packet<?> inPacket, @Nullable final GenericFutureListener<? extends Future<? super Void>>[] futureListeners, CallbackInfo callbackInfo) {
        if (ModuleManager.preSendPacket(inPacket)) {
            callbackInfo.cancel();
            return;
        }
    }

    @Inject(method = "dispatchPacket", at = @At("RETURN"))
    public void postSend(final Packet<?> inPacket, @Nullable final GenericFutureListener<? extends Future<? super Void>>[] futureListeners, CallbackInfo callbackInfo) {
        ModuleManager.postSendPacket(inPacket);
    }

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    public void preProcess(ChannelHandlerContext p_channelRead0_1_, Packet<?> p_channelRead0_2_, CallbackInfo callbackInfo) {
        if (ModuleManager.preRecievePacket(p_channelRead0_2_)) {
            callbackInfo.cancel();
            return;
        }
    }

    @Inject(method = "channelRead0", at = @At("RETURN"))
    public void postProcess(ChannelHandlerContext p_channelRead0_1_, Packet<?> p_channelRead0_2_, CallbackInfo callbackInfo) {
        ModuleManager.postRecievePacket(p_channelRead0_2_);
    }
}
