package net.daporkchop.pepsimod.mixin.network.play.client;

import net.daporkchop.pepsimod.module.impl.AntiHunger;
import net.daporkchop.pepsimod.module.impl.NoFall;
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
        if (NoFall.NO_FALL && AntiHunger.ANTI_HUNGER) {
            buf.writeByte(this.onGround ? 0 : 1);
            /*
             * This inverts the value sent to the server
             * If the player is falling, it says that it's on the ground
             * And antihunger will run while the player's on the ground (it says it's not on the ground)
             */
        } else if (NoFall.NO_FALL) {
            buf.writeByte(1); //on ground
        } else if (AntiHunger.ANTI_HUNGER) {
            buf.writeByte(0); //on ground
        } else {
            buf.writeByte(this.onGround ? 1 : 0);
        }
    }
}
