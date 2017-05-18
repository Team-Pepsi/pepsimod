package net.daporkchop.pepsimod;

import net.daporkchop.pepsimod.event.GuiRenderHandler;
import net.daporkchop.pepsimod.key.KeyRegistry;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.impl.AntiHunger;
import net.daporkchop.pepsimod.module.impl.NoFall;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLStateEvent;

@Mod(name = "PepsiMod", modid = "pepsimod", version = PepsiMod.VERSION)
public class PepsiMod {
    public static final String VERSION = "11.0";
    public static PepsiMod INSTANCE;
    public boolean isMcLeaksAccount = false;
    public Session originalSession = null;
    public Minecraft mc;

    {
        INSTANCE = this;
    }

    public static void registerModules(FMLStateEvent event) {
        //TODO: save enabled status
        //TODO: save hidden status
        //i think i got keybinds, still need to test
        ModuleManager.registerModule(new NoFall(false, -1, false));
        ModuleManager.registerModule(new AntiHunger(false, -1, false));
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new KeyRegistry());
        this.mc = Minecraft.getMinecraft();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        registerModules(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new GuiRenderHandler());
    }
}
