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

import net.daporkchop.pepsimod.command.impl.GoToCommand;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.misc.ITickListener;
import net.daporkchop.pepsimod.util.misc.IWurstRenderListener;

public class PathFindTickListener implements ITickListener, IWurstRenderListener {
    public static PathFindTickListener INSTANCE = null;

    public PathFindTickListener() {
        INSTANCE = this;
    }

    @Override
    public void tick() {
        // find path
        if (GoToCommand.INSTANCE.processor != null) {
            GoToCommand.INSTANCE.processor.lockControls();
        }

        GoToCommand.INSTANCE.pathFinder.think();

        /*if (!GoToCommand.INSTANCE.pathFinder.checkDone()) {
            if (GoToCommand.INSTANCE.pathFinder.isFailed()) {
                Command.clientMessage("Could not find a path.");
                disable();
            }

            return;
        }*/

        GoToCommand.INSTANCE.pathFinder.formatPath();

        // set processor
        if (GoToCommand.INSTANCE.processor == null) {
            GoToCommand.INSTANCE.processor = GoToCommand.INSTANCE.pathFinder.getProcessor();
        }


        // check path
/*        if (!GoToCommand.INSTANCE.pathFinder.isPathStillValid(GoToCommand.INSTANCE.processor.index)) {
            System.out.println("Updating path...");
            GoToCommand.INSTANCE.pathFinder = new PathFinder(GoToCommand.INSTANCE.pathFinder.getGoal());
            return;
        }*/

        // process path
        GoToCommand.INSTANCE.processor.process();

        if (GoToCommand.INSTANCE.hasReachedFinalGoal()) {
            PepsiUtils.toRemoveTickListeners.add(this);
            PepsiUtils.toRemoveWurstRenderListeners.add(this);
            disable();
        }
    }

    @Override
    public void render(float partialTicks) {
        GoToCommand.INSTANCE.pathFinder.renderPath(false, false);
    }

    public void disable() {
        PepsiUtils.toRemoveTickListeners.add(this);
        PepsiUtils.toRemoveWurstRenderListeners.add(this);

        GoToCommand.INSTANCE.pathFinder = null;
        if (GoToCommand.INSTANCE.processor != null) {
            GoToCommand.INSTANCE.processor.stop();
            GoToCommand.INSTANCE.processor = null;
        }

        GoToCommand.INSTANCE.endGoal = null;
        GoToCommand.INSTANCE.enabled = false;
    }
}
