package team.pepsi.pepsimod.launcher.classloading;

import net.minecraftforge.fml.common.FMLLog;
import team.pepsi.pepsimod.launcher.LauncherMixinLoader;
import team.pepsi.pepsimod.launcher.util.Zlib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class PepsiModClassLoader extends URLClassLoader {
    public final Map<String, byte[]> extraClassDefs;
    public Method findClass = null;

    public PepsiModClassLoader(URL[] urls, ClassLoader parent, Map<String, byte[]> extraClassDefs) {
        super(urls, parent.getParent());
        this.extraClassDefs = new HashMap<>(extraClassDefs);
        Class<?> currentLoader = parent.getClass();
        while (findClass == null) {
            FMLLog.log.info("Trying class: " + currentLoader.getCanonicalName());
            try {
                findClass = currentLoader.getDeclaredMethod("findClass", String.class);
                FMLLog.log.info("Found method in " + currentLoader.getCanonicalName());
                findClass.setAccessible(true);
                FMLLog.log.info("Set method to accessible");
            } catch (NoSuchMethodException e) {
                currentLoader = currentLoader.getSuperclass();
            }
        }
    }

    @Override
    public Class<?> findClass(final String name) throws ClassNotFoundException {
        byte[] classBytes = this.extraClassDefs.getOrDefault(name, null);
        if (classBytes != null) {
            FMLLog.log.info("[PepsiModClassLoader] loading class: " + name);
            classBytes = Zlib.inflate(classBytes);
            return defineClass(name, classBytes, 0, classBytes.length);
        }
        /*try {
            return (Class<?>) findClass.invoke(getParent(), name);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            Runtime.getRuntime().exit(0);
        }*/

        if (getParent() == null) {
            return super.findClass(name);
        } else {
            try {
                findClass.invoke(getParent(), name);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                Runtime.getRuntime().exit(9);
            } catch (InvocationTargetException e) {
                return LauncherMixinLoader.tryLoadingClassAsMainLoader(name);
            }

            FMLLog.log.error("NOT LOADING CLASS: " + name);
            return null;
        }
    }

    @Override
    public Class<?> loadClass(String var1) throws ClassNotFoundException {
        return this.loadClass(var1, false);
    }

    @Override
    public Class<?> loadClass(String var1, boolean var2) throws ClassNotFoundException {
        try {
            return super.loadClass(var1, false);
        } catch (ClassNotFoundException e) {
        }

        return LauncherMixinLoader.tryLoadingClassAsMainLoader(var1);
    }
}
