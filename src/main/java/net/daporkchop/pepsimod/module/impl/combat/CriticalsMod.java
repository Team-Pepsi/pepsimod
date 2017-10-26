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

    public ModuleCategory getCategory() {
        return ModuleCategory.COMBAT;
    }
}
