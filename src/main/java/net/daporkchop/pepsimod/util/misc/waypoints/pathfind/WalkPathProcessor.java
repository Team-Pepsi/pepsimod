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

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.command.impl.GoToCommand;
import net.daporkchop.pepsimod.module.impl.movement.JesusMod;
import net.daporkchop.pepsimod.module.impl.movement.StepMod;
import net.daporkchop.pepsimod.totally.not.skidded.RotationUtils;
import net.daporkchop.pepsimod.totally.not.skidded.WBlock;
import net.daporkchop.pepsimod.util.ReflectionStuff;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockVine;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class WalkPathProcessor extends PathProcessor {
    @Override
    public void process() {
        // get positions
        BlockPos pos;
        if (PepsiMod.INSTANCE.mc.player.onGround)
            pos = new BlockPos(PepsiMod.INSTANCE.mc.player.posX,
                    PepsiMod.INSTANCE.mc.player.posY + 0.5, PepsiMod.INSTANCE.mc.player.posZ);
        else
            pos = new BlockPos(PepsiMod.INSTANCE.mc.player);
        PathPos nextPos = GoToCommand.INSTANCE.pathFinder.currentTarget;
        int posIndex = GoToCommand.INSTANCE.pathFinder.queue.indexOf(pos);

        // update index
        if (pos.equals(nextPos)) {
            // disable when done
            if (index >= GoToCommand.INSTANCE.pathFinder.queue.size())
                done = true;
            return;
        } else if (posIndex > index) {
            index = posIndex + 1;

            // disable when done
            if (index >= GoToCommand.INSTANCE.pathFinder.queue.size())
                done = true;
            return;
        }

        lockControls();

        // face next position
        facePosition(nextPos);

        if (Math.abs(RotationUtils.getHorizontalAngleToClientRotation(
                new Vec3d(nextPos).addVector(0.5, 0.5, 0.5))) > 90)
            return;

        if (JesusMod.INSTANCE.isEnabled) {
            // wait for Jesus to swim up
            if (PepsiMod.INSTANCE.mc.player.posY < nextPos.getY()
                    && (PepsiMod.INSTANCE.mc.player.isInWater()
                    || PepsiMod.INSTANCE.mc.player.isInLava()))
                return;

            // manually swim down if using Jesus
            if (PepsiMod.INSTANCE.mc.player.posY - nextPos.getY() > 0.5
                    && (PepsiMod.INSTANCE.mc.player.isInWater()
                    || PepsiMod.INSTANCE.mc.player.isInLava()
                    || JesusMod.INSTANCE.isOverLiquid()))
                ReflectionStuff.setPressed(mc.gameSettings.keyBindSneak, true);
        }

        // horizontal movement
        if (pos.getX() != nextPos.getX() || pos.getZ() != nextPos.getZ()) {
            ReflectionStuff.setPressed(mc.gameSettings.keyBindForward, true);

            if (index > 0 && GoToCommand.INSTANCE.pathFinder.queue.get(index - 1).isJumping() || pos.getY() < nextPos.getY()) { //TODO: fix swimming
                if (!(StepMod.INSTANCE.isEnabled && (PepsiMod.INSTANCE.miscOptions.step_legit || PepsiMod.INSTANCE.miscOptions.step_height == 1))) {
                    ReflectionStuff.setPressed(mc.gameSettings.keyBindJump, true);
                }
            }

            // vertical movement
        } else if (pos.getY() != nextPos.getY())
            // go up
            if (pos.getY() < nextPos.getY()) {
                // climb up
                // TODO: Spider
                Block block = WBlock.getBlock(pos);
                if (block instanceof BlockLadder || block instanceof BlockVine) {
                    RotationUtils.faceVectorForWalking(
                            WBlock.getBoundingBox(pos).getCenter());

                    ReflectionStuff.setPressed(mc.gameSettings.keyBindForward, true);

                } else {
                    // directional jump
                    if (index < GoToCommand.INSTANCE.pathFinder.queue.size() - 1
                            && !nextPos.up().equals(GoToCommand.INSTANCE.pathFinder.queue.get(index + 1)))
                        index++;

                    // jump up
                    if (!(StepMod.INSTANCE.isEnabled && (PepsiMod.INSTANCE.miscOptions.step_legit || PepsiMod.INSTANCE.miscOptions.step_height == 1))) {
                        ReflectionStuff.setPressed(mc.gameSettings.keyBindJump, true);
                    }
                }

                // go down
            } else {
                // skip mid-air nodes and go straight to the bottom
                while (index < GoToCommand.INSTANCE.pathFinder.queue.size() - 1
                        && GoToCommand.INSTANCE.pathFinder.queue.get(index).down().equals(GoToCommand.INSTANCE.pathFinder.queue.get(index + 1)))
                    index++;

                // walk off the edge
                if (PepsiMod.INSTANCE.mc.player.onGround)
                    ReflectionStuff.setPressed(mc.gameSettings.keyBindForward, true);
            }
    }

    @Override
    public void lockControls() {
        super.lockControls();
        PepsiMod.INSTANCE.mc.player.capabilities.isFlying = false;
    }
}

