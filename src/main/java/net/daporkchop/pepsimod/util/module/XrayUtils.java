package net.daporkchop.pepsimod.util.module;

import net.daporkchop.pepsimod.util.PepsiUtils;
import net.minecraft.block.Block;

import java.util.ArrayList;

public class XrayUtils {
    public static ArrayList<Integer> target_blocks = new ArrayList<>();

    /**
     * checks if a block should be rendered by xray
     */
    public static boolean isTargeted(Block block) {
        int id = PepsiUtils.getBlockId(block);
        for (Integer i : target_blocks) {
            if (i == id) {
                return true;
            }
        }

        return false;
    }
}
