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

public class WaypointHardClearCommand extends Command {
    public WaypointHardClearCommand() {
        super("waypointhardclear");
    }

    @Override
    public void execute(String cmd, String[] args) {
        WaypointsTranslator.INSTANCE.hardClearWaypoints();
        clientMessage("Cleared all waypoints on all servers!");
    }

    @Override
    public String getSuggestion(String cmd, String[] args) {
        return cmd;
    }

    @Override
    public String[] aliases() {
        return new String[]{
                "waypointhardclear",
                "whardclear"
        };
    }
}
