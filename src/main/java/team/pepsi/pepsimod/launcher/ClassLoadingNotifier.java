package team.pepsi.pepsimod.launcher;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.FMLLog;

public class ClassLoadingNotifier implements IClassTransformer {
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        FMLLog.log.info("Class loading: " + transformedName);
        /*try {
            if (LauncherMixinLoader.preInit == null && transformedName.endsWith(".FMLInitializationEvent")) {
                LauncherMixinLoader.preInit = Launch.classLoader.loadClass("net.daporkchop.pepsimod.PepsiMod").getDeclaredMethod("preInit", FMLPreInitializationEvent.class);
            }
            if (LauncherMixinLoader.init == null && transformedName.endsWith(".FMLPostInitializationEvent")) {
                LauncherMixinLoader.init = Launch.classLoader.loadClass("net.daporkchop.pepsimod.PepsiMod").getDeclaredMethod("init", FMLInitializationEvent.class);
            }
            if (LauncherMixinLoader.postInit == null && transformedName.endsWith(".FMLLoadCompleteEvent")) {
                LauncherMixinLoader.postInit = Launch.classLoader.loadClass("net.daporkchop.pepsimod.PepsiMod").getDeclaredMethod("postInit", FMLPostInitializationEvent.class);
            }
        } catch (NoSuchMethodException | ClassNotFoundException e)   {
            e.printStackTrace();
        }*/
        return basicClass;
    }
}
