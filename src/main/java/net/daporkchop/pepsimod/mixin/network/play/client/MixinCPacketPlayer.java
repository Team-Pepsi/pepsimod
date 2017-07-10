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

    /**
     * this hides a stupid warning
     */
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
