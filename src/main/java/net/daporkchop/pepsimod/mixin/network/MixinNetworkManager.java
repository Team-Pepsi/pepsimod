/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2018 DaPorkchop_
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from DaPorkchop_.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original author of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.daporkchop.pepsimod.mixin.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.daporkchop.pepsimod.misc.TickRate;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.impl.misc.FreecamMod;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.util.text.ITextComponent;
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
        TickRate.update(p_channelRead0_2_);
        if (ModuleManager.preRecievePacket(p_channelRead0_2_)) {
            callbackInfo.cancel();
            return;
        }
    }

    @Inject(method = "channelRead0", at = @At("RETURN"))
    public void postProcess(ChannelHandlerContext p_channelRead0_1_, Packet<?> p_channelRead0_2_, CallbackInfo callbackInfo) {
        ModuleManager.postRecievePacket(p_channelRead0_2_);
    }

    @Inject(method = "closeChannel", at = @At("HEAD"))
    public void preCloseChannel(ITextComponent message, CallbackInfo callbackInfo) {
        TickRate.reset();
        if (FreecamMod.INSTANCE.state.enabled) {
            ModuleManager.disableModule(FreecamMod.INSTANCE);
        }
    }

    @Inject(method = "exceptionCaught", at = @At("RETURN"))
    public void postExceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_, CallbackInfo callbackInfo) {
        p_exceptionCaught_2_.printStackTrace(System.out);
    }
}
