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

package net.daporkchop.pepsimod.module.impl.combat;

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.OptionCompletions;
import net.daporkchop.pepsimod.util.config.impl.CriticalsTranslator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;

public class CriticalsMod extends Module {
    public static CriticalsMod INSTANCE;

    {
        INSTANCE = this;
    }

    public CriticalsMod() {
        super("Criticals");
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
        return new ModuleOption[]{new ModuleOption<>(true, "packet", OptionCompletions.BOOLEAN,
                (value) -> {
                    CriticalsTranslator.INSTANCE.packet = value;
                    return true;
                },
                () -> {
                    return CriticalsTranslator.INSTANCE.packet;
                }, "Packet")};
    }

    @Override
    public boolean preSendPacket(Packet<?> packetIn) {
        if (packetIn instanceof CPacketUseEntity) {
            if (((CPacketUseEntity) packetIn).getAction() == CPacketUseEntity.Action.ATTACK) {
                this.doCrit();
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

        if ((boolean) this.getOptionByName("packet").getValue()) {
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
        if ((boolean) this.getOptionByName("packet").getValue()) {
            return "Packet";
        } else {
            return "Jump";
        }
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.COMBAT;
    }
}
