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
        return TickRate.format.format(getMultiplier());
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
