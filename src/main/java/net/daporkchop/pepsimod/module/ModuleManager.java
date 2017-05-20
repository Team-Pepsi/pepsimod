package net.daporkchop.pepsimod.module;

import net.daporkchop.pepsimod.module.api.Module;

import java.util.ArrayList;

public class ModuleManager {

    /**
     * All modules that are registered
     */
    public static final ArrayList<Module> AVALIBLE_MODULES = new ArrayList<>();

    /**
     * All modules that are currently enabled
     */
    public static final ArrayList<Module> ENABLED_MODULES = new ArrayList<>();

    /**
     * Adds a module to the registry
     *
     * @param toRegister the Module to register
     * @return the Module passed to the function
     */
    public static final Module registerModule(Module toRegister) {
        AVALIBLE_MODULES.add(toRegister);
        if (toRegister.isEnabled) {
            ENABLED_MODULES.add(toRegister);
        }
        return toRegister;
    }

    /**
     * Enables a module
     *
     * @param toEnable the module to enable
     * @return the enabled module
     */
    public static final Module enableModule(Module toEnable) {
        if (!ENABLED_MODULES.contains(toEnable)) {
            if (AVALIBLE_MODULES.contains(toEnable)) {
                ENABLED_MODULES.add(toEnable);
                toEnable.setEnabled(true);
            } else {
                throw new IllegalStateException("Attempted to enable an unregistered Module!");
            }
        }
        return toEnable;
    }

    /**
     * Disables a module
     *
     * @param toDisable the module to disable
     * @return the disabled module
     */
    public static final Module disableModule(Module toDisable) {
        if (toDisable.isEnabled && ENABLED_MODULES.contains(toDisable)) {
            if (AVALIBLE_MODULES.contains(toDisable)) {
                ENABLED_MODULES.remove(toDisable);
                toDisable.setEnabled(false);
            } else {
                throw new IllegalStateException("Attempted to disable an unregistered Module!");
            }
        }
        return toDisable;
    }

    /**
     * Toggles a module
     *
     * @param toToggle the module to toggle
     * @return the toggled module
     */
    public static final Module toggleModule(Module toToggle) {
        if (toToggle.isEnabled) {
            disableModule(toToggle);
        } else {
            enableModule(toToggle);
        }
        return toToggle;
    }
}
