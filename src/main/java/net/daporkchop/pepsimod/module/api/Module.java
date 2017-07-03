package net.daporkchop.pepsimod.module.api;

import net.daporkchop.pepsimod.command.CommandRegistry;
import net.daporkchop.pepsimod.command.api.Command;
import net.daporkchop.pepsimod.module.api.option.OptionTypeBoolean;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.colors.ColorizedText;
import net.daporkchop.pepsimod.util.colors.rainbow.RainbowText;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;

/**
 * hehe this is actually a command
 * but it's a module
 * gl understanding my overly complicated class heirachy
 */
public abstract class Module extends Command {
    public boolean isEnabled;
    public KeyBinding keybind;
    public boolean hide;
    public ColorizedText text;
    public ModuleOption[] options;
    public String nameFull;
    public String[] completionOptions;

    public Module(boolean def, String name, int keybind, boolean hide) {
        super(name.toLowerCase());
        nameFull = name;
        this.isEnabled = def;
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
            this.text = new RainbowText(this.nameFull);
        }
        CommandRegistry.registerCommand(this);
        ArrayList<String> temp = new ArrayList<>();
        for (ModuleOption option : options) {
            temp.add(option.getName());
        }
        temp.add("toggle");
        temp.add("list");
        temp.add("enabled");
        completionOptions = temp.toArray(new String[temp.size()]);
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
            text = new RainbowText(nameFull + PepsiUtils.COLOR_ESCAPE + "customa8a8a8 [" + getModeForName() + "]");
        }
    }

    public String getSuggestion(String cmd, String[] args) {
        switch (args.length) {
            case 1:
                return "." + name + " " + completionOptions[0];
            case 2:
                if (args[1].isEmpty()) {
                    return "." + name + " " + completionOptions[0];
                }
                for (String mode : completionOptions) {
                    if (mode.equals(args[1])) {
                        ModuleOption option = getOptionByName(args[1]);
                        if (option == null) {
                            return "";
                        } else {
                            return cmd + " " + option.getDefaultValue();
                        }
                    } else if (mode.startsWith(args[1])) {
                        return "." + name + " " + mode;
                    }
                }
            case 3:
                if (args[2].isEmpty()) {
                    ModuleOption option = getOptionByName(args[1].trim());
                    if (option == null) {
                        return "";
                    } else {
                        return cmd + option.getDefaultValue();
                    }
                }
                ModuleOption option = getOptionByName(args[1]);
                if (option.getDefaultValue().toString().startsWith(args[2])) {
                    return args[0] + " " + args[1] + " " + option.getDefaultValue();
                } else {
                    return "";
                }
        }

        return "." + name;
    }

    public void execute(String cmd, String[] args) {
        //TODO
    }
}
