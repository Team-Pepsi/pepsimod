package net.daporkchop.pepsimod.module;

import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleSortType;
import net.daporkchop.pepsimod.util.PepsiUtils;

import java.util.ArrayList;

public class ModuleManager {

    /**
     * All modules that are registered
     */
    public static ArrayList<Module> AVALIBLE_MODULES = new ArrayList<>();

    /**
     * All modules that are currently enabled
     */
    public static ArrayList<Module> ENABLED_MODULES = new ArrayList<>();

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

    /**
     * Gets a module by it's name
     *
     * @param name the module's name
     * @return a module, or null if nothing was found
     */
    public static final Module getModuleByName(String name) {
        for (Module module : AVALIBLE_MODULES) {
            if (module.name.equals(name)) {
                return module;
            }
        }

        return null;
    }

    public static final void sortModules(ModuleSortType type) {
        switch (type) {
            case ALPHABETICAL:
                ArrayList<Module> tempArrayList = (ArrayList<Module>) ENABLED_MODULES.clone();
                ArrayList<Module> newArrayList = new ArrayList<>();
                ESCAPE:
                for (Module module : tempArrayList) {
                    for (int i = 0; i < newArrayList.size(); i++) {
                        if (module.name.compareTo(newArrayList.get(i).name) > 0) {
                            newArrayList.add(i, module);
                            continue ESCAPE;
                        }
                    }
                    newArrayList.add(module);
                }
                ENABLED_MODULES = newArrayList;
                break;
            case DEFAULT: //hehe do nothing lol
                break;
            case SIZE:
                ArrayList<Module> tempArrayList1 = (ArrayList<Module>) ENABLED_MODULES.clone();
                ArrayList<Module> newArrayList1 = new ArrayList<>();
                ESCAPE:
                for (Module module : tempArrayList1) {
                    for (int i = 0; i < newArrayList1.size(); i++) {
                        Module existingModule = newArrayList1.get(i);
                        if (module.text.width() <= existingModule.text.width()) {
                            newArrayList1.add(i, module);
                            continue ESCAPE;
                        }
                    }
                    newArrayList1.add(module);
                }
                ENABLED_MODULES = newArrayList1;
                break;
            case RANDOM:
                ArrayList<Module> tempArrayList2 = (ArrayList<Module>) ENABLED_MODULES.clone();
                ArrayList<Module> newArrayList2 = new ArrayList<>();
                ESCAPE:
                for (Module module : tempArrayList2) {
                    newArrayList2.add(PepsiUtils.rand(0, newArrayList2.size()), module);
                }
                ENABLED_MODULES = newArrayList2;
        }
    }
}
