package net.daporkchop.pepsimod.misc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketTimeUpdate;

import java.text.DecimalFormat;

public class TickRate {
    public static float TPS = 20.0f;

    public static long[] updateTimes = new long[10];

    public static float[] tpsCounts = new float[updateTimes.length - 1];

    public static DecimalFormat format = new DecimalFormat("##.##");

    static {
        for (int i = updateTimes.length - 1; i >= 0; i--) {
            updateTimes[i] = System.currentTimeMillis() - (i * 1000);
        }
    }

    public static void update(Packet packet) {
        if (!(packet instanceof SPacketTimeUpdate)) {
            return;
        }

        long currentTime = System.currentTimeMillis();

        for (int i = updateTimes.length - 1; i > 0; i--) {
            updateTimes[i] = updateTimes[i - 1]; //move everything over 1
        }
        updateTimes[0] = currentTime;

        for (int i = 0; i < updateTimes.length - 1; i++) { //TODO: make this more efficient
            long time1 = updateTimes[i];
            long time2 = updateTimes[i + 1];
            long timeDiff = time1 - time2;
            float tickTime = timeDiff / 20;
            if (tickTime == 0) {
                tickTime = 50;
            }
            float tps = 1000 / tickTime;
            if (tps > 20.0f) {
                tps = 20.0f;
            }
            tpsCounts[i] = tps;
        }

        double total = 0.0;
        for (float f : tpsCounts) {
            total += f;
        }
        total /= tpsCounts.length;

        System.out.println("tps: " + total);
        if (total > 20.0) {
            total = 20.0;
        }

        TPS = Float.parseFloat(format.format(total));
    }

    public static void reset() {
        for (int i = updateTimes.length - 1; i >= 0; i--) {
            updateTimes[i] = System.currentTimeMillis() - (i * 1000);
        }
        for (int i = 0; i < tpsCounts.length; i++) {
            tpsCounts[i] = 20.0f;
        }
        TPS = 20.0f;
    }
}
