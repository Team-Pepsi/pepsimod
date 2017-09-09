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

package net.daporkchop.pepsimod.module.impl.render;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.OptionCompletions;
import net.daporkchop.pepsimod.module.api.option.ExtensionSlider;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;

public class NoWeatherMod extends Module {
    public static NoWeatherMod INSTANCE;

    public NoWeatherMod(boolean isEnabled, int key, boolean hide) {
        super(isEnabled, "NoWeather", key, hide);
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
                new ModuleOption<>(false, "disableRain", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.noWeatherSettings.disableRain = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.noWeatherSettings.disableRain;
                        }, "Disable Rain"),
                new ModuleOption<>(false, "changeTime", OptionCompletions.BOOLEAN,
                        (value) -> {
                            PepsiMod.INSTANCE.noWeatherSettings.changeTime = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.noWeatherSettings.changeTime;
                        }, "Change Time"),
                new ModuleOption<>(PepsiMod.INSTANCE.noWeatherSettings.time, "time", new String[]{"0", "6000", "12000", "18000", "24000"},
                        (value) -> {
                            if (value < 0 || value > 24000) {
                                clientMessage("Time must be in range 0-24000!");
                                return false;
                            }
                            PepsiMod.INSTANCE.noWeatherSettings.time = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.noWeatherSettings.time;
                        }, "Time", new ExtensionSlider(ExtensionType.VALUE_INT, 0, 24000, 500))
        };
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.RENDER;
    }
}
