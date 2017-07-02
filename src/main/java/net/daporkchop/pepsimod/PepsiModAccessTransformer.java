package net.daporkchop.pepsimod;

import net.minecraftforge.fml.common.asm.transformers.AccessTransformer;

import java.io.IOException;

public class PepsiModAccessTransformer extends AccessTransformer {
    public PepsiModAccessTransformer() throws IOException {
        super("pepsimod_at.cfg");
    }
}
