package team.pepsi.pepsimod.launcher;

import javax.annotation.Nullable;
import java.util.Map;

public interface IPepsiModMixinLoader {
    String[] getASMTransformerClass();

    String getModContainerClass();

    @Nullable
    String getSetupClass();

    void injectData(Map<String, Object> data);

    String getAccessTransformerClass();

    void inALoadingPluginThisWouldBeTheConstructor(LauncherMixinLoader loader);
}
