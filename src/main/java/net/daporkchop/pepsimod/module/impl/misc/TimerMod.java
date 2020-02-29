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

package net.daporkchop.pepsimod.module.impl.misc;

import net.daporkchop.pepsimod.misc.TickRate;
import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.OptionCompletions;
import net.daporkchop.pepsimod.module.api.option.ExtensionSlider;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;
import net.daporkchop.pepsimod.util.config.impl.TimerTranslator;

public class TimerMod extends Module {
    public static TimerMod INSTANCE;

    {
        INSTANCE = this;
    }

    public TimerMod() {
        super("Timer");
    }

    @Override
    public void onEnable() {
        INSTANCE = this;//adding this a bunch because it always seems to be null idk y
    }

    @Override
    public void onDisable() {
        INSTANCE = this;//adding this a bunch because it always seems to be null idk y
    }

    @Override
    public void tick() {

    }

    @Override
    public void init() {
        INSTANCE = this; //adding this a bunch because it always seems to be null idk y
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new ModuleOption<>(1.0f, "multiplier", new String[]{"1.0", "0.0"},
                        (value) -> {
                            if (value <= 0.0f) {
                                clientMessage("Multiplier cannot be negative or 0!");
                                return false;
                            }
                            TimerTranslator.INSTANCE.multiplier = value;
                            return true;
                        },
                        () -> {
                            return TimerTranslator.INSTANCE.multiplier;
                        }, "Multiplier", new ExtensionSlider(ExtensionType.VALUE_FLOAT, 0.0f, 1.0f, 0.01f)),
                new ModuleOption<>(false, "tps_sync", OptionCompletions.BOOLEAN,
                        (value) -> {
                            TimerTranslator.INSTANCE.tpsSync = value;
                            return true;
                        },
                        () -> {
                            return TimerTranslator.INSTANCE.tpsSync;
                        }, "TpsSync")
        };
    }

    @Override
    public boolean hasModeInName() {
        return true;
    }

    @Override
    public String getModeForName() {
        return TickRate.format.format(this.getMultiplier());
    }

    public float getMultiplier() {
        if (this.state.enabled) {
            if (TimerTranslator.INSTANCE.tpsSync) {
                return TickRate.TPS / 20 * TimerTranslator.INSTANCE.multiplier;
            } else {
                return TimerTranslator.INSTANCE.multiplier;
            }
        } else {
            return 1.0f;
        }
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.MISC;
    }
}
