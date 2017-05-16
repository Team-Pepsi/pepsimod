package net.daporkchop.pepsimod;

import net.minecraft.util.Session;
import net.minecraftforge.fml.common.Mod;

@Mod(name = "PepsiMod", modid = "pepsimod", version = "11.0")
public class PepsiMod {
    public static PepsiMod INSTANCE;

    public boolean isMcLeaksAccount = false;
    public Session originalSession = null;

    {
        INSTANCE = this;
    }
}
