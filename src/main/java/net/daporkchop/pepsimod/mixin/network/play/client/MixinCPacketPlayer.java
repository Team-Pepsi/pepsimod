/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2019 DaPorkchop_
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

package net.daporkchop.pepsimod.mixin.network.play.client;

import net.daporkchop.pepsimod.module.impl.misc.AntiHungerMod;
import net.daporkchop.pepsimod.module.impl.misc.NoFallMod;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;

@Mixin(CPacketPlayer.class)
public abstract class MixinCPacketPlayer {
    @Shadow
    protected boolean onGround;

    @Overwrite
    public void writePacketData(PacketBuffer buf) throws IOException {
        if (NoFallMod.NO_FALL && AntiHungerMod.ANTI_HUNGER) {
            buf.writeByte(this.onGround ? 0 : 1);
            /*
             * This inverts the value sent to the server
             * If the player is falling, it says that it's on the ground
             * And antihunger will run while the player's on the ground (it says it's not on the ground)
             */
        } else if (NoFallMod.NO_FALL) {
            buf.writeByte(1); //on ground
        } else if (AntiHungerMod.ANTI_HUNGER) {
            buf.writeByte(0); //on ground
        } else {
            buf.writeByte(this.onGround ? 1 : 0);
        }
    }
}
