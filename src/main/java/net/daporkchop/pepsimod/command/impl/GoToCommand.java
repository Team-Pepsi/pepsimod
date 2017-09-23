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
import net.daporkchop.pepsimod.util.misc.waypoints.Waypoint;
import net.daporkchop.pepsimod.util.misc.waypoints.pathfind.PathFindTickListener;
import net.daporkchop.pepsimod.util.misc.waypoints.pathfind.PathFinder;
import net.daporkchop.pepsimod.util.misc.waypoints.pathfind.PathProcessor;
import net.minecraft.util.math.BlockPos;

public class GoToCommand extends Command {
    public static GoToCommand INSTANCE;
    public PathFinder pathFinder;
    public PathProcessor processor;
    public boolean enabled;
    public BlockPos endGoal, currentGoal;
    public int maxTravelDist = 50; //TODO: use loaded chunk radius

    public GoToCommand() {
        super("goto");
        INSTANCE = this;
    }

    @Override
    public void execute(String cmd, String[] args) {
        if (PathFindTickListener.INSTANCE == null) {
            new PathFindTickListener();
        }
        if (enabled) {
            PathFindTickListener.INSTANCE.disable();
            clientMessage("Disabled pathfinder.");
            return;
        }
        if (args.length == 2) {
            Waypoint waypoint = PepsiMod.INSTANCE.waypoints.getWaypoint(args[1]);
            if (waypoint == null) {
                clientMessage("No such waypoint: " + args[1]);
                return;
            } else {
                setGoal(new BlockPos(waypoint.x, waypoint.y, waypoint.z));
            }
        } else if (args.length == 4) {
            int x, y, z;
            try {
                x = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                clientMessage("Invalid integer: " + args[1]);
                return;
            }
            try {
                y = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                clientMessage("Invalid integer: " + args[2]);
                return;
            }
            try {
                z = Integer.parseInt(args[3]);
            } catch (NumberFormatException e) {
                clientMessage("Invalid integer: " + args[3]);
                return;
            }
            setGoal(new BlockPos(x, y, z));
        } else {
            clientMessage("Usage: \u00A7o.goto <waypoint>\u00A7r or \u00A7o.goto <x> <y> <z>");
            return;
        }
        enabled = true;
        PathFindTickListener.INSTANCE.preCalculate();
        PepsiUtils.tickListeners.add(PathFindTickListener.INSTANCE);
        PepsiUtils.wurstRenderListeners.add(PathFindTickListener.INSTANCE);
    }

    public void setGoal(BlockPos goal) {
        if (endGoal == null) {
            endGoal = goal;
            pathFinder = new PathFinder(goal);
        } else {
            throw new IllegalStateException("Attempted to start pathfinder while endGoal was set");
        }
    }

    public boolean hasReachedFinalGoal() {
        if (endGoal == null) {
            return false;
        }
        return Math.abs(Math.abs(PepsiMod.INSTANCE.mc.player.posX) - Math.abs(endGoal.getX())) <= 1
                && Math.abs(Math.abs(PepsiMod.INSTANCE.mc.player.posY) - Math.abs(endGoal.getY())) <= 1
                && Math.abs(Math.abs(PepsiMod.INSTANCE.mc.player.posZ) - Math.abs(endGoal.getZ())) <= 1;
    }

    @Override
    public String getSuggestion(String cmd, String[] args) {
        return ".goto";
    }
}
