package net.daporkchop.pepsimod.module.impl;

import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.option.OptionTypeInteger;
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
        return new ModuleOption[]{new OptionTypeInteger(0, "mode")};
    }

    @Override
    public boolean preSendPacket(Packet<?> packetIn) {
        //System.out.println(packetIn.getClass().getCanonicalName());
        if (packetIn instanceof CPacketUseEntity) {
            System.out.println("CPacketUseEntity");
            if (((CPacketUseEntity) packetIn).getAction() == CPacketUseEntity.Action.ATTACK) {
                System.out.println("doing crit");
                doCrit();
            }
        }
        return false;
    }

    public void doCrit() {
        EntityPlayer player = Minecraft.getMinecraft().player;
        double x = player.posX;
        double y = player.posY;
        double z = player.posZ;
        NetworkManager manager = Minecraft.getMinecraft().getConnection().getNetworkManager();
        manager.sendPacket(new CPacketPlayer.Position(x, y + 0.0625D, z, true));
        manager.sendPacket(new CPacketPlayer.Position(x, y, z, false));
        manager.sendPacket(new CPacketPlayer.Position(x, y + 1.1E-5D, z, false));
        manager.sendPacket(new CPacketPlayer.Position(x, y, z, false));
    }

    @Override
    public boolean hasModeInName() {
        return true;
    }

    @Override
    public String getModeForName() {
        switch ((int) (Integer) getOptionByName("mode").getValue()) {
            case 0:
                return "Packet";
            case 1:
                return "Jump";
        }
        return "Unknown";
    }
}
