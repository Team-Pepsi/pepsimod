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

package net.daporkchop.pepsimod.util.misc.waypoints;

import net.daporkchop.pepsimod.util.misc.Default;

import java.io.Serializable;
import java.util.Collection;
import java.util.Hashtable;
import java.util.UUID;

public class Waypoints extends Default implements Serializable {
    public Hashtable<String, ServerWaypoints> identifiersToServerWaypoints = new Hashtable<>();

    public static String getCurrentServerIdentifier() {
        if (mc.isIntegratedServerRunning()) {
            return mc.getIntegratedServer().getFolderName();
        } else {
            return mc.getCurrentServerData().serverIP;
        }
    }

    public Collection<Waypoint> getWaypoints() {
        ServerWaypoints serverWaypoints = identifiersToServerWaypoints.get(getCurrentServerIdentifier());
        if (serverWaypoints == null) {
            serverWaypoints = new ServerWaypoints();
            identifiersToServerWaypoints.put(getCurrentServerIdentifier(), serverWaypoints);
        }
        DimensionWaypoints dimensionWaypoints = serverWaypoints.waypoints.get(mc.player.dimension);
        if (dimensionWaypoints == null) {
            dimensionWaypoints = new DimensionWaypoints();
            serverWaypoints.waypoints.put(mc.player.dimension, dimensionWaypoints);
        }
        return dimensionWaypoints.waypoints.values();
    }

    public Waypoint getWaypoint(String name) {
        ServerWaypoints serverWaypoints = identifiersToServerWaypoints.get(getCurrentServerIdentifier());
        if (serverWaypoints == null) {
            serverWaypoints = new ServerWaypoints();
            identifiersToServerWaypoints.put(getCurrentServerIdentifier(), serverWaypoints);
        }
        DimensionWaypoints dimensionWaypoints = serverWaypoints.waypoints.get(mc.player.dimension);
        if (dimensionWaypoints == null) {
            dimensionWaypoints = new DimensionWaypoints();
            serverWaypoints.waypoints.put(mc.player.dimension, dimensionWaypoints);
        }
        return dimensionWaypoints.waypoints.get(name);
    }

    public Waypoint addWaypoint(Waypoint waypoint) {
        ServerWaypoints serverWaypoints = identifiersToServerWaypoints.get(getCurrentServerIdentifier());
        if (serverWaypoints == null) {
            serverWaypoints = new ServerWaypoints();
            identifiersToServerWaypoints.put(getCurrentServerIdentifier(), serverWaypoints);
        }
        DimensionWaypoints dimensionWaypoints = serverWaypoints.waypoints.get(mc.player.dimension);
        if (dimensionWaypoints == null) {
            dimensionWaypoints = new DimensionWaypoints();
            serverWaypoints.waypoints.put(mc.player.dimension, dimensionWaypoints);
        }
        dimensionWaypoints.waypoints.put(waypoint.name, waypoint);
        return waypoint;
    }

    public Waypoint addWaypoint(String name) {
        return addWaypoint(new Waypoint(name, mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.dimension));
    }

    public Waypoint addWaypoint() {
        return addWaypoint(UUID.randomUUID().toString());
    }

    public Waypoint removeWaypoint(String name) {
        ServerWaypoints serverWaypoints = identifiersToServerWaypoints.get(getCurrentServerIdentifier());
        if (serverWaypoints == null) {
            serverWaypoints = new ServerWaypoints();
            identifiersToServerWaypoints.put(getCurrentServerIdentifier(), serverWaypoints);
        }
        DimensionWaypoints dimensionWaypoints = serverWaypoints.waypoints.get(mc.player.dimension);
        if (dimensionWaypoints == null) {
            dimensionWaypoints = new DimensionWaypoints();
            serverWaypoints.waypoints.put(mc.player.dimension, dimensionWaypoints);
        }
        return dimensionWaypoints.waypoints.remove(name);
    }

    public void clearWaypoints() {
        identifiersToServerWaypoints.remove(getCurrentServerIdentifier());
    }

    public void hardClearWaypoints() {
        identifiersToServerWaypoints = new Hashtable<>();
    }
}
