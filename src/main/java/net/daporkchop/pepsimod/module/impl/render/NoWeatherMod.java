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
import net.daporkchop.pepsimod.module.api.CustomOption;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.option.OptionTypeBoolean;
import net.daporkchop.pepsimod.module.api.option.OptionTypeInteger;

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
                new CustomOption<>(false, "disableRain", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.noWeatherSettings.disableRain = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.noWeatherSettings.disableRain;
                        }),
                new CustomOption<>(false, "changeTime", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.noWeatherSettings.changeTime = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.noWeatherSettings.changeTime;
                        }),
                new CustomOption<>(PepsiMod.INSTANCE.noWeatherSettings.time, "time", new String[]{"0", "6000", "12000", "18000", "24000"},
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
                        }),
                new CustomOption<>(false, "changeMoon", OptionTypeBoolean.DEFAULT_COMPLETIONS,
                        (value) -> {
                            PepsiMod.INSTANCE.noWeatherSettings.changeMoon = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.noWeatherSettings.changeMoon;
                        }),
                new CustomOption<>(PepsiMod.INSTANCE.noWeatherSettings.moonPhase, "moonPhase", OptionTypeInteger.DEFAULT_COMPLETIONS,
                        (value) -> {
                            if (value < 0 || value > 7) {
                                clientMessage("Moon phase must be in range 0-7!");
                                return false;
                            }
                            PepsiMod.INSTANCE.noWeatherSettings.moonPhase = value;
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.noWeatherSettings.moonPhase;
                        })
        };
    }
}
