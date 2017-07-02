package optifine;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

import java.io.File;
import java.util.List;

/**
 * based on
 * https://github.com/octarine-noise/BetterFoliage/blob/abf037d8a9640594a76b9f2524885da6f440bd41/src/main/kotlin/optifine/OptifineTweakerDevWrapper.kt
 * <p>
 * allows optifine dev jars to actually be loaded
 * actually it doesn't work
 * whatever
 * lol
 */
public class OptifineDevWrapper implements ITweaker {
    public void acceptOptions(List<String> args, File gameDir, final File assetsDir, String profile) {

    }

    public String[] getLaunchArguments() {
        return new String[0];
    }

    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        System.out.println("class loader register");
        classLoader.registerTransformer("optifine.OptifineTransformerDevWrapper");
    }
}
