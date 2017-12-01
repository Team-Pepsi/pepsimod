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
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleLaunchState;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.option.ExtensionSlider;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;
import net.daporkchop.pepsimod.util.EntityFakePlayer;
import net.daporkchop.pepsimod.util.config.impl.FreecamTranslator;

public class FreecamMod extends Module {
    public static FreecamMod INSTANCE;

    {
        INSTANCE = this;
    }

    public EntityFakePlayer fakePlayer;

    {
        INSTANCE = this;
    }

    public FreecamMod() {
        super("Freecam");
    }

    @Override
    public void onEnable() {
        INSTANCE = this;//adding this a bunch because it always seems to be null idk y
        if (pepsiMod.hasInitializedModules) {
            fakePlayer = new EntityFakePlayer();
        }
    }

    @Override
    public void onDisable() {
        INSTANCE = this;//adding this a bunch because it always seems to be null idk y
        if (pepsiMod.hasInitializedModules) {
            fakePlayer.resetPlayerPosition();
            fakePlayer.despawn();

            //PepsiMod.pepsimodInstance.mc.renderGlobal.loadRenderers();
        }
    }

    @Override
    public void tick() {
        mc.player.motionX = 0;
        mc.player.motionZ = 0;

        mc.player.jumpMovementFactor = FreecamTranslator.INSTANCE.speed / 10;
    }

    @Override
    public void init() {
        INSTANCE = this; //adding this a bunch because it always seems to be null idk y
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new ModuleOption<>(1.0f, "speed", new String[]{"1.0", "0.0"},
                        (value) -> {
                            if (value <= 0.0f) {
                                clientMessage("Speed cannot be negative or 0!");
                                return false;
                            }
                            FreecamTranslator.INSTANCE.speed = value;
                            return true;
                        },
                        () -> {
                            return FreecamTranslator.INSTANCE.speed;
                        }, "Speed", new ExtensionSlider(ExtensionType.VALUE_FLOAT, 0.0f, 1.0f, 0.1f))
        };
    }

    @Override
    public ModuleLaunchState getLaunchState() {
        return ModuleLaunchState.DISABLED;
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.MISC;
    }
}
