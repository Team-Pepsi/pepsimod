package net.daporkchop.pepsimod.util;

import net.daporkchop.pepsimod.PepsiMod;
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