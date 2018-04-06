/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2018 DaPorkchop_
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from DaPorkchop_.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original author of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.daporkchop.pepsimod.module;

import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleSortType;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.config.impl.GeneralTranslator;
import net.minecraft.network.Packet;

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
        if (toDisable.state.enabled && ENABLED_MODULES.contains(toDisable)) {
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
    public static final Module getModuleByName(String name) {
        for (Module module : AVALIBLE_MODULES) {
            if (module.name.equals(name)) {
                return module;
            }
        }

        return null;
    }

    public static final void sortModules(ModuleSortType type) {
        GeneralTranslator.INSTANCE.sortType = type;
        switch (type) {
            case ALPHABETICAL:
                ArrayList<Module> tempArrayList = (ArrayList<Module>) ENABLED_MODULES.clone();
                ArrayList<Module> newArrayList = new ArrayList<>();
                ESCAPE:
                for (Module module : tempArrayList) {
                    for (int i = 0; i < newArrayList.size(); i++) {
                        if (module.name.compareTo(newArrayList.get(i).name) < 0) {
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
                    if (module.text == null) {
                        return;
                    }
                    for (int i = 0; i < newArrayList1.size(); i++) {
                        Module existingModule = newArrayList1.get(i);
                        if (module.text.width() > existingModule.text.width()) {
                            newArrayList1.add(i, module);
                            continue ESCAPE;
                        } else if (module.text.width() == existingModule.text.width()) {
                            if (module.name.compareTo(existingModule.name) < 0) {
                                newArrayList1.add(i, module);
                                continue ESCAPE;
                            }
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
