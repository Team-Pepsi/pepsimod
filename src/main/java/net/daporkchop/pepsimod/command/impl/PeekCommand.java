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

package net.daporkchop.pepsimod.command.impl;

import net.daporkchop.pepsimod.command.api.Command;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

public class PeekCommand extends Command {
    public static Block[] SHULKERS;

    public static boolean isShulkerBox(Block block) {
        for (Block b : SHULKERS) {
            if (b == block) {
                return true;
            }
        }

        return false;
    }

    public static InventoryBasic getFromItemNBT(NBTTagCompound tag) {
        NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
        String customName = "Shulker Box";

        if (tag.hasKey("Items", 9)) {
            ItemStackHelper.loadAllItems(tag, items);
        }

        if (tag.hasKey("CustomName", 8)) {
            customName = tag.getString("CustomName");
        }

        InventoryBasic inventoryBasic = new InventoryBasic(customName, true, items.size());
        for (int i = 0; i < items.size(); i++) {
            inventoryBasic.setInventorySlotContents(i, items.get(i));
        }
        return inventoryBasic;
    }

    {
        SHULKERS = new Block[]{
                Blocks.BLACK_SHULKER_BOX,
                Blocks.BLUE_SHULKER_BOX,
                Blocks.BROWN_SHULKER_BOX,
                Blocks.CYAN_SHULKER_BOX,
                Blocks.GRAY_SHULKER_BOX,
                Blocks.GREEN_SHULKER_BOX,
                Blocks.LIGHT_BLUE_SHULKER_BOX,
                Blocks.LIME_SHULKER_BOX,
                Blocks.MAGENTA_SHULKER_BOX,
                Blocks.ORANGE_SHULKER_BOX,
                Blocks.PINK_SHULKER_BOX,
                Blocks.PURPLE_SHULKER_BOX,
                Blocks.RED_SHULKER_BOX,
                Blocks.SILVER_SHULKER_BOX,
                Blocks.WHITE_SHULKER_BOX,
                Blocks.YELLOW_SHULKER_BOX
        };
    }

    public PeekCommand() {
        super("peek");
    }

    @Override
    public void execute(String cmd, String[] args) {
        ItemStack stack = null;
        if (!mc.player.getHeldItemOffhand().isEmpty()) {
            stack = mc.player.getHeldItemOffhand();
        }
        if (!mc.player.getHeldItemMainhand().isEmpty()) {
            stack = mc.player.getHeldItemMainhand();
        }
        if (stack != null && !stack.isEmpty()) {
            if (stack.getItem() instanceof ItemBlock) {
                Block block = ((ItemBlock) stack.getItem()).getBlock();
                if (isShulkerBox(block)) {
                    if (stack.hasTagCompound()) {
                        ItemStack wtf_java = stack;

                        mc.displayGuiScreen(new GuiChest(mc.player.inventory, getFromItemNBT(wtf_java.getTagCompound().getCompoundTag("BlockEntityTag"))));
                    } else {
                        mc.displayGuiScreen(new GuiChest(new InventoryBasic("Shulker Box", true, 27), mc.player.inventory));
                    }
                    return;
                }
            }
        }

        clientMessage("Not holding a shulker box!");
    }

    @Override
    public String getSuggestion(String cmd, String[] args) {
        return ".peek";
    }
}
