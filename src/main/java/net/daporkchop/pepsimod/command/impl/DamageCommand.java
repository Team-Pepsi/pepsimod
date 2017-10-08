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

package net.daporkchop.pepsimod.command.impl;

import net.daporkchop.pepsimod.command.api.Command;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketPlayer;

public class DamageCommand extends Command {
    public DamageCommand() {
        super("damage");
    }

    public static void damage(int damage) {
        boolean bypass = true;

        EntityPlayerSP player = mc.player;
        if (player == null) {
            return;
        }
        double x = player.posX;
        double y = player.posY;
        double z = player.posZ;
        if (bypass) {
            for (double dist = 0.0D; dist < damage + 2; dist += 1.25D) {
                mc.getConnection().sendPacket(new CPacketPlayer.Position(x, y + 1.25D, z, false));
                mc.getConnection().sendPacket(new CPacketPlayer.Position(x, y, z, false));
            }
        } else {
            mc.getConnection().sendPacket(new CPacketPlayer.Position(x, y + 0.1D, z, false));
            mc.getConnection().sendPacket(new CPacketPlayer.Position(x, y - damage - 3.0D, z, false));
        }
    }

    @Override
    public void execute(String cmd, String[] args) {
        if (args.length == 1) {
            damage(1);
        } else if (args.length == 2) {
            try {
                damage(Integer.parseInt(args[1]));
            } catch (NumberFormatException e) {
                clientMessage("Not a number: " + args[1]);
            }
        }
    }

    @Override
    public String getSuggestion(String cmd, String[] args) {
        return ".damage";
    }
}
