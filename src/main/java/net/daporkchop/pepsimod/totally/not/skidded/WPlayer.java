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

import net.daporkchop.pepsimod.util.misc.Default;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;

public final class WPlayer extends Default {
    public static void swingArmClient() {
        mc.player.swingArm(EnumHand.MAIN_HAND);
    }

    public static void swingArmPacket() {
        mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
    }

    public static void attackEntity(Entity entity) {
        Minecraft.getMinecraft().playerController.attackEntity(mc.player, entity);
        swingArmClient();
    }

    public static void sendAttackPacket(Entity entity) {
        mc.player.connection.sendPacket(new CPacketUseEntity(entity, EnumHand.MAIN_HAND));
    }

    public static float getCooldown() {
        return mc.player.getCooledAttackStrength(0);
    }

    public static void addPotionEffect(Potion potion) {
        mc.player
                .addPotionEffect(new PotionEffect(potion, 10801220));
    }

    public static void removePotionEffect(Potion potion) {
        mc.player.removePotionEffect(potion);
    }
}
