/*
 * Adapted from The MIT License (MIT)
 *
 * Copyright (c) 2016-2020 DaPorkchop_
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

package net.daporkchop.pepsimod.module.impl.render;

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.OptionCompletions;
import net.daporkchop.pepsimod.module.api.option.ExtensionSlider;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;
import net.daporkchop.pepsimod.util.config.impl.NoWeatherTranslator;

public class NoWeatherMod extends Module {
    public static NoWeatherMod INSTANCE;

    {
        INSTANCE = this;
    }

    public NoWeatherMod() {
        super("NoWeather");
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
                            NoWeatherTranslator.INSTANCE.disableRain = value;
                            return true;
                        },
                        () -> {
                            return NoWeatherTranslator.INSTANCE.disableRain;
                        }, "Disable Rain"),
                new ModuleOption<>(false, "changeTime", OptionCompletions.BOOLEAN,
                        (value) -> {
                            NoWeatherTranslator.INSTANCE.changeTime = value;
                            return true;
                        },
                        () -> {
                            return NoWeatherTranslator.INSTANCE.changeTime;
                        }, "Change Time"),
                new ModuleOption<>(NoWeatherTranslator.INSTANCE.time, "time", new String[]{"0", "6000", "12000", "18000", "24000"},
                        (value) -> {
                            if (value < 0 || value > 24000) {
                                clientMessage("Time must be in range 0-24000!");
                                return false;
                            }
                            NoWeatherTranslator.INSTANCE.time = value;
                            return true;
                        },
                        () -> {
                            return NoWeatherTranslator.INSTANCE.time;
                        }, "Time", new ExtensionSlider(ExtensionType.VALUE_INT, 0, 24000, 500))
        };
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.RENDER;
    }
}
