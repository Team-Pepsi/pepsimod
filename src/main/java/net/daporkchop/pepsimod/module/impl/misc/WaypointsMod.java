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

package net.daporkchop.pepsimod.module.impl.misc;

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.OptionCompletions;
import net.daporkchop.pepsimod.module.api.option.ExtensionSlider;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.config.impl.WaypointsTranslator;
import net.daporkchop.pepsimod.util.misc.waypoints.Waypoint;
import net.daporkchop.pepsimod.util.render.LineRenderer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

public class WaypointsMod extends Module {
    public static WaypointsMod INSTANCE;

    protected Collection<Waypoint> renderCache;

    {
        INSTANCE = this;
    }

    public WaypointsMod() {
        super("Waypoints");
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void tick() {
    }

    @Override
    public void init() {
        INSTANCE = this;
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new ModuleOption<>(WaypointsTranslator.INSTANCE.tracers, "tracers", OptionCompletions.BOOLEAN,
                        (value) -> {
                            WaypointsTranslator.INSTANCE.tracers = value;
                            return true;
                        },
                        () -> {
                            return WaypointsTranslator.INSTANCE.tracers;
                        }, "Tracers"),
                new ModuleOption<>(WaypointsTranslator.INSTANCE.nametag, "nametag", OptionCompletions.BOOLEAN,
                        (value) -> {
                            WaypointsTranslator.INSTANCE.nametag = value;
                            return true;
                        },
                        () -> {
                            return WaypointsTranslator.INSTANCE.nametag;
                        }, "Nametag"),
                new ModuleOption<>(WaypointsTranslator.INSTANCE.dist, "distance", OptionCompletions.BOOLEAN,
                        (value) -> {
                            WaypointsTranslator.INSTANCE.dist = value;
                            return true;
                        },
                        () -> {
                            return WaypointsTranslator.INSTANCE.dist;
                        }, "Distance"),
                new ModuleOption<>(WaypointsTranslator.INSTANCE.coords, "coords", OptionCompletions.BOOLEAN,
                        (value) -> {
                            WaypointsTranslator.INSTANCE.coords = value;
                            return true;
                        },
                        () -> {
                            return WaypointsTranslator.INSTANCE.coords;
                        }, "Coords"),
                new ModuleOption<>(WaypointsTranslator.INSTANCE.r, "red", new String[]{"0", "256"},
                        (value) -> {
                            WaypointsTranslator.INSTANCE.r = Math.max(0, Math.min(value, 255));
                            return true;
                        },
                        () -> {
                            return WaypointsTranslator.INSTANCE.r;
                        }, "Red", new ExtensionSlider(ExtensionType.VALUE_INT, 0, 255, 1)),
                new ModuleOption<>(WaypointsTranslator.INSTANCE.g, "green", new String[]{"0", "256"},
                        (value) -> {
                            WaypointsTranslator.INSTANCE.g = Math.max(0, Math.min(value, 255));
                            return true;
                        },
                        () -> {
                            return WaypointsTranslator.INSTANCE.g;
                        }, "Green", new ExtensionSlider(ExtensionType.VALUE_INT, 0, 255, 1)),
                new ModuleOption<>(WaypointsTranslator.INSTANCE.b, "blue", new String[]{"0", "256"},
                        (value) -> {
                            WaypointsTranslator.INSTANCE.b = Math.max(0, Math.min(value, 255));
                            return true;
                        },
                        () -> {
                            return WaypointsTranslator.INSTANCE.b;
                        }, "Blue", new ExtensionSlider(ExtensionType.VALUE_INT, 0, 255, 1))
        };
    }

    @Override
    public void renderLines(LineRenderer renderer) {
        this.renderCache = new ArrayList<>(WaypointsTranslator.INSTANCE.getWaypoints());

        renderer.color(WaypointsTranslator.INSTANCE.r, WaypointsTranslator.INSTANCE.g, WaypointsTranslator.INSTANCE.b);
        for (Waypoint waypoint : this.renderCache) {
            renderer.lineFromEyes(waypoint.x, waypoint.y, waypoint.z);
        }
    }

    public void renderText() {
        if (this.state.enabled && WaypointsTranslator.INSTANCE.nametag) {
            if (this.renderCache == null) {
                this.renderCache = WaypointsTranslator.INSTANCE.getWaypoints();
            }
            for (Waypoint waypoint : this.renderCache) {
                String text = waypoint.name;
                if (WaypointsTranslator.INSTANCE.coords) {
                    text += " \u00A77" + waypoint.x + "\u00A7f, \u00A77" + waypoint.y + "\u00A7f, \u00A77" + waypoint.z;
                }
                if (WaypointsTranslator.INSTANCE.dist) {
                    text += " \u00A7f (\u00A7b" + PepsiUtils.roundFloatForSlider((float) mc.player.getDistance(waypoint.x, waypoint.y, waypoint.z)) + "\u00A7f)";
                }
                PepsiUtils.renderFloatingText(text,
                        waypoint.x, waypoint.y + 1, waypoint.z,
                        Color.white.getRGB(),
                        true, 1.0f);
            }
            this.renderCache.clear();
        }
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.MISC;
    }
}
