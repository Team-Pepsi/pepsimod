package net.daporkchop.pepsimod.key;

import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class KeyRegistry {
    @SubscribeEvent
    public void onKeyPress(InputEvent.KeyInputEvent event) {
        for (Module module : ModuleManager.ENABLED_MODULES) {
            if (module.keybind.isPressed()) {
                ModuleManager.toggleModule(module);
            }
        }
    }
}
