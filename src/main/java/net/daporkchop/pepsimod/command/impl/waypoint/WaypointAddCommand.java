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

package net.daporkchop.pepsimod.command.impl.waypoint;

import net.daporkchop.pepsimod.command.api.Command;
import net.daporkchop.pepsimod.util.config.impl.WaypointsTranslator;
import net.daporkchop.pepsimod.util.misc.waypoints.Waypoint;

public class WaypointAddCommand extends Command {
    public WaypointAddCommand() {
        super("waypointadd");
    }

    @Override
    public void execute(String cmd, String[] args) {
        Waypoint waypoint = null;
        if (args.length >= 5) {
            if (WaypointsTranslator.INSTANCE.getWaypoint(args[1]) == null) {
                int x, y, z;
                try {
                    x = Integer.parseInt(args[2]);
                } catch (NumberFormatException e) {
                    clientMessage("Invalid integer: " + args[2]);
                    return;
                }
                try {
                    y = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    clientMessage("Invalid integer: " + args[3]);
                    return;
                }
                try {
                    z = Integer.parseInt(args[4]);
                } catch (NumberFormatException e) {
                    clientMessage("Invalid integer: " + args[4]);
                    return;
                }
                WaypointsTranslator.INSTANCE.addWaypoint(waypoint = new Waypoint(args[1], x, y, z, mc.player.dimension));
            } else {
                clientMessage("Waypoint \u00A7o" + args[1] + "\u00A7r already exists!");
                return;
            }
        } else if (args.length >= 2) {
            if (WaypointsTranslator.INSTANCE.getWaypoint(args[1]) == null) {
                waypoint = WaypointsTranslator.INSTANCE.addWaypoint(args[1]);
            } else {
                clientMessage("Waypoint \u00A7o" + args[1] + "\u00A7r already exists!");
                return;
            }
        } else {
            waypoint = WaypointsTranslator.INSTANCE.addWaypoint();
        }
        clientMessage("Added waypoint: \u00A7o" + waypoint.name + "\u00A7r in dimension " + waypoint.dim + " at XYZ " + waypoint.x + ", " + waypoint.y + ", " + waypoint.z);
    }

    @Override
    public String getSuggestion(String cmd, String[] args) {
        return cmd;
    }

    @Override
    public String[] aliases() {
        return new String[]{
                "waypointadd",
                "wadd"
        };
    }
}
