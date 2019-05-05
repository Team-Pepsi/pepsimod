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

package net.daporkchop.pepsimod.util;

import net.minecraft.client.entity.EntityOtherPlayerMP;

import static net.daporkchop.pepsimod.util.PepsiConstants.mc;

public class EntityFakePlayer extends EntityOtherPlayerMP {
    public EntityFakePlayer() {
        super(mc.world, mc.player.getGameProfile());
        this.copyLocationAndAnglesFrom(mc.player);

        // fix inventory
        this.inventory.copyInventory(mc.player.inventory);
        PepsiUtils.copyPlayerModel(mc.player, this);

        // fix rotation
        this.rotationYawHead = mc.player.rotationYawHead;
        this.renderYawOffset = mc.player.renderYawOffset;

        // fix cape movement
        this.chasingPosX = this.posX;
        this.chasingPosY = this.posY;
        this.chasingPosZ = this.posZ;

        // spawn
        mc.world.addEntityToWorld(this.getEntityId(), this);
    }

    public void resetPlayerPosition() {
        mc.player.setPositionAndRotation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
    }

    public void despawn() {
        mc.world.removeEntityFromWorld(this.getEntityId());
    }
}