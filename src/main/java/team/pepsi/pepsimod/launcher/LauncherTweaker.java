package team.pepsi.pepsimod.launcher;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

public class LauncherTweaker implements ITweaker {
    public LauncherTweaker() {
        try {
            Method m = MixinBootstrap.class.getDeclaredMethod("start");
            m.setAccessible(true);
            m.invoke(null);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void acceptOptions(List<String> args, File gameDir, final File assetsDir, String profile) {
        try {
            Method m = MixinBootstrap.class.getDeclaredMethod("doInit", List.class);
            m.setAccessible(true);
            m.invoke(null, args);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        classLoader.addClassLoaderExclusion("net.daporkchop.pepsimod.");
        classLoader.addClassLoaderExclusion("team.pepsi.pepsimod.");
        try {
            Method m = MixinBootstrap.class.getDeclaredMethod("injectIntoClassLoader", LaunchClassLoader.class);
            m.setAccessible(true);
            m.invoke(null, classLoader);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    public String[] getLaunchArguments() {
        return new String[0];
    }
}
