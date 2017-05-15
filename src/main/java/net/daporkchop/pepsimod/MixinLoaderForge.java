package net.daporkchop.pepsimod;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Our {@link MixinLoader} class doesn't seem to work properly through a development environment, so we will use this
 * as our tweak class instead when we're in that context.
 * <p>
 * Compiled versions of the client will use {@link MixinLoader} without any trouble.
 */
public class MixinLoaderForge implements IFMLLoadingPlugin {

    public MixinLoaderForge() {
        System.out.println("\n\n\nPepsiMod Init\n\n\n");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.pepsimod.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
