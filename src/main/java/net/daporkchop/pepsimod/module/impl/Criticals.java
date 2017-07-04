package net.daporkchop.pepsimod.module.impl;

import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.option.OptionTypeBoolean;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;

public class Criticals extends Module {

    public Criticals(boolean isEnabled, int key, boolean hide) {
        super(isEnabled, "Criticals", key, hide);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void tick() {
    }

    @Override
    public void init() {
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{new OptionTypeBoolean(true, "packet")};
    }

    @Override
    public boolean preSendPacket(Packet<?> packetIn) {
        if (packetIn instanceof CPacketUseEntity) {
            if (((CPacketUseEntity) packetIn).getAction() == CPacketUseEntity.Action.ATTACK) {
                doCrit();
            }
        }
        return false;
    }

    public void doCrit() {
        EntityPlayer player = Minecraft.getMinecraft().player;

        if (!player.onGround) {
            return;
        }

        if (player.isInWater() || player.isInLava()) {
            return;
        }

        if ((boolean) getOptionByName("packet").getValue()) {
            double x = player.posX;
            double y = player.posY;
            double z = player.posZ;
            NetworkManager manager = Minecraft.getMinecraft().getConnection().getNetworkManager();
            manager.sendPacket(new CPacketPlayer.Position(x, y + 0.0625D, z, true));
            manager.sendPacket(new CPacketPlayer.Position(x, y, z, false));
            manager.sendPacket(new CPacketPlayer.Position(x, y + 1.1E-5D, z, false));
            manager.sendPacket(new CPacketPlayer.Position(x, y, z, false));
        } else {
            player.motionY = 0.1F;
            player.fallDistance = 0.1F;
            player.onGround = false;
        }
    }

    @Override
    public boolean hasModeInName() {
        return true;
    }

    @Override
    public String getModeForName() {
        if ((boolean) getOptionByName("packet").getValue()) {
            return "Packet";
        } else {
            return "Jump";
        }
    }
}
