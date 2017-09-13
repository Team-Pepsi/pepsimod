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

import net.daporkchop.pepsimod.PepsiMod;
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

    public HUDMod(boolean isEnabled, int key, boolean hide) {
        super(isEnabled, "HUD", key, true);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void tick() {

    }

    @Override
    public void init() {
        INSTANCE = this;
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new ModuleOption<>(PepsiMod.INSTANCE.hudSettings.drawLogo, "draw_logo", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.hudSettings.drawLogo = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.hudSettings.drawLogo;
                        }, "Watermark"),
                new ModuleOption<>(PepsiMod.INSTANCE.hudSettings.arrayList, "arraylist", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.hudSettings.arrayList = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.hudSettings.arrayList;
                        }, "ArrayList"),
                new ModuleOption<>(PepsiMod.INSTANCE.hudSettings.TPS, "tps", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.hudSettings.TPS = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.hudSettings.TPS;
                        }, "TPS"),
                new ModuleOption<>(PepsiMod.INSTANCE.hudSettings.coords, "coords", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.hudSettings.coords = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.hudSettings.coords;
                        }, "Coords"),
                new ModuleOption<>(PepsiMod.INSTANCE.hudSettings.netherCoords, "nether_coords", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.hudSettings.netherCoords = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.hudSettings.netherCoords;
                        }, "NetherCoords"),
                new ModuleOption<>(PepsiMod.INSTANCE.hudSettings.arrayListTop, "arraylist_top", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.hudSettings.arrayListTop = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.hudSettings.arrayListTop;
                        }, "ArrayListOnTop"),
                new ModuleOption<>(PepsiMod.INSTANCE.hudSettings.serverBrand, "server_brand", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.hudSettings.serverBrand = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.hudSettings.serverBrand;
                        }, "ServerBrand"),
                new ModuleOption<>(PepsiMod.INSTANCE.hudSettings.rainbow, "rainbow", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.hudSettings.rainbow = value;
                            for (Module module : ModuleManager.AVALIBLE_MODULES) {
                                module.updateName();
                            }
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.hudSettings.rainbow;
                        }, "Rainbow"),
                new ModuleOption<>(PepsiMod.INSTANCE.hudSettings.direction, "direction", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.hudSettings.direction = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.hudSettings.direction;
                        }, "Direction"),
                new ModuleOption<>(PepsiMod.INSTANCE.hudSettings.armor, "armor", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.hudSettings.armor = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.hudSettings.armor;
                        }, "Armor"),
                new ModuleOption<>(PepsiMod.INSTANCE.hudSettings.effects, "effects", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.hudSettings.effects = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.hudSettings.effects;
                        }, "Effects"),
                new ModuleOption<>(PepsiMod.INSTANCE.hudSettings.fps, "fps", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.hudSettings.fps = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.hudSettings.fps;
                        }, "FPS"),
                new ModuleOption<>(PepsiMod.INSTANCE.hudSettings.ping, "ping", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.hudSettings.ping = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.hudSettings.ping;
                        }, "Ping"),
                new ModuleOption<>(PepsiMod.INSTANCE.hudSettings.r, "r", new String[]{"0", "128", "255"},
                        (value) -> {
                            PepsiMod.INSTANCE.hudSettings.r = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.hudSettings.r;
                        }, "Red", new ExtensionSlider(ExtensionType.VALUE_INT, 0, 255, 1)),
                new ModuleOption<>(PepsiMod.INSTANCE.hudSettings.g, "g", new String[]{"0", "128", "255"},
                        (value) -> {
                            PepsiMod.INSTANCE.hudSettings.g = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.hudSettings.g;
                        }, "Green", new ExtensionSlider(ExtensionType.VALUE_INT, 0, 255, 1)),
                new ModuleOption<>(PepsiMod.INSTANCE.hudSettings.b, "b", new String[]{"0", "128", "255"},
                        (value) -> {
                            PepsiMod.INSTANCE.hudSettings.b = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.hudSettings.b;
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
