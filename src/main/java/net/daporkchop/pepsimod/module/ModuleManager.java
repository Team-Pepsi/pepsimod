/*
 * Adapted from The MIT License (MIT)
 *
 * Copyright (c) 2016-2021 DaPorkchop_
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * Any persons and/or organizations using this software must include the above copyright notice and this permission notice,
 * provide sufficient credit to the original authors of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.daporkchop.pepsimod.module;

import net.daporkchop.lib.common.util.PArrays;
import net.daporkchop.pepsimod.Pepsimod;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleSortType;
import net.daporkchop.pepsimod.util.PepsiConstants;
import net.daporkchop.pepsimod.util.config.impl.GeneralTranslator;
import net.minecraft.network.Packet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class ModuleManager {

    /**
     * All modules that are registered
     */
    public static ArrayList<Module> AVALIBLE_MODULES = new ArrayList<>();

    /**
     * All modules that are currently enabled
     */
    public static List<Module> ENABLED_MODULES = new CopyOnWriteArrayList<>();

    /**
     * Adds a module to the registry
     *
     * @param toRegister the Module to register
     * @return the Module passed to the function
     */
    public static Module registerModule(Module toRegister) {
        if (toRegister.shouldRegister()) {
            AVALIBLE_MODULES.add(toRegister);
            if (toRegister.state.enabled) {
                enableModule(toRegister);
            } else {
                disableModule(toRegister);
            }
        }
        return toRegister;
    }

    public static void registerModules(Module... toRegister) {
        for (Module module : toRegister)    {
            registerModule(module);
        }
    }

    public static void unRegister(Module module) {
        if (AVALIBLE_MODULES.contains(module)) {
            AVALIBLE_MODULES.remove(module);
            ENABLED_MODULES.remove(module);
        }
    }

    /**
     * Enables a module
     *
     * @param toEnable the module to enable
     * @return the enabled module
     */
    public static Module enableModule(Module toEnable) {
        if (!ENABLED_MODULES.contains(toEnable)) {
            if (AVALIBLE_MODULES.contains(toEnable)) {
                ENABLED_MODULES.add(toEnable);
                toEnable.setEnabled(true);
                sortModules(GeneralTranslator.INSTANCE.sortType);
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
    public static Module disableModule(Module toDisable) {
        if (toDisable.state.enabled && ENABLED_MODULES.contains(toDisable)) {
            if (AVALIBLE_MODULES.contains(toDisable)) {
                ENABLED_MODULES.remove(toDisable);
                toDisable.setEnabled(false);
                sortModules(GeneralTranslator.INSTANCE.sortType);
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
    public static Module toggleModule(Module toToggle) {
        if (toToggle.state.enabled) {
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
    public static Module getModuleByName(String name) {
        for (Module module : AVALIBLE_MODULES) {
            if (module.name.equals(name)) {
                return module;
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public static void sortModules(ModuleSortType type) {
        if (!PepsiConstants.pepsimod.isInitialized) {
            return;
        }

        switch (type) {
            case ALPHABETICAL:
                ENABLED_MODULES = new CopyOnWriteArrayList<>(ENABLED_MODULES.stream()
                        .sorted(Comparator.<Module, String>comparing(m -> m.name).reversed())
                        .collect(Collectors.toList()));
                break;
            case DEFAULT: //hehe do nothing lol
                break;
            case SIZE:
                ENABLED_MODULES = new CopyOnWriteArrayList<>(ENABLED_MODULES.stream()
                        .sorted(Comparator.<Module>comparingInt(m -> m.text.width()).thenComparing(Comparator.comparing(m -> m.name)).reversed())
                        .collect(Collectors.toList()));
                break;
            case RANDOM:
                Module[] modules = ENABLED_MODULES.toArray(new Module[0]);
                PArrays.shuffle(modules);
                ENABLED_MODULES = new CopyOnWriteArrayList<>(Arrays.asList(modules));
        }
    }

    public static boolean preRecievePacket(Packet<?> packetIn) {
        boolean cancel = false;
        for (Module module : ENABLED_MODULES) {
            if (module.preRecievePacket(packetIn)) {
                cancel = true;
            }
        }
        return cancel;
    }

    public static void postRecievePacket(Packet<?> packetIn) {
        for (Module module : ENABLED_MODULES) {
            module.postRecievePacket(packetIn);
        }
    }

    public static boolean preSendPacket(Packet<?> packetIn) {
        boolean cancel = false;
        for (Module module : ENABLED_MODULES) {
            if (module.preSendPacket(packetIn)) {
                cancel = true;
            }
        }
        return cancel;
    }

    public static void postSendPacket(Packet<?> packetIn) {
        for (Module module : ENABLED_MODULES) {
            module.postSendPacket(packetIn);
        }
    }
}
