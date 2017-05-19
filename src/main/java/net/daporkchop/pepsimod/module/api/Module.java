package net.daporkchop.pepsimod.module.api;

import net.daporkchop.pepsimod.module.api.option.OptionTypeBoolean;
import net.daporkchop.pepsimod.util.colors.ColorizedText;
import net.daporkchop.pepsimod.util.colors.rainbow.RainbowText;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;

public abstract class Module {
    public boolean isEnabled;
    public String name;
    public KeyBinding keybind;
    public boolean hide;
    public ColorizedText text;
    public ModuleOption[] options;

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
        this.getOptionByName("enabled").setValue(this.isEnabled);
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
        this.getOptionByName("enabled").setValue(this.isEnabled);
        return isEnabled;
    }

    /**
     * Gets all the default module options
     * @return all the default module options
     */
    public final ModuleOption[] defaultOptions()    {
        return ArrayUtils.addAll(new ModuleOption[] {new OptionTypeBoolean(false,"enabled"), new OptionTypeBoolean(false,"hidden")}, this.getDefaultOptions());
    }

    /**
     * Gets a ModuleOption by name
     * @param name the name to search for
     * @return a ModuleOption by the given name, null if there was nothing with the name
     */
    public ModuleOption getOptionByName(String name)    {
        for (ModuleOption moduleOption : this.options)  {
            if (moduleOption.getName().equals(name))    {
                return moduleOption;
            }
        }

        return null;
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

    /**
     * Module specific module settings
     */
    protected abstract ModuleOption[] getDefaultOptions();
}
