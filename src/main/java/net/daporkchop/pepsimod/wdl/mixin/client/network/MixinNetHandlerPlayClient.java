/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017 Team Pepsi
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from Team Pepsi.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: Team Pepsi), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.daporkchop.pepsimod.wdl.mixin.client.network;

import net.daporkchop.pepsimod.wdl.WDLHooks;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketBlockAction;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketMaps;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public abstract class MixinNetHandlerPlayClient {
    @Inject(method = "handleChat", at = @At("RETURN"))
    public void postHandleChat(SPacketChat chat, CallbackInfo callbackInfo) {
        WDLHooks.onNHPCHandleChat(NetHandlerPlayClient.class.cast(this), chat);
    }

    @Inject(method = "handleBlockAction", at = @At("RETURN"))
    public void postHandleBlockAction(SPacketBlockAction action, CallbackInfo callbackInfo) {
        WDLHooks.onNHPCHandleBlockAction(NetHandlerPlayClient.class.cast(this), action);
    }

    @Inject(method = "handleMaps", at = @At("RETURN"))
    public void postHandleMaps(SPacketMaps maps, CallbackInfo callbackInfo) {
        WDLHooks.onNHPCHandleMaps(NetHandlerPlayClient.class.cast(this), maps);
    }

    @Inject(method = "handleCustomPayload", at = @At("RETURN"))
    public void postHandleCustomPayload(SPacketCustomPayload payload, CallbackInfo callbackInfo) {
        WDLHooks.onNHPCHandleCustomPayload(NetHandlerPlayClient.class.cast(this), payload);
    }
}
