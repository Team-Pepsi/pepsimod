/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2018 DaPorkchop_
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

package net.daporkchop.pepsimod.command.impl;

import net.daporkchop.pepsimod.command.api.Command;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;

public class InvSeeCommand extends Command {
    public InvSeeCommand() {
        super("invsee");
    }

    @Override
    public void execute(String cmd, String[] args) {
        if (args.length < 2) {
            clientMessage("Usage: .invsee <player name>");
            return;
        }

        for (Entity entity : mc.world.getLoadedEntityList()) {
            if (entity instanceof EntityOtherPlayerMP) {
                EntityOtherPlayerMP player = (EntityOtherPlayerMP) entity;
                if (player.getName().equals(args[1])) {
                    clientMessage("Showing inventory of " + player.getName());
                    mc.displayGuiScreen(new GuiInventory(player));
                    return;
                }
            }
        }

        clientMessage("Such player in range!");
    }

    @Override
    public String getSuggestion(String cmd, String[] args) {
        switch (args.length) {
            case 1:
                String aName = this.getPlayerName("");
                if (aName == null) {
                    break;
                } else {
                    return ".invsee " + aName;
                }
            case 2:
                String bName = this.getPlayerName(args[1]);
                if (bName == null) {
                    break;
                } else {
                    return ".invsee " + bName;
                }
        }

        return ".invsee";
    }

    public String getPlayerName(String in) {
        for (Entity e : mc.world.loadedEntityList) {
            if (e instanceof EntityOtherPlayerMP) {
                EntityOtherPlayerMP player = (EntityOtherPlayerMP) e;
                if (player.getName().startsWith(in)) {
                    return player.getName();
                }
            }
        }

        return null;
    }
}
