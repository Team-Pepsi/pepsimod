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

package net.daporkchop.pepsimod.module.impl.movement;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.OptionCompletions;
import net.daporkchop.pepsimod.module.api.option.ExtensionSlider;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;

public class EntityStepMod extends Module {
    public static EntityStepMod INSTANCE;

    public EntityStepMod(boolean isEnabled, int key, boolean hide) {
        super(isEnabled, "EntityStep", key, hide);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        if (PepsiMod.INSTANCE.hasInitializedModules && mc.player.getRidingEntity() != null) {
            mc.player.getRidingEntity().stepHeight = 1f;
        }
    }

    @Override
    public void tick() {
        if (mc.player.getRidingEntity() != null) {
            mc.player.getRidingEntity().stepHeight = PepsiMod.INSTANCE.miscOptions.entityStep_step;
        }
    }

    @Override
    public void init() {
        INSTANCE = this;
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new ModuleOption<>(PepsiMod.INSTANCE.miscOptions.entityStep_step, "step", OptionCompletions.FLOAT,
                        (value) -> {
                            PepsiMod.INSTANCE.miscOptions.entityStep_step = Math.max(0, value);
                            return true;
                        },
                        () -> {
                            return PepsiMod.INSTANCE.miscOptions.entityStep_step;
                        }, "Step", new ExtensionSlider(ExtensionType.VALUE_FLOAT, 1f, 50f, 0.5f))
        };
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.MOVEMENT;
    }
}
