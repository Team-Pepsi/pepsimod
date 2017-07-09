package net.daporkchop.pepsimod.misc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketTimeUpdate;

public class TickRate {
    public static float TPS = 20.0f;

    public static long lastUpdate = -1;

    public static void update(Packet packet) {
        if (!(packet instanceof SPacketTimeUpdate)) {
            return;
        }
        if (lastUpdate == -1) {
            lastUpdate = System.currentTimeMillis();
            return;
        }
        System.out.println("time update! " + packet.getClass().getCanonicalName());

        long currentTime = System.currentTimeMillis();
        //TODO: finish this
    }
}
