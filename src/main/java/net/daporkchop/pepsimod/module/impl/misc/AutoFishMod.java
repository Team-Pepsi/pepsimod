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

import net.daporkchop.pepsimod.command.api.Command;
import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.totally.not.skidded.WMinecraft;
import net.daporkchop.pepsimod.totally.not.skidded.WPlayerController;
import net.daporkchop.pepsimod.util.ReflectionStuff;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketSoundEffect;

public class AutoFishMod extends Module {
    public static AutoFishMod INSTANCE;

    {
        INSTANCE = this;
    }

    public int timer;

    public AutoFishMod() {
        super("AutoFish");
    }

    public static boolean isBobberSplash(SPacketSoundEffect soundEffect) {
        return SoundEvents.ENTITY_BOBBER_SPLASH.equals(soundEffect.getSound());
    }

    @Override
    public void onEnable() {
        // reset timer
        timer = 0;
    }

    @Override
    public void onDisable() {
        // reset timer
        timer = 0;
    }

    @Override
    public void tick() {
        // search fishing rod in hotbar
        int rodInHotbar = -1;
        for (int i = 0; i < 9; i++) {
            // skip non-rod items
            ItemStack stack = WMinecraft.getPlayer().inventory.getStackInSlot(i);
            if (stack.isEmpty() || !(stack.getItem() instanceof ItemFishingRod)) {
                continue;
            }

            rodInHotbar = i;
            break;
        }

        // check if any rod was found
        if (rodInHotbar != -1) {
            // select fishing rod
            if (WMinecraft.getPlayer().inventory.currentItem != rodInHotbar) {
                WMinecraft.getPlayer().inventory.currentItem = rodInHotbar;
                return;
            }

            // wait for timer
            if (timer > 0) {
                timer--;
                return;
            }

            // check bobber
            if (WMinecraft.getPlayer().fishEntity != null) {
                return;
            }

            // cast rod
            rightClick();
            return;
        }

        // search fishing rod in inventory
        int rodInInventory = -1;
        for (int i = 9; i < 36; i++) {
            // skip non-rod items
            ItemStack stack =
                    WMinecraft.getPlayer().inventory.getStackInSlot(i);
            if (stack.isEmpty() || !(stack.getItem() instanceof ItemFishingRod))
                continue;

            rodInInventory = i;
            break;
        }

        // check if completely out of rods
        if (rodInInventory == -1) {
            Command.clientMessage("Out of fishing rods.");
            ModuleManager.disableModule(this);
            return;
        }

        // find empty hotbar slot
        int hotbarSlot = -1;
        for (int i = 0; i < 9; i++) {
            // skip non-empty slots
            if (!mc.player.inventory.getStackInSlot(i).isEmpty()) {
                continue;
            }

            hotbarSlot = i;
            break;
        }

        // check if hotbar is full
        boolean swap = false;
        if (hotbarSlot == -1) {
            hotbarSlot = WMinecraft.getPlayer().inventory.currentItem;
            swap = true;
        }

        // place rod in hotbar slot
        WPlayerController.windowClick_PICKUP(rodInInventory);
        WPlayerController.windowClick_PICKUP(36 + hotbarSlot);

        // swap old hotbar item with rod
        if (swap) {
            WPlayerController.windowClick_PICKUP(rodInInventory);
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
        return ModuleCategory.MISC;
    }

    private void rightClick() {
        // check held item
        ItemStack stack = mc.player.inventory.getCurrentItem();
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemFishingRod)) {
            return;
        }

        // right click
        ReflectionStuff.rightClickMouse();

        // reset timer
        timer = 15;
    }

    @Override
    public void postRecievePacket(Packet<?> packetIn) {
        // check packet type
        if (!(packetIn instanceof SPacketSoundEffect)) {
            return;
        }

        // check sound type
        if (!isBobberSplash((SPacketSoundEffect) packetIn)) {
            return;
        }

        // catch fish
        rightClick();
    }
}
