/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2019 DaPorkchop_
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from DaPorkchop_.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original author of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
