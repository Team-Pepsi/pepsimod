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

package net.daporkchop.pepsimod.module.impl.player;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.OptionCompletions;
import net.daporkchop.pepsimod.module.api.option.ExtensionSlider;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;
import net.daporkchop.pepsimod.util.ReflectionStuff;

public class SpeedmineMod extends Module {
    public static SpeedmineMod INSTANCE;

    public SpeedmineMod(boolean isEnabled, int key, boolean hide) {
        super(isEnabled, "Speedmine", key, hide);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void tick() {
        if (PepsiMod.INSTANCE.mc.world != null) {
            if (ReflectionStuff.getCurBlockDamageMP() < PepsiMod.INSTANCE.miscOptions.speedmine_speed) {
                ReflectionStuff.setCurBlockDamageMP(PepsiMod.INSTANCE.miscOptions.speedmine_speed);
            }
            if (ReflectionStuff.getBlockHitDelay() > 1) {
                ReflectionStuff.setBlockHitDelay(1);
            }
        }
    }

    @Override
    public void init() {
        INSTANCE = this;
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new ModuleOption<>(PepsiMod.INSTANCE.miscOptions.speedmine_speed, "speed", OptionCompletions.FLOAT,
                        (value) -> {
                            PepsiMod.INSTANCE.miscOptions.speedmine_speed = Math.max(0, value);
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.miscOptions.speedmine_speed;
                        }, "Speed", new ExtensionSlider(ExtensionType.VALUE_FLOAT, 0.1f, 1f, 0.1f))
        };
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.PLAYER;
    }
}
