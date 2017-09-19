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

package net.daporkchop.pepsimod.totally.not.skidded;

import net.daporkchop.pepsimod.PepsiMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public final class WPlayerController {
    private static PlayerControllerMP getPlayerController() {
        return Minecraft.getMinecraft().playerController;
    }

    public static ItemStack windowClick_PICKUP(int slot) {
        return getPlayerController().windowClick(0, slot, 0, ClickType.PICKUP, PepsiMod.INSTANCE.mc.player);
    }

    public static ItemStack windowClick_QUICK_MOVE(int slot) {
        return getPlayerController().windowClick(0, slot, 0, ClickType.QUICK_MOVE, PepsiMod.INSTANCE.mc.player);
    }

    public static ItemStack windowClick_THROW(int slot) {
        return getPlayerController().windowClick(0, slot, 1, ClickType.THROW,
                PepsiMod.INSTANCE.mc.player);
    }

    public static void processRightClick() {
        getPlayerController().processRightClick(PepsiMod.INSTANCE.mc.player,
                PepsiMod.INSTANCE.mc.world, EnumHand.MAIN_HAND);
    }

    public static void processRightClickBlock(BlockPos pos, EnumFacing side, Vec3d hitVec) {
        getPlayerController().processRightClickBlock(PepsiMod.INSTANCE.mc.player,
                PepsiMod.INSTANCE.mc.world, pos, side, hitVec, EnumHand.MAIN_HAND);
    }
}

