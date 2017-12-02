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

package net.daporkchop.pepsimod.util.misc.waypoints.pathfind;

import net.daporkchop.pepsimod.the.wurst.pkg.name.RotationUtils;
import net.daporkchop.pepsimod.the.wurst.pkg.name.WMinecraft;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.ReflectionStuff;
import net.daporkchop.pepsimod.util.misc.Default;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public abstract class PathProcessor extends Default {

    protected final ArrayList<PathPos> path;
    public boolean lookedLastTick = false;
    protected int index;

    public PathProcessor(ArrayList<PathPos> path) {
        if (path.isEmpty())
            throw new IllegalStateException("There is no path!");

        this.path = path;
    }

    public abstract void process();

    public void stop() {
        releaseControls();
    }

    public void lockControls() {
        // disable keys
        for (KeyBinding key : PepsiUtils.controls)
            ReflectionStuff.setPressed(key, false);

        // face next position
        if (index < path.size())
            facePosition(path.get(index));

        // disable sprinting
        WMinecraft.getPlayer().setSprinting(false);
    }

    protected void facePosition(BlockPos pos) {
        if (lookedLastTick) {
            lookedLastTick = false;
        } else {
            lookedLastTick = RotationUtils.faceVectorForWalking(new Vec3d(pos).addVector(0.5, 0.5, 0.5));
        }
    }

    public final void releaseControls() {
        // reset keys
        for (KeyBinding key : PepsiUtils.controls) {
            ReflectionStuff.setPressed(key, GameSettings.isKeyDown(key));
        }
    }
}
