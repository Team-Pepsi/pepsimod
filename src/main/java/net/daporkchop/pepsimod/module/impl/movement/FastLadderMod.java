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

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.event.MoveEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockVine;
import net.minecraft.util.math.BlockPos;

public class FastLadderMod extends Module {
    public static FastLadderMod INSTANCE;

    public FastLadderMod() {
        super("FastLadder");
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
    public void onPlayerMove(MoveEvent e) {
        if (PepsiUtils.isControlsPressed()) {
            if (!mc.player.isOnLadder() || !mc.player.collidedHorizontally || !mc.player.onGround) {
                return;
            }
            int ladders = getAboveLadders();
            if (ladders == 0) {
                return;
            }
            mc.player.setPosition(mc.player.posX, ladders < 10 ? ladders + 0.99D : 9.99D + mc.player.posY, mc.player.posZ);
            mc.player.motionY = -0.1D;
            mc.player.motionX = mc.player.motionZ = 0.0D;
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
        return ModuleCategory.MOVEMENT;
    }

    public int getAboveLadders() {
        int ladders = 0;
        for (int dist = 1; dist < 256; dist++) {
            BlockPos pos = new BlockPos(mc.player.posX, mc.player.posY + dist, mc.player.posZ);
            Block block = mc.world.getBlockState(pos).getBlock();
            if ((!(block instanceof BlockLadder)) && (!(block instanceof BlockVine))) {
                break;
            }
            ladders++;
        }
        return ladders;
    }
}
