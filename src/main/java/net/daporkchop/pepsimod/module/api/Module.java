package net.daporkchop.pepsimod.module.api;

import net.daporkchop.pepsimod.util.colors.ColorizedText;
import net.daporkchop.pepsimod.util.colors.rainbow.RainbowText;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public abstract class Module {
    public boolean isEnabled;
    public String name;
    public KeyBinding keybind;
    public boolean hide;
    public ColorizedText text;

    public Module(boolean def, String name, int keybind, boolean hide) {
        this.isEnabled = def;
        this.name = name;
        this.keybind = new KeyBinding(name, keybind == -1 ? Keyboard.KEY_NONE : keybind, "key.categories.pepsimod");
        ClientRegistry.registerKeyBinding(this.keybind);
        if (def) {
            this.onEnable();
        } else {
            this.onDisable();
        }
        this.hide = hide;
        text = new RainbowText(name);
    }

    /**
     * Toggles a Module
     *
     * @return the new status
     */
    public boolean toggle() {
        this.isEnabled = !this.isEnabled;
        if (this.isEnabled) {
            this.onEnable();
        } else {
            this.onDisable();
        }
        return this.isEnabled;
    }

    /**
     * Enables or disables a Module
     *
     * @param isEnabled the new status
     * @return the given argument
     */
    public boolean setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
        if (this.isEnabled) {
            this.onEnable();
        } else {
            this.onDisable();
        }
        return isEnabled;
    }

    /**
     * Called on all enabled modules once per client tick
     */
    public abstract void tick();

    /**
     * Called when the Module is enabled
     */
    public abstract void onEnable();

    /**
     * Called when the Module is disabled
     */
    public abstract void onDisable();

    /**
     * Called when the module is registered
     * DO NOT CALL THIS YOURSELF, ModuleManager DOES THAT FOR YOU!!!
     */
    public abstract void init();
}
