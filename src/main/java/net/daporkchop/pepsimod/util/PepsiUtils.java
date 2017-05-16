package net.daporkchop.pepsimod.util;

import net.minecraft.client.multiplayer.ServerData;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by DaPorkchop_ on 5/16/2017.
 */
public class PepsiUtils {
    public static String buttonPrefix = "§c";
    public static final String[] colorCodes = new String[] {"c", "9", "f", "1", "4"};

    private static final Random random = new Random(System.currentTimeMillis());

    public static final char COLOR_ESCAPE = '\u00A7';

    public static final Timer timer = new Timer();

    public static final ServerData TOOBEETOOTEE_DATA = new ServerData("toobeetootee", "2b2t.org", false);

    static {
        TOOBEETOOTEE_DATA.setResourceMode(ServerData.ServerResourceMode.PROMPT);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {// random colors
                PepsiUtils.buttonPrefix = PepsiUtils.COLOR_ESCAPE + colorCodes[PepsiUtils.rand(0, PepsiUtils.colorCodes.length)];
            }
        }, 1000, 1000);
    }

    /**
     * Returns a random number between min (inkl.) and max (excl.) If you want a number between 1 and 4 (inkl) you need to call rand (1, 5)
     *
     * @param min min inklusive value
     * @param max max exclusive value
     * @return
     */
    public static int rand(int min, int max) {
        if (min == max) {
            return max;
        }
        return min + random.nextInt(max - min);
    }

    /**
     * Returns random boolean
     * @return a boolean random value either <code>true</code> or <code>false</code>
     */
    public static boolean rand() {
        return random.nextBoolean();
    }
}
