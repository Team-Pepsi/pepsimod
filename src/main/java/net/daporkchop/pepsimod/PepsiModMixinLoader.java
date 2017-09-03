package net.daporkchop.pepsimod;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import javax.annotation.Nullable;
import java.util.Map;

public class PepsiModMixinLoader implements IFMLLoadingPlugin {
    public static boolean isObfuscatedEnvironment = false;

    public PepsiModMixinLoader() {
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
        isObfuscatedEnvironment = (boolean) (Boolean) data.get("runtimeDeobfuscationEnabled");
    }

    @Override
    public String getAccessTransformerClass() {
        return "net.daporkchop.pepsimod.PepsiModAccessTransformer";
    }
}
