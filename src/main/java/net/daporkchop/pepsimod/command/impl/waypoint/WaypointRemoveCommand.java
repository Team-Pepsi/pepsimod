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

package net.daporkchop.pepsimod.command.impl.waypoint;

import net.daporkchop.pepsimod.command.api.Command;
import net.daporkchop.pepsimod.util.config.impl.WaypointsTranslator;
import net.daporkchop.pepsimod.util.misc.waypoints.Waypoint;

import java.util.Collection;

public class WaypointRemoveCommand extends Command {
    public WaypointRemoveCommand() {
        super("waypointremove");
    }

    @Override
    public void execute(String cmd, String[] args) {
        Waypoint waypoint = null;
        if (args.length >= 2) {
            if ((waypoint = WaypointsTranslator.INSTANCE.removeWaypoint(args[1])) == null) {
                clientMessage("No waypoint with name:\u00A7o" + args[1] + "\u00A7r!");
                return;
            }
        } else {
            clientMessage("\u00A7cNo waypoint given!");
            return;
        }
        clientMessage("Removed waypoint: \u00A7o" + waypoint.name + "\u00A7r!");
    }

    @Override
    public String getSuggestion(String cmd, String[] args) {
        if (args.length == 1) {
            return cmd + " " + WaypointsTranslator.INSTANCE.getWaypoints().iterator().next().name;
        } else if (args.length == 2) {
            Collection<Waypoint> waypoints = WaypointsTranslator.INSTANCE.getWaypoints();
            for (Waypoint waypoint : waypoints) {
                if (waypoint.name.startsWith(args[1])) {
                    return args[0] + " " + waypoint.name;
                }
            }
        }

        return cmd;
    }

    @Override
    public String[] aliases() {
        return new String[]{
                "waypointremove",
                "wremove",
                "waypointdelete",
                "wdelete"
        };
    }
}
