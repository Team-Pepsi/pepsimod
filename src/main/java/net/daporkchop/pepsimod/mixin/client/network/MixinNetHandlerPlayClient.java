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

package net.daporkchop.pepsimod.mixin.client.network;

import com.google.common.collect.Maps;
import net.daporkchop.pepsimod.module.impl.misc.AnnouncerMod;
import net.daporkchop.pepsimod.module.impl.misc.FreecamMod;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.UUID;

import static net.daporkchop.pepsimod.util.PepsiConstants.mc;

@Mixin(NetHandlerPlayClient.class)
public abstract class MixinNetHandlerPlayClient {
    @Shadow
    private final Map<UUID, NetworkPlayerInfo> playerInfoMap = Maps.newHashMap();

    @Inject(method = "sendPacket", at = @At("HEAD"), cancellable = true)
    public void preSendPacket(Packet<?> packet, CallbackInfo callbackInfo) {
        if (FreecamMod.INSTANCE.state.enabled) {
            if (packet instanceof CPacketPlayer) {
                callbackInfo.cancel();
            }
        }
    }

    @Inject(method = "handlePlayerListItem", at = @At("HEAD"))
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
}
