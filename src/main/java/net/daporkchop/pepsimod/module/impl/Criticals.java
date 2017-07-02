package net.daporkchop.pepsimod.module.impl;

import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.option.OptionTypeInteger;
import net.minecraft.network.Packet;
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
        if (packetIn instanceof CPacketUseEntity) {
            if (((CPacketUseEntity) packetIn).getAction().ordinal() == CPacketUseEntity.Action.ATTACK.ordinal()) {
                doCrit();
            }
        }
        return false;
    }

    public void doCrit() {

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
