package net.daporkchop.pepsimod;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MixinLoader implements ITweaker {
    private ArrayList<String> args = new ArrayList<>();

    private boolean forge = false;

    @Override
    public void acceptOptions(List<String> list, File gameDir, File assetsDir, String profile) {
        System.out.println("\n\n\nPepsiMod AcceptOptions\n\n\n");
        args.addAll(list);
        if (args.contains("--ignoreme")) {
            forge = true;
            args.remove("--ignoreme");
        }
        if (!args.contains("--version") && profile != null) {
            this.args.add("--version");
            this.args.add(profile);
        }
        if (!args.contains("--assetsDir") && assetsDir != null) {
            this.args.add("--assetsDir");
            this.args.add(assetsDir.getPath());
        }
        if (!args.contains("--gameDir") && gameDir != null) {
            this.args.add("--gameDir");
            this.args.add(gameDir.getPath());
        }
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        System.out.println("\n\n\nPepsiMod Inject Start\n\n\n");
        MixinBootstrap.init();
        /*
          Check for Forge's GuiIngame class.  If it is found, then we will tell the Mixin library to use the searge
		  obfuscation context, since Forge jars are obfuscated differently.
		 */
        Mixins.addConfiguration("mixins.pepsimod.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        try {
            Class.forName("net.minecraftforge.client.GuiIngameForge");
        } catch (ClassNotFoundException e) {
            System.out.println("Forge not found!");
            MixinEnvironment.getDefaultEnvironment().setObfuscationContext("notch");
        }
        MixinEnvironment.getDefaultEnvironment().setSide(MixinEnvironment.Side.CLIENT);
        System.out.println("\n\n\nPepsiMod Inject Finish\n\n\n");
    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    @Override
    public String[] getLaunchArguments() {
        return forge ? new String[0] : args.toArray(new String[args.size()]);
    }
}
