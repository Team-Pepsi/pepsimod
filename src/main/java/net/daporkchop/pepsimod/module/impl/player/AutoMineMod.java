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

package net.daporkchop.pepsimod.module.impl.player;

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.optimization.OverrideCounter;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.RayTraceResult;

import java.util.concurrent.atomic.AtomicBoolean;

public class AutoMineMod extends Module {
    public static AutoMineMod INSTANCE;
    protected boolean started = false;
    protected final AtomicBoolean incremented = new AtomicBoolean(false);

    {
        INSTANCE = this;
    }

    public AutoMineMod() {
        super("AutoMine");
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
        if (this.incremented.getAndSet(false)) {
            ((OverrideCounter) mc.gameSettings.keyBindAttack).decrementOverride();
        }
    }

    @Override
    public void tick() {
        if (this.started && mc.gameSettings.keyBindUseItem.isKeyDown()) {
            this.started = false;
        }
        if (this.incremented.getAndSet(false)) {
            ((OverrideCounter) mc.gameSettings.keyBindAttack).decrementOverride();
        }
        if (mc.objectMouseOver == null || mc.objectMouseOver.typeOfHit != RayTraceResult.Type.BLOCK) {
            return;
        }

        if (this.started) {
            IBlockState state = mc.world.getBlockState(mc.objectMouseOver.getBlockPos());
            boolean flag = state.getBlock().getMaterial(state) != Material.AIR;
            if (flag && !this.incremented.getAndSet(true)) {
                ((OverrideCounter) mc.gameSettings.keyBindAttack).incrementOverride();
            }
        } else {
            if (mc.gameSettings.keyBindAttack.isKeyDown()) {
                this.started = true;
            }
        }
    }

    @Override
    public void init() {
        INSTANCE = this;
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[0];
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.PLAYER;
    }
}
