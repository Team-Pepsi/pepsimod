/*
 * Adapted from The MIT License (MIT)
 *
 * Copyright (c) 2016-2021 DaPorkchop_
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * Any persons and/or organizations using this software must include the above copyright notice and this permission notice,
 * provide sufficient credit to the original authors of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.daporkchop.pepsimod.asm.core.client.network;

import net.daporkchop.pepsimod.module.impl.misc.AnnouncerMod;
import net.daporkchop.pepsimod.module.impl.movement.VelocityMod;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

import static net.daporkchop.pepsimod.util.PepsiConstants.*;

@Mixin(NetHandlerPlayClient.class)
public abstract class MixinNetHandlerPlayClient {
    @Shadow
    @Final
    private Map<UUID, NetworkPlayerInfo> playerInfoMap;

    @Inject(
            method = "Lnet/minecraft/client/network/NetHandlerPlayClient;handlePlayerListItem(Lnet/minecraft/network/play/server/SPacketPlayerListItem;)V",
            at = @At("HEAD")
    )
    public void preHandlePlayerListItem(SPacketPlayerListItem listItem, CallbackInfo callbackInfo) {
        try {
            if (listItem.getEntries().size() == 1) {
                if (listItem.getAction() == SPacketPlayerListItem.Action.ADD_PLAYER) {
                    for (SPacketPlayerListItem.AddPlayerData data : listItem.getEntries()) {
                        if (!data.getProfile().getId().equals(mc.player.getGameProfile().getId())) {
                            AnnouncerMod.INSTANCE.onPlayerJoin(data.getProfile().getName());
                        }
                    }
                } else if (listItem.getAction() == SPacketPlayerListItem.Action.REMOVE_PLAYER) {
                    for (SPacketPlayerListItem.AddPlayerData data : listItem.getEntries()) {
                        if (!data.getProfile().getId().equals(mc.player.getGameProfile().getId())) {
                            AnnouncerMod.INSTANCE.onPlayerLeave(this.playerInfoMap.get(data.getProfile().getId()).getGameProfile().getName());
                        }
                    }

                }
            }
        } catch (NullPointerException e) {
        }
    }

    @Redirect(method = "Lnet/minecraft/client/network/NetHandlerPlayClient;handleExplosion(Lnet/minecraft/network/play/server/SPacketExplosion;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/network/play/server/SPacketExplosion;getMotionX()F"))
    private float pepsimod_handleExplosion_adjustMotionX(SPacketExplosion packet) {
        return packet.getMotionX() * VelocityMod.INSTANCE.getVelocity();
    }

    @Redirect(method = "Lnet/minecraft/client/network/NetHandlerPlayClient;handleExplosion(Lnet/minecraft/network/play/server/SPacketExplosion;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/network/play/server/SPacketExplosion;getMotionY()F"))
    private float pepsimod_handleExplosion_adjustMotionY(SPacketExplosion packet) {
        return packet.getMotionY() * VelocityMod.INSTANCE.getVelocity();
    }

    @Redirect(method = "Lnet/minecraft/client/network/NetHandlerPlayClient;handleExplosion(Lnet/minecraft/network/play/server/SPacketExplosion;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/network/play/server/SPacketExplosion;getMotionZ()F"))
    private float pepsimod_handleExplosion_adjustMotionZ(SPacketExplosion packet) {
        return packet.getMotionZ() * VelocityMod.INSTANCE.getVelocity();
    }
}
