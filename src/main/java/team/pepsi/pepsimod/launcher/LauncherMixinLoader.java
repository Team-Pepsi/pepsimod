package team.pepsi.pepsimod.launcher;

import net.daporkchop.pepsimod.PepsiModMixinLoader;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import sun.misc.Unsafe;
import team.pepsi.pepsimod.launcher.classloading.PepsiModClassLoader;
import team.pepsi.pepsimod.launcher.util.Zlib;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class LauncherMixinLoader implements IFMLLoadingPlugin {
    public static boolean isObfuscatedEnvironment = false;
    public static PepsiModClassLoader classLoader;
    public static Method preInit = null, init = null, postInit = null;
    public static ArrayList<String> loadingClasses = new ArrayList<>();
    public IPepsiModMixinLoader coremod;

    public LauncherMixinLoader() {
        try {
            Map<String, byte[]> classes = PepsiModServerManager.downloadPepsiMod();
            classLoader = new PepsiModClassLoader(new URL[0], getClass().getClassLoader(), classes);
            Zlib.class.getCanonicalName(); //this loads the Zlib class to memory
            Field parent = ClassLoader.class.getDeclaredField("parent");
            long offset = getUnsafe().objectFieldOffset(parent);
            getUnsafe().putObject(getClass().getClassLoader(), offset, classLoader);
            coremod = new PepsiModMixinLoader();
            coremod.inALoadingPluginThisWouldBeTheConstructor(this);
            Field resourceCache = LaunchClassLoader.class.getDeclaredField("resourceCache");
            resourceCache.setAccessible(true);
            Map<String, byte[]> classCache = (Map<String, byte[]>) resourceCache.get(Launch.classLoader);
            FMLLog.log.info("Initial size: " + classCache.size());
            for (Map.Entry<String, byte[]> entry : classes.entrySet()) {
                classCache.put(entry.getKey(), Zlib.inflate(entry.getValue()));
            }
            FMLLog.log.info("Size after: " + ((Map<String, byte[]>) resourceCache.get(Launch.classLoader)).size());
        } catch (Throwable t) {
            t.printStackTrace();
            System.out.println("FATAL ERROR IN PEPSIMOD LAUNCHER, SYSTEM WILL EXIT NOW!!!");
            Runtime.getRuntime().exit(0);
        }
    }

    public static Unsafe getUnsafe() {
        try {
            FMLLog.log.info("Getting field");
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            FMLLog.log.info("Setting field to be accessible");
            f.setAccessible(true);
            FMLLog.log.info("Getting value");
            return (Unsafe) f.get(null);
        } catch (Exception e) {
            e.printStackTrace();
            Runtime.getRuntime().exit(928273);
        }

        return null;
    }

    public static Class<?> tryLoadingClassAsMainLoader(String name) throws ClassNotFoundException {
        if (loadingClasses.contains(name)) {
            throw new ClassNotFoundException("CLASS NOT FOUND ON SECOND ITERATION! " + name);
        }
        loadingClasses.add(name);
        Class<?> toReturn = null;
        toReturn = Launch.classLoader.loadClass(name);
        if (toReturn == null) {
            FMLLog.log.info("Unable to load class " + name + " with Launch.classLoader");
            toReturn = LauncherMixinLoader.class.getClassLoader().loadClass(name);
        }
        if (toReturn == null) {
            FMLLog.log.info("Failed to load class " + name);
            throw new ClassNotFoundException("unable to find class");
        }
        loadingClasses.remove(name);
        return toReturn;
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{"team.pepsi.pepsimod.launcher.ClassLoadingNotifier"};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return coremod.getSetupClass();
    }

    @Override
    public void injectData(Map<String, Object> data) {
        isObfuscatedEnvironment = (boolean) (Boolean) data.get("runtimeDeobfuscationEnabled");
        coremod.injectData(data);
    }

    @Override
    public String getAccessTransformerClass() {
        return coremod.getAccessTransformerClass();
    }
}
