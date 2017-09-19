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
import net.daporkchop.pepsimod.totally.not.skidded.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class ScaffoldMod extends Module {
    public static ScaffoldMod INSTANCE;

    public ScaffoldMod(boolean isEnabled, int key, boolean hide) {
        super(isEnabled, "Scaffold", key, hide);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void tick() {
        BlockPos belowPlayer = new BlockPos(PepsiMod.INSTANCE.mc.player).down();

        // check if block is already placed
        IBlockState state = PepsiMod.INSTANCE.mc.world.getBlockState(belowPlayer);
        if (!state.getBlock().isReplaceable(PepsiMod.INSTANCE.mc.world, belowPlayer)) {
            return;
        }

        // search blocks in hotbar
        int newSlot = -1;
        for (int i = 0; i < 9; i++) {
            // filter out non-block items
            ItemStack stack = PepsiMod.INSTANCE.mc.player.inventory.getStackInSlot(i);
            if (stack == null || stack.isEmpty() || !(stack.getItem() instanceof ItemBlock)) {
                continue;
            }

            // filter out non-solid blocks
            if (!Block.getBlockFromItem(stack.getItem()).getDefaultState().isFullBlock()) {
                continue;
            }

            newSlot = i;
            break;
        }

        // check if any blocks were found
        if (newSlot == -1)
            return;

        // set slot
        int oldSlot = PepsiMod.INSTANCE.mc.player.inventory.currentItem;
        PepsiMod.INSTANCE.mc.player.inventory.currentItem = newSlot;

        // place block
        BlockUtils.placeBlockScaffold(belowPlayer);

        // reset slot
        PepsiMod.INSTANCE.mc.player.inventory.currentItem = oldSlot;
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
