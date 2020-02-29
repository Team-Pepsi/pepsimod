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