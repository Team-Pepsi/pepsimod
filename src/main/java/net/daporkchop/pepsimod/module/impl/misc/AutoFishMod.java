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

package net.daporkchop.pepsimod.module.impl.misc;

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.the.wurst.pkg.name.WPlayerController;
import net.daporkchop.pepsimod.util.ReflectionStuff;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketSoundEffect;

public class AutoFishMod extends Module {
    public static AutoFishMod INSTANCE;

    public static boolean isBobberSplash(SPacketSoundEffect soundEffect) {
        return SoundEvents.ENTITY_BOBBER_SPLASH.equals(soundEffect.getSound());
    }

    public int timer;

    {
        INSTANCE = this;
    }

    public AutoFishMod() {
        super("AutoFish");
    }

    @Override
    public void onEnable() {
        // reset timer
        this.timer = 0;
    }

    @Override
    public void onDisable() {
        // reset timer
        this.timer = 0;
    }

    @Override
    public void tick() {
        // search fishing rod in hotbar
        int rodInHotbar = -1;
        for (int i = 0; i < 9; i++) {
            // skip non-rod items
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.isEmpty() || !(stack.getItem() instanceof ItemFishingRod)) {
                continue;
            }

            rodInHotbar = i;
            break;
        }

        // check if any rod was found
        if (rodInHotbar != -1) {
            // select fishing rod
            if (mc.player.inventory.currentItem != rodInHotbar) {
                mc.player.inventory.currentItem = rodInHotbar;
                return;
            }

            // wait for timer
            if (this.timer > 0) {
                this.timer--;
                return;
            }

            // check bobber
            if (mc.player.fishEntity != null) {
                return;
            }

            // cast rod
            this.rightClick();
            return;
        }

        // search fishing rod in inventory
        int rodInInventory = -1;
        for (int i = 9; i < 36; i++) {
            // skip non-rod items
            ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.isEmpty() || !(stack.getItem() instanceof ItemFishingRod)) {
                continue;
            }

            rodInInventory = i;
            break;
        }

        // check if completely out of rods
        if (rodInInventory == -1) {
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
            hotbarSlot = mc.player.inventory.currentItem;
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
        this.timer = 15;
    }

    @Override
    public void postRecievePacket(Packet<?> packetIn) {
        if (packetIn instanceof SPacketSoundEffect && isBobberSplash((SPacketSoundEffect) packetIn) && mc.player.fishEntity != null) {
            this.rightClick();
        }
    }
}
