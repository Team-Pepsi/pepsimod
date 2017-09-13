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

package net.daporkchop.pepsimod.command.impl;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.command.api.Command;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;

import java.util.TimerTask;

public class PeekCommand extends Command {
    public static Block[] SHULKERS;

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

    @Override
    public void execute(String cmd, String[] args) {
        ItemStack stack = null;
        if (!PepsiMod.INSTANCE.mc.player.getHeldItemOffhand().isEmpty()) {
            stack = PepsiMod.INSTANCE.mc.player.getHeldItemOffhand();
        }
        if (!PepsiMod.INSTANCE.mc.player.getHeldItemMainhand().isEmpty()) {
            stack = PepsiMod.INSTANCE.mc.player.getHeldItemMainhand();
        }
        if (stack != null && !stack.isEmpty()) {
            if (stack.getItem() instanceof ItemBlock) {
                Block block = ((ItemBlock) stack.getItem()).getBlock();
                if (isShulkerBox(block)) {
                    if (stack.hasTagCompound()) {
                        ItemStack wtf_java = stack;
                        PepsiUtils.timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                PepsiMod.INSTANCE.mc.displayGuiScreen(new GuiChest(PepsiMod.INSTANCE.mc.player.inventory, getFromItemNBT(wtf_java.getTagCompound().getCompoundTag("BlockEntityTag"))));
                            }
                        }, 500);
                    } else {
                        PepsiUtils.timer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                PepsiMod.INSTANCE.mc.displayGuiScreen(new GuiChest(new InventoryBasic("Shulker Box", true, 27), PepsiMod.INSTANCE.mc.player.inventory));
                            }
                        }, 500);
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
