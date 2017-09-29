/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017 Team Pepsi
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from Team Pepsi.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: Team Pepsi), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.daporkchop.pepsimod.module.impl.misc;

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.OptionCompletions;
import net.daporkchop.pepsimod.module.api.option.ExtensionSlider;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketCustomPayload;

public class HUDMod extends Module {
    public static HUDMod INSTANCE;
    public String serverBrand = "";

    public HUDMod(boolean isEnabled, int key) {
        super(isEnabled, "HUD", key, true);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean shouldTick() {
        return true;
    }

    @Override
    public void tick() {
        for (Module module : ModuleManager.ENABLED_MODULES) {
            module.updateName();
        }

        ModuleManager.sortModules(ModuleManager.sortType);
    }

    @Override
    public void init() {
        INSTANCE = this;
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new ModuleOption<>(pepsiMod.hudSettings.drawLogo, "draw_logo", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.hudSettings.drawLogo = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.hudSettings.drawLogo;
                        }, "Watermark"),
                new ModuleOption<>(pepsiMod.hudSettings.arrayList, "arraylist", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.hudSettings.arrayList = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.hudSettings.arrayList;
                        }, "ArrayList"),
                new ModuleOption<>(pepsiMod.hudSettings.TPS, "tps", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.hudSettings.TPS = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.hudSettings.TPS;
                        }, "TPS"),
                new ModuleOption<>(pepsiMod.hudSettings.coords, "coords", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.hudSettings.coords = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.hudSettings.coords;
                        }, "Coords"),
                new ModuleOption<>(pepsiMod.hudSettings.netherCoords, "nether_coords", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.hudSettings.netherCoords = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.hudSettings.netherCoords;
                        }, "NetherCoords"),
                new ModuleOption<>(pepsiMod.hudSettings.arrayListTop, "arraylist_top", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.hudSettings.arrayListTop = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.hudSettings.arrayListTop;
                        }, "ArrayListOnTop"),
                new ModuleOption<>(pepsiMod.hudSettings.serverBrand, "server_brand", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.hudSettings.serverBrand = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.hudSettings.serverBrand;
                        }, "ServerBrand"),
                new ModuleOption<>(pepsiMod.hudSettings.rainbow, "rainbow", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.hudSettings.rainbow = value;
                            for (Module module : ModuleManager.AVALIBLE_MODULES) {
                                module.updateName();
                            }
                            return true;
                        },
                        () -> {
                            return pepsiMod.hudSettings.rainbow;
                        }, "Rainbow"),
                new ModuleOption<>(pepsiMod.hudSettings.direction, "direction", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.hudSettings.direction = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.hudSettings.direction;
                        }, "Direction"),
                new ModuleOption<>(pepsiMod.hudSettings.armor, "armor", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.hudSettings.armor = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.hudSettings.armor;
                        }, "Armor"),
                new ModuleOption<>(pepsiMod.hudSettings.effects, "effects", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.hudSettings.effects = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.hudSettings.effects;
                        }, "Effects"),
                new ModuleOption<>(pepsiMod.hudSettings.fps, "fps", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.hudSettings.fps = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.hudSettings.fps;
                        }, "FPS"),
                new ModuleOption<>(pepsiMod.hudSettings.ping, "ping", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.hudSettings.ping = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.hudSettings.ping;
                        }, "Ping"),
                new ModuleOption<>(pepsiMod.hudSettings.r, "r", new String[]{"0", "128", "255"},
                        (value) -> {
                            pepsiMod.hudSettings.r = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.hudSettings.r;
                        }, "Red", new ExtensionSlider(ExtensionType.VALUE_INT, 0, 255, 1)),
                new ModuleOption<>(pepsiMod.hudSettings.g, "g", new String[]{"0", "128", "255"},
                        (value) -> {
                            pepsiMod.hudSettings.g = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.hudSettings.g;
                        }, "Green", new ExtensionSlider(ExtensionType.VALUE_INT, 0, 255, 1)),
                new ModuleOption<>(pepsiMod.hudSettings.b, "b", new String[]{"0", "128", "255"},
                        (value) -> {
                            pepsiMod.hudSettings.b = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.hudSettings.b;
                        }, "Blue", new ExtensionSlider(ExtensionType.VALUE_INT, 0, 255, 1))
        };
    }

    @Override
    public boolean preRecievePacket(Packet packet)  {
        if (packet instanceof SPacketCustomPayload) {
            if (((SPacketCustomPayload) packet).getChannelName().equals("MC|Brand"))    {
                serverBrand = ((SPacketCustomPayload) packet).getBufferData().readString(32767);
            }
        }
        return false;
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.MISC;
    }

    public void registerKeybind(String name, int key) {
    }
}
