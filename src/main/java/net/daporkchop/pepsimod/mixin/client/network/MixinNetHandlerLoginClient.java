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

package net.daporkchop.pepsimod.mixin.client.network;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.util.MCLeaks;
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

    { // kek look at this hack
        this.networkManager = null;
    }

    @Inject(method = "handleEncryptionRequest", at = @At("HEAD"), cancellable = true)
    // setting cancellable to true so that we can make the method return prematurely if need be
    public void handleEncryptionRequest(SPacketEncryptionRequest packetIn, CallbackInfo ci) {
        if (PepsiMod.INSTANCE.isMcLeaksAccount) {
            MCLeaks.joinServerStuff(packetIn, this.networkManager);
            ci.cancel(); // prevent vanilla auth code from running afterwards
            // https://github.com/SpongePowered/Mixin/wiki/Advanced-Mixin-Usage---Callback-Injectors#3-cancellable-injections
        }
    }
}
