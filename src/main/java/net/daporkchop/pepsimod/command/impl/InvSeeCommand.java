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

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.command.api.Command;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.Entity;

import java.util.TimerTask;

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

        for (Entity entity : PepsiMod.INSTANCE.mc.world.getLoadedEntityList()) {
            if (entity instanceof EntityOtherPlayerMP) {
                EntityOtherPlayerMP player = (EntityOtherPlayerMP) entity;
                if (player.getName().equals(args[1])) {
                    clientMessage("Showing inventory of " + player.getName());
                    PepsiUtils.timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            PepsiMod.INSTANCE.mc.displayGuiScreen(new GuiInventory(player));
                        }
                    }, 500);
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
                String aName = getPlayerName("");
                if (aName == null) {
                    break;
                } else {
                    return ".invsee " + aName;
                }
            case 2:
                String bName = getPlayerName(args[1]);
                if (bName == null) {
                    break;
                } else {
                    return ".invsee " + bName;
                }
        }

        return ".invsee";
    }

    public String getPlayerName(String in) {
        for (Entity e : PepsiMod.INSTANCE.mc.world.loadedEntityList) {
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
