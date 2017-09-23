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

package net.daporkchop.pepsimod.util.module;

import java.io.Serializable;
import java.util.HashMap;

public class MiscOptions implements Serializable {
    private static final long serialVersionUID = 6988070214567038785L;

    public HashMap<String, ModuleState> states = new HashMap<>();
    public boolean criticals_packet = true;
    public float entitySpeed_speed = 1.0f;
    public float crystalAura_speed = 1.0f;
    public float crystalAura_range = 3.8f;
    public float entityStep_step = 1f;
    public float flight_speed = 1.0f;
    public float speedmine_speed = 0.4f;
    public float autoEat_threshold = 7f;
    public boolean step_legit = false;
    public int step_height = 1;
    public int cpu_framecap = 5;
    public boolean waypoints_tracers = false;
    public int waypoints_r = 0;
    public int waypoints_g = 0;
    public int waypoints_b = 0;
    public boolean waypoints_nametag = false;
    public boolean waypoints_dist = true;
    public boolean waypoints_coords = false;

    public static class ModuleState implements Serializable {
        public boolean enabled;
        public boolean hidden;

        public ModuleState(boolean a, boolean b) {
            enabled = a;
            hidden = b;
        }
    }
}

