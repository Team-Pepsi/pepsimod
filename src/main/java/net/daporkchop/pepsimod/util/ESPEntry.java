package net.daporkchop.pepsimod.util;

import net.minecraft.util.math.BlockPos;

public class ESPEntry {
    public BlockPos start;
    public BlockPos end;

    public ESPEntry(BlockPos start, BlockPos end)   {
        this.start = start;
        this.end = end;
    }

    public ESPEntry(BlockPos pos)   {
        start = pos;
        end = pos;
    }
}
