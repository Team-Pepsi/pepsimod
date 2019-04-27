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
        return mc.isIntegratedServerRunning() ? mc.getIntegratedServer().getFolderName() : Optional.ofNullable(mc.getCurrentServerData())
                                                                                                   .map(current -> current.serverIP)
                                                                                                   .orElse("realms");
    }

    public void encode(JsonObject json) {
        json.addProperty("tracers", this.tracers);
        json.addProperty("r", this.r);
        json.addProperty("g", this.g);
        json.addProperty("b", this.b);
        json.addProperty("nametag", this.nametag);
        json.addProperty("dist", this.dist);
        json.addProperty("coords", this.coords);

        for (Map.Entry<String, ServerWaypoints> server : this.identifiersToServerWaypoints.entrySet()) {
            for (Map.Entry<Integer, DimensionWaypoints> dimension : server.getValue().waypoints.entrySet()) {
                for (Map.Entry<String, Waypoint> waypoint : dimension.getValue().waypoints.entrySet()) {
                    json.addProperty(server.getKey() + '.' + dimension.getKey() + '.' + waypoint.getKey() + ".name", waypoint.getValue().name);
                    json.addProperty(server.getKey() + '.' + dimension.getKey() + '.' + waypoint.getKey() + ".x", waypoint.getValue().x);
                    json.addProperty(server.getKey() + '.' + dimension.getKey() + '.' + waypoint.getKey() + ".y", waypoint.getValue().y);
                    json.addProperty(server.getKey() + '.' + dimension.getKey() + '.' + waypoint.getKey() + ".z", waypoint.getValue().z);
                    json.addProperty(server.getKey() + '.' + dimension.getKey() + '.' + waypoint.getKey() + ".dim", waypoint.getValue().dim);
                    json.addProperty(server.getKey() + '.' + dimension.getKey() + '.' + waypoint.getKey() + ".shown", waypoint.getValue().shown);
                }
            }
        }
    }

    public void decode(String fieldName, JsonObject json) {
        this.tracers = this.getBoolean(json, "tracers", this.tracers);
        this.r = this.getInt(json, "r", this.r);
        this.g = this.getInt(json, "g", this.g);
        this.b = this.getInt(json, "b", this.b);
        this.nametag = this.getBoolean(json, "nametag", this.nametag);
        this.dist = this.getBoolean(json, "dist", this.dist);
        this.coords = this.getBoolean(json, "coords", this.coords);

        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            if (entry.getKey().endsWith(".name")) { //waypoint definition
                String baseKey = entry.getKey().substring(0, entry.getKey().length() - 5);
                String name = this.getString(json, baseKey + ".name", UUID.randomUUID().toString());
                int x = this.getInt(json, baseKey + ".x", 0);
                int y = this.getInt(json, baseKey + ".y", 0);
                int z = this.getInt(json, baseKey + ".z", 0);
                int dim = this.getInt(json, baseKey + ".dim", 0);
                boolean shown = this.getBoolean(json, baseKey + ".shown", true);
                String serverIdentifier = this.getString(json, baseKey + ".server", "localhost");
                ServerWaypoints serber = this.identifiersToServerWaypoints.computeIfAbsent(serverIdentifier, k -> new ServerWaypoints());
                DimensionWaypoints dimension = serber.waypoints.computeIfAbsent(dim, k -> new DimensionWaypoints());
                dimension.waypoints.put(name, new Waypoint(name, x, y, z, shown, dim));
            }
        }
    }

    public String name() {
        return "waypoints";
    }

    public Collection<Waypoint> getWaypoints() {
        ServerWaypoints serverWaypoints = this.identifiersToServerWaypoints.computeIfAbsent(getCurrentServerIdentifier(), k -> new ServerWaypoints());
        DimensionWaypoints dimensionWaypoints = serverWaypoints.waypoints.computeIfAbsent(mc.player.dimension, k -> new DimensionWaypoints());
        return dimensionWaypoints.waypoints.values();
    }

    public Waypoint getWaypoint(String name) {
        ServerWaypoints serverWaypoints = this.identifiersToServerWaypoints.computeIfAbsent(getCurrentServerIdentifier(), k -> new ServerWaypoints());
        DimensionWaypoints dimensionWaypoints = serverWaypoints.waypoints.computeIfAbsent(mc.player.dimension, k -> new DimensionWaypoints());
        return dimensionWaypoints.waypoints.get(name);
    }

    public Waypoint addWaypoint(Waypoint waypoint) {
        ServerWaypoints serverWaypoints = this.identifiersToServerWaypoints.computeIfAbsent(getCurrentServerIdentifier(), k -> new ServerWaypoints());
        DimensionWaypoints dimensionWaypoints = serverWaypoints.waypoints.computeIfAbsent(mc.player.dimension, k -> new DimensionWaypoints());
        dimensionWaypoints.waypoints.put(waypoint.name, waypoint);
        return waypoint;
    }

    public Waypoint addWaypoint(String name) {
        return this.addWaypoint(new Waypoint(name, mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.dimension));
    }

    public Waypoint addWaypoint() {
        return this.addWaypoint(UUID.randomUUID().toString());
    }

    public Waypoint removeWaypoint(String name) {
        ServerWaypoints serverWaypoints = this.identifiersToServerWaypoints.computeIfAbsent(getCurrentServerIdentifier(), k -> new ServerWaypoints());
        DimensionWaypoints dimensionWaypoints = serverWaypoints.waypoints.computeIfAbsent(mc.player.dimension, k -> new DimensionWaypoints());
        return dimensionWaypoints.waypoints.remove(name);
    }

    public void clearWaypoints() {
        this.identifiersToServerWaypoints.remove(getCurrentServerIdentifier());
    }

    public void hardClearWaypoints() {
        this.identifiersToServerWaypoints = new Hashtable<>();
    }
}
