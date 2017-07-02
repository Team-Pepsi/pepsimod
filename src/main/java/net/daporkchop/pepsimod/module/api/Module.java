package net.daporkchop.pepsimod.module.api;

import net.daporkchop.pepsimod.module.api.option.OptionTypeBoolean;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.colors.ColorizedText;
import net.daporkchop.pepsimod.util.colors.rainbow.RainbowText;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;

public abstract class Module {
    public final String name;
    public boolean isEnabled;
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
     *
     * @return all the default module options
     */
    public final ModuleOption[] defaultOptions() {
        return ArrayUtils.addAll(new ModuleOption[]{new OptionTypeBoolean(false, "enabled"), new OptionTypeBoolean(false, "hidden")}, this.getDefaultOptions());
    }

    /**
     * Handles base initialization logic after minecraft is started
     */
    public final void doInit() {
        if (hasModeInName()) {
            updateName();
        } else {
            this.text = new RainbowText(this.name);
        }
        this.init();
    }

    /**
     * Gets a ModuleOption by name
     *
     * @param name the name to search for
     * @return a ModuleOption by the given name, null if there was nothing with the name
     */
    public ModuleOption getOptionByName(String name) {
        for (ModuleOption moduleOption : this.options) {
            if (moduleOption.getName().equals(name)) {
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
     * Called when minecraft is started
     * DO NOT CALL THIS YOURSELF, ModuleManager DOES THAT FOR YOU!!!
     */
    public abstract void init();

    /**
     * Module specific module settings
     */
    protected abstract ModuleOption[] getDefaultOptions();

    /**
     * Called directly after a packet is recieved, before it's processed
     *
     * @return if true, the packet will be ignored by vanilla
     */
    public boolean preRecievePacket(Packet<?> packetIn) {
        return false;
    }

    /**
     * Called after a packet is recieved, after it's been processed
     */
    public void postRecievePacket(Packet<?> packetIn) {

    }

    /**
     * Called right before a packet is sent
     *
     * @return if true, the packet will not be sent
     */
    public boolean preSendPacket(Packet<?> packetIn) {
        return false;
    }

    /**
     * Called after a packet is sent
     */
    public void postSendPacket(Packet<?> packetIn) {

    }

    /**
     * Whether or not extra info should show in the name
     * e.g.
     * Criticals [Packet]
     */
    public boolean hasModeInName() {
        return false;
    }

    /**
     * Whether or not extra info should show in the name
     * e.g.
     * Criticals [Packet]
     * <p>
     * This method returned "Packet"
     */
    public String getModeForName() {
        return "";
    }

    /**
     * Updates the module's name
     * Does nothing if the module has no custom name
     */
    public void updateName() {
        if (hasModeInName()) {
            text = new RainbowText(name + PepsiUtils.COLOR_ESCAPE + "customa8a8a8 [" + getModeForName() + "]");
        }
    }
}
