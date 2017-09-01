package team.pepsi.pepsimod.launcher;

import net.daporkchop.pepsimod.PepsiMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(name = "pepsimod", modid = "pepsimod", version = "0.1")
public class PepsiModLauncher {
    public static PepsiMod pepsimodInstance;
    public static Logger logger;

    public PepsiModLauncher() {
        pepsimodInstance = new PepsiMod();
    }

    @Mod.EventHandler
    public void construct(FMLConstructionEvent event) {
        System.out.println("FMLConstructionEvent");
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("FMLPreInitializationEvent");
        /*try {
            LauncherMixinLoader.preInit.invoke(pepsimodInstance, event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }*/
        pepsimodInstance.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info("FMLInitializationEvent");
        /*try {
            LauncherMixinLoader.init.invoke(pepsimodInstance, event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }*/
        pepsimodInstance.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        logger.info("FMLPostInitializationEvent");
        /*try {
            LauncherMixinLoader.postInit.invoke(pepsimodInstance, event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }*/
        pepsimodInstance.postInit(event);
    }
}
