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
