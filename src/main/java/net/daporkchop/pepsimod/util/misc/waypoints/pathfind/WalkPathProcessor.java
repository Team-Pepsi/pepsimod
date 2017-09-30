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
import net.daporkchop.pepsimod.module.impl.movement.StepMod;
import net.daporkchop.pepsimod.totally.not.skidded.RotationUtils;
import net.daporkchop.pepsimod.totally.not.skidded.WBlock;
import net.daporkchop.pepsimod.totally.not.skidded.WMinecraft;
import net.daporkchop.pepsimod.util.ReflectionStuff;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockVine;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;

public class WalkPathProcessor extends PathProcessor {
    public WalkPathProcessor(ArrayList<PathPos> path) {
        super(path);
    }

    @Override
    public void process() {
        // get positions
        BlockPos pos;
        if (WMinecraft.getPlayer().onGround)
            pos = new BlockPos(WMinecraft.getPlayer().posX,
                    WMinecraft.getPlayer().posY + 0.5, WMinecraft.getPlayer().posZ);
        else
            pos = new BlockPos(WMinecraft.getPlayer());
        index = 1;
        PathPos nextPos = null;
        boolean forceNext = false;
        try {
            nextPos = path.get(index);
        } catch (IndexOutOfBoundsException e) {
            forceNext = true;
        }

        // update index
        if (forceNext || pos.equals(nextPos)) {
            GoToCommand.INSTANCE.pathFinder.toRemove.add(nextPos);
            return;
        } else if (WMinecraft.getPlayer().getDistanceSq(nextPos) >= 16 || (!GoToCommand.INSTANCE.pathFinder.goesToGoal && path.size() <= 15)) {
            GoToCommand.INSTANCE.pathFinder = new PathFinder(GoToCommand.INSTANCE.pathFinder.goal);
            return;
        }

        lockControls();

        // face next position
        facePosition(nextPos);

        if (Math.abs(RotationUtils.getHorizontalAngleToClientRotation(new Vec3d(nextPos).addVector(0.5, 0.5, 0.5))) > 90) {
            return;
        }
        if (WMinecraft.getPlayer().isInWater() && pos.getY() <= nextPos.getY()) {
            ReflectionStuff.setPressed(mc.gameSettings.keyBindJump, true);
        }

        // horizontal movement
        if (pos.getX() != nextPos.getX() || pos.getZ() != nextPos.getZ()) {
            ReflectionStuff.setPressed(mc.gameSettings.keyBindForward, true);

            if (index > 0 && path.get(index - 1).jumping || pos.getY() < nextPos.getY()) {
                if (!(StepMod.INSTANCE.isEnabled && (pepsiMod.miscOptions.step_legit || pepsiMod.miscOptions.step_height == 1))) {
                    ReflectionStuff.setPressed(mc.gameSettings.keyBindJump, true);
                }
            }
            // vertical movement
        } else if (pos.getY() != nextPos.getY())
            // go up
            if (pos.getY() < nextPos.getY()) {
                // climb up
                Block block = WBlock.getBlock(pos);
                if (block instanceof BlockLadder || block instanceof BlockVine) {
                    RotationUtils.faceVectorForWalking(WBlock.getBoundingBox(pos).getCenter());
                    ReflectionStuff.setPressed(mc.gameSettings.keyBindForward, true);
                } else {
                    // directional jump
                    if (index < path.size() - 1 && !nextPos.up().equals(path.get(index + 1))) {
                        GoToCommand.INSTANCE.pathFinder.toRemove.add(nextPos);
                    }

                    // jump up
                    if (!(StepMod.INSTANCE.isEnabled && (pepsiMod.miscOptions.step_legit || pepsiMod.miscOptions.step_height == 1))) {
                        ReflectionStuff.setPressed(mc.gameSettings.keyBindJump, true);
                    }
                }
                // go down
            } else {
                // skip mid-air nodes and go straight to the bottom
                int i = index;
                while (i < path.size() - 1 && path.get(i).down().equals(path.get(i + 1))) {
                    i++;
                }
                GoToCommand.INSTANCE.pathFinder = new PathFinder(GoToCommand.INSTANCE.pathFinder.goal);
                GoToCommand.INSTANCE.pathFinder.toRemove.add(path.get(i));

                // walk off the edge
                if (WMinecraft.getPlayer().onGround) {
                    ReflectionStuff.setPressed(mc.gameSettings.keyBindForward, true);
                }
            }
    }

    @Override
    public void lockControls() {
        super.lockControls();
        WMinecraft.getPlayer().capabilities.isFlying = false;
    }
}
