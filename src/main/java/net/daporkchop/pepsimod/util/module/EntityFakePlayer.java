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

package net.daporkchop.pepsimod.util.module;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;

public class EntityFakePlayer extends EntityOtherPlayerMP {
    public EntityFakePlayer() {
        super(PepsiMod.INSTANCE.mc.world, PepsiMod.INSTANCE.mc.player.getGameProfile());
        copyLocationAndAnglesFrom(PepsiMod.INSTANCE.mc.player);

        // fix inventory
        inventory.copyInventory(PepsiMod.INSTANCE.mc.player.inventory);
        PepsiUtils.copyPlayerModel(PepsiMod.INSTANCE.mc.player, this);

        // fix rotation
        rotationYawHead = PepsiMod.INSTANCE.mc.player.rotationYawHead;
        renderYawOffset = PepsiMod.INSTANCE.mc.player.renderYawOffset;

        // fix cape movement
        chasingPosX = posX;
        chasingPosY = posY;
        chasingPosZ = posZ;

        // spawn
        PepsiMod.INSTANCE.mc.world.addEntityToWorld(getEntityId(), this);
    }

    public void resetPlayerPosition() {
        PepsiMod.INSTANCE.mc.player.setPositionAndRotation(posX, posY, posZ, rotationYaw, rotationPitch);
    }

    public void despawn() {
        PepsiMod.INSTANCE.mc.world.removeEntityFromWorld(getEntityId());
    }
}