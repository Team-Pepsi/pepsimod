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

package net.daporkchop.pepsimod.util.config.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.daporkchop.pepsimod.util.config.IConfigTranslator;
import net.daporkchop.pepsimod.util.misc.Default;
import net.daporkchop.pepsimod.util.misc.waypoints.DimensionWaypoints;
import net.daporkchop.pepsimod.util.misc.waypoints.ServerWaypoints;
import net.daporkchop.pepsimod.util.misc.waypoints.Waypoint;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;
import java.util.Optional;

public class WaypointsTranslator extends Default implements IConfigTranslator {
    public static final WaypointsTranslator INSTANCE = new WaypointsTranslator();
    public boolean tracers = false;
    public int r = 0;
    public int g = 0;
    public int b = 0;
    public boolean nametag = false;
    public boolean dist = true;
    public boolean coords = false;
    public Hashtable<String, ServerWaypoints> identifiersToServerWaypoints = new Hashtable<>();

    private WaypointsTranslator() {

    }

    public static String getCurrentServerIdentifier() {
        if (mc.isIntegratedServerRunning()) {
            return mc.getIntegratedServer().getFolderName();
        } else {
            return Optional.ofNullable(MC.getCurrentServerData())
                           .map(current -> current.serverIP)
                           .orElse("realms");
        }
    }

    public void encode(JsonObject json) {
        json.addProperty("tracers", tracers);
        json.addProperty("r", r);
        json.addProperty("g", g);
        json.addProperty("b", b);
        json.addProperty("nametag", nametag);
        json.addProperty("dist", dist);
        json.addProperty("coords", coords);

        for (Map.Entry<String, ServerWaypoints> server : identifiersToServerWaypoints.entrySet()) {
            for (Map.Entry<Integer, DimensionWaypoints> dimension : server.getValue().waypoints.entrySet()) {
                for (Map.Entry<String, Waypoint> waypoint : dimension.getValue().waypoints.entrySet()) {
                    json.addProperty(server.getKey() + "." + dimension.getKey() + "." + waypoint.getKey() + ".name", waypoint.getValue().name);
                    json.addProperty(server.getKey() + "." + dimension.getKey() + "." + waypoint.getKey() + ".x", waypoint.getValue().x);
                    json.addProperty(server.getKey() + "." + dimension.getKey() + "." + waypoint.getKey() + ".y", waypoint.getValue().y);
                    json.addProperty(server.getKey() + "." + dimension.getKey() + "." + waypoint.getKey() + ".z", waypoint.getValue().z);
                    json.addProperty(server.getKey() + "." + dimension.getKey() + "." + waypoint.getKey() + ".dim", waypoint.getValue().dim);
                    json.addProperty(server.getKey() + "." + dimension.getKey() + "." + waypoint.getKey() + ".shown", waypoint.getValue().shown);
                }
            }
        }
    }

    public void decode(String fieldName, JsonObject json) {
        tracers = getBoolean(json, "tracers", tracers);
        r = getInt(json, "r", r);
        g = getInt(json, "g", g);
        b = getInt(json, "b", b);
        nametag = getBoolean(json, "nametag", nametag);
        dist = getBoolean(json, "dist", dist);
        coords = getBoolean(json, "coords", coords);

        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            if (entry.getKey().endsWith(".name")) { //waypoint definition
                String baseKey = entry.getKey().substring(0, entry.getKey().length() - 5);
                String name = getString(json, baseKey + ".name", UUID.randomUUID().toString());
                int x = getInt(json, baseKey + ".x", 0);
                int y = getInt(json, baseKey + ".y", 0);
                int z = getInt(json, baseKey + ".z", 0);
                int dim = getInt(json, baseKey + ".dim", 0);
                boolean shown = getBoolean(json, baseKey + ".shown", true);
                String serverIdentifier = getString(json, baseKey + ".server", "localhost");
                ServerWaypoints serber = identifiersToServerWaypoints.get(serverIdentifier);
                if (serber == null) {
                    identifiersToServerWaypoints.put(serverIdentifier, serber = new ServerWaypoints());
                }
                DimensionWaypoints dimension = serber.waypoints.get(dim);
                if (dimension == null) {
                    serber.waypoints.put(dim, dimension = new DimensionWaypoints());
                }
                dimension.waypoints.put(name, new Waypoint(name, x, y, z, shown, dim));
            }
        }
    }

    public String name() {
        return "waypoints";
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
