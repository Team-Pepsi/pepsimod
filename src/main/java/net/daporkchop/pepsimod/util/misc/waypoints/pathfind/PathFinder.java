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
import net.daporkchop.pepsimod.module.impl.misc.NoFallMod;
import net.daporkchop.pepsimod.module.impl.movement.FlightMod;
import net.daporkchop.pepsimod.module.impl.movement.JesusMod;
import net.daporkchop.pepsimod.module.impl.movement.NoSlowdownMod;
import net.daporkchop.pepsimod.totally.not.skidded.WBlock;
import net.daporkchop.pepsimod.totally.not.skidded.WMinecraft;
import net.daporkchop.pepsimod.util.ReflectionStuff;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

public class PathFinder {
    public final HashMap<PathPos, PathPos> prevPosMap = new HashMap<>();
    public final boolean invulnerable =
            WMinecraft.getPlayer().capabilities.isCreativeMode;
    public final boolean creativeFlying =
            WMinecraft.getPlayer().capabilities.isFlying;
    public final boolean flying =
            creativeFlying || FlightMod.INSTANCE.isEnabled;
    public final boolean immuneToFallDamage =
            invulnerable || NoFallMod.INSTANCE.isEnabled;
    public final boolean noWaterSlowdown =
            NoSlowdownMod.INSTANCE.isEnabled;
    public final boolean jesus = JesusMod.INSTANCE.isEnabled;
    public final BlockPos goal;
    public final HashMap<PathPos, Float> costMap = new HashMap<>();
    public final PathQueue queue = new PathQueue();
    //TODO: public final boolean spider = wurst.mods.spiderMod.isActive();
    public PathPos start;
    public boolean fallingAllowed = true;
    public boolean divingAllowed = true;
    public PathPos current;
    public boolean done, actuallyDone = false;
    public boolean failed;
    public PathPos currentTarget = null;
    public ArrayList<PathPos> toRemove = new ArrayList<>();

    public PathFinder(BlockPos goal) {
        if (WMinecraft.getPlayer().onGround)
            start = new PathPos(new BlockPos(WMinecraft.getPlayer().posX,
                    WMinecraft.getPlayer().posY + 0.5,
                    WMinecraft.getPlayer().posZ));
        else
            start = new PathPos(new BlockPos(WMinecraft.getPlayer()));
        this.goal = goal;

        costMap.put(start, 0F);
        queue.add(start, getHeuristic(start));
    }

    public PathFinder(PathFinder pathFinder) {
        this(pathFinder.goal);
    }

    public void think() {
        //if (done)
        //    return;

        try {
            int i = 0;
            for (; ; ) {
                // get next position from queue
                current = queue.poll();

                // check if path is found
                if (goal.equals(current))
                    return;

                // add neighbors to queue
                for (PathPos next : getNeighbors(current)) {
                    // check cost
                    float newCost = costMap.get(current) + getCost(current, next);
                    if (costMap.containsKey(next) && costMap.get(next) <= newCost)
                        continue;

                    // add to queue
                    costMap.put(next, newCost);
                    prevPosMap.put(next, current);
                    queue.add(next, newCost + getHeuristic(next));
                }
            }
        } catch (OutOfWorldException | NullPointerException e) {
            done = true;
            queue.cancelledPositions.add(current);
        }
    }

    public ArrayList<PathPos> getNeighbors(PathPos pos) {
        ArrayList<PathPos> neighbors = new ArrayList<>();

        // abort if too far away
        if (Math.abs(start.getX() - pos.getX()) > 256
                || Math.abs(start.getZ() - pos.getZ()) > 256)
            return neighbors;

        // get all neighbors
        BlockPos north = pos.north();
        BlockPos east = pos.east();
        BlockPos south = pos.south();
        BlockPos west = pos.west();

        BlockPos northEast = north.east();
        BlockPos southEast = south.east();
        BlockPos southWest = south.west();
        BlockPos northWest = north.west();

        BlockPos up = pos.up();
        BlockPos down = pos.down();

        // flying
        boolean flying = canFlyAt(pos);
        // walking
        boolean onGround = canBeSolid(down);

        // player can move sideways if flying, standing on the ground, jumping,
        // or inside of a block that allows sideways movement (ladders, webs,
        // etc.)
        if (flying || onGround || pos.isJumping()
                || canMoveSidewaysInMidairAt(pos) || canClimbUpAt(pos.down())) {
            // north
            if (checkHorizontalMovement(pos, north))
                neighbors.add(new PathPos(north));

            // east
            if (checkHorizontalMovement(pos, east))
                neighbors.add(new PathPos(east));

            // south
            if (checkHorizontalMovement(pos, south))
                neighbors.add(new PathPos(south));

            // west
            if (checkHorizontalMovement(pos, west))
                neighbors.add(new PathPos(west));

            // north-east
            if (checkDiagonalMovement(pos, EnumFacing.NORTH, EnumFacing.EAST))
                neighbors.add(new PathPos(northEast));

            // south-east
            if (checkDiagonalMovement(pos, EnumFacing.SOUTH, EnumFacing.EAST))
                neighbors.add(new PathPos(southEast));

            // south-west
            if (checkDiagonalMovement(pos, EnumFacing.SOUTH, EnumFacing.WEST))
                neighbors.add(new PathPos(southWest));

            // north-west
            if (checkDiagonalMovement(pos, EnumFacing.NORTH, EnumFacing.WEST))
                neighbors.add(new PathPos(northWest));
        }

        // up
        if (pos.getY() < 256 && canGoThrough(up.up())
                && (flying || onGround || canClimbUpAt(pos))
                && (flying || canClimbUpAt(pos) || goal.equals(up)
                || canSafelyStandOn(north) || canSafelyStandOn(east)
                || canSafelyStandOn(south) || canSafelyStandOn(west))
                && (divingAllowed || WBlock.getMaterial(up.up()) != Material.WATER))
            neighbors.add(new PathPos(up, onGround));

        // down
        if (pos.getY() > 0 && canGoThrough(down) && canGoAbove(down.down())
                && (flying || canFallBelow(pos))
                && (divingAllowed || WBlock.getMaterial(pos) != Material.WATER))
            neighbors.add(new PathPos(down));

        return neighbors;
    }

    public boolean checkHorizontalMovement(BlockPos current, BlockPos next) {
        if (!WMinecraft.getWorld().isBlockLoaded(next, false)) {
            throw new OutOfWorldException();
        }
        return isPassable(next) && (canFlyAt(current) || canGoThrough(next.down())
                || canSafelyStandOn(next.down()));

    }

    public boolean checkDiagonalMovement(BlockPos current,
                                         EnumFacing direction1, EnumFacing direction2) {
        if (!WMinecraft.getWorld().isBlockLoaded(current.offset(direction1).offset(direction2), false)) {
            throw new OutOfWorldException();
        }
        BlockPos horizontal1 = current.offset(direction1);
        BlockPos horizontal2 = current.offset(direction2);
        BlockPos next = horizontal1.offset(direction2);

        return isPassable(horizontal1) && isPassable(horizontal2)
                && checkHorizontalMovement(current, next);

    }

    public boolean isPassable(BlockPos pos) {
        return canGoThrough(pos) && canGoThrough(pos.up())
                && canGoAbove(pos.down()) && (divingAllowed
                || WBlock.getMaterial(pos.up()) != Material.WATER);
    }

    public boolean canBeSolid(BlockPos pos) {
        Material material = WBlock.getMaterial(pos);
        Block block = WBlock.getBlock(pos);
        return material.blocksMovement() && !(block instanceof BlockSign)
                || block instanceof BlockLadder || jesus
                && (material == Material.WATER || material == Material.LAVA);
    }

    public boolean canGoThrough(BlockPos pos) {
        // check if loaded
        if (!WMinecraft.getWorld().isBlockLoaded(pos, false))
            return false;

        // check if solid
        Material material = WBlock.getMaterial(pos);
        Block block = WBlock.getBlock(pos);
        if (material.blocksMovement() && !(block instanceof BlockSign))
            return false;

        // check if trapped
        if (block instanceof BlockTripWire
                || block instanceof BlockPressurePlate)
            return false;

        // check if safe
        return !(!invulnerable
                && (material == Material.LAVA || material == Material.FIRE));
    }

    public boolean canGoAbove(BlockPos pos) {
        // check for fences, etc.
        Block block = WBlock.getBlock(pos);
        return !(block instanceof BlockFence || block instanceof BlockWall
                || block instanceof BlockFenceGate);
    }

    public boolean canSafelyStandOn(BlockPos pos) {
        // check if solid
        Material material = WBlock.getMaterial(pos);
        if (!canBeSolid(pos))
            return false;

        // check if safe
        return !(!invulnerable
                && (material == Material.CACTUS || material == Material.LAVA));
    }

    public boolean canFallBelow(PathPos pos) {
        // check if player can keep falling
        BlockPos down2 = pos.down(2);
        if (fallingAllowed && canGoThrough(down2))
            return true;

        // check if player can stand below
        if (!canSafelyStandOn(down2))
            return false;

        // check if fall damage is off
        if (immuneToFallDamage && fallingAllowed)
            return true;

        // check if fall ends with slime block
        if (WBlock.getBlock(down2) instanceof BlockSlime && fallingAllowed)
            return true;

        // check fall damage
        BlockPos prevPos = pos;
        for (int i = 0; i <= (fallingAllowed ? 3 : 1); i++) {
            // check if prevPos does not exist, meaning that the pathfinding
            // started during the fall and fall damage should be ignored because
            // it cannot be prevented
            if (prevPos == null)
                return true;

            // check if point is not part of this fall, meaning that the fall is
            // too short to cause any damage
            if (!pos.up(i).equals(prevPos))
                return true;

            // check if block resets fall damage
            Block prevBlock = WBlock.getBlock(prevPos);
            if (prevBlock instanceof BlockLiquid
                    || prevBlock instanceof BlockLadder
                    || prevBlock instanceof BlockVine
                    || prevBlock instanceof BlockWeb)
                return true;

            prevPos = prevPosMap.get(prevPos);
        }

        return false;
    }

    public boolean canFlyAt(BlockPos pos) {
        return flying
                || !noWaterSlowdown && WBlock.getMaterial(pos) == Material.WATER;
    }

    public boolean canClimbUpAt(BlockPos pos) {
        // check if this block works for climbing
        Block block = WBlock.getBlock(pos);
        if (!(block instanceof BlockLadder)
                && !(block instanceof BlockVine))
            return false;

        // check if any adjacent block is solid
        BlockPos up = pos.up();
        return !(!canBeSolid(pos.north()) && !canBeSolid(pos.east())
                && !canBeSolid(pos.south()) && !canBeSolid(pos.west())
                && !canBeSolid(up.north()) && !canBeSolid(up.east())
                && !canBeSolid(up.south()) && !canBeSolid(up.west()));
    }

    public boolean canMoveSidewaysInMidairAt(BlockPos pos) {
        // check feet
        Block blockFeet = WBlock.getBlock(pos);
        if (blockFeet instanceof BlockLiquid || blockFeet instanceof BlockLadder
                || blockFeet instanceof BlockVine || blockFeet instanceof BlockWeb)
            return true;

        // check head
        Block blockHead = WBlock.getBlock(pos.up());
        return blockHead instanceof BlockLiquid || blockHead instanceof BlockWeb;

    }

    public float getCost(BlockPos current, BlockPos next) {
        float[] costs = {0.5F, 0.5F};
        BlockPos[] positions = new BlockPos[]{current, next};

        for (int i = 0; i < positions.length; i++) {
            Material material = WBlock.getMaterial(positions[i]);

            // liquids
            if (material == Material.WATER && !noWaterSlowdown)
                costs[i] *= 1.3164437838225804F;
            else if (material == Material.LAVA)
                costs[i] *= 4.539515393656079F;

            // soul sand
            if (!canFlyAt(positions[i]) && WBlock
                    .getBlock(positions[i].down()) instanceof BlockSoulSand)
                costs[i] *= 2.5F;
        }

        float cost = costs[0] + costs[1];

        // diagonal movement
        if (current.getX() != next.getX() && current.getZ() != next.getZ())
            cost *= 1.4142135623730951F;

        return cost;
    }

    public float getHeuristic(BlockPos pos) {
        float dx = Math.abs(pos.getX() - goal.getX());
        float dy = Math.abs(pos.getY() - goal.getY());
        float dz = Math.abs(pos.getZ() - goal.getZ());
        return 1.001F * (dx + dy + dz - 0.5857864376269049F * Math.min(dx, dz));
    }

    public PathPos getCurrentPos() {
        return current;
    }

    public BlockPos getGoal() {
        return goal;
    }

    public int countProcessedBlocks() {
        return prevPosMap.keySet().size();
    }

    public int getQueueSize() {
        return queue.size();
    }

    public float getCost(BlockPos pos) {
        return costMap.get(pos);
    }

    public boolean isDone() {
        return done;
    }

    public boolean isFailed() {
        return failed;
    }

    public ArrayList<PathPos> formatPath() {
        GoToCommand.INSTANCE.path.clear();
        PathPos playerPos = new PathPos(new BlockPos(PepsiMod.INSTANCE.mc.player));
        for (Iterator<PathPos> iterator = prevPosMap.keySet().iterator(); iterator.hasNext(); ) {
            PathPos pos = iterator.next();
            if (pos.roughEquals(playerPos)) {
                toRemove.add(pos);
            }
            if (!WMinecraft.getWorld().isBlockLoaded(pos)) {
                toRemove.add(pos);
            }
        }
        for (PathPos pos : toRemove) {
            prevPosMap.remove(pos);
            costMap.remove(pos);
            queue.removePoint(pos);
            ArrayList<PathPos> list = getNeighbors(pos);
            for (PathPos a : list) {
                queue.removePoint(a);
            }
        }
        toRemove.clear();

        // get last position
        PathPos pos = start = current;
        if (!failed)
            pos = current;
        else {
            pos = start;
            for (PathPos next : prevPosMap.keySet())
                if (getHeuristic(next) < getHeuristic(pos)
                        && (canFlyAt(next) || canBeSolid(next.down())))
                    pos = next;
        }

        // get positions
        while (pos != null) {
            GoToCommand.INSTANCE.path.add(pos);
            pos = prevPosMap.get(pos);
        }
        if (GoToCommand.INSTANCE.path.size() < 2) {
            pos = prevPosMap.values().iterator().next();
            while (pos != null) {
                GoToCommand.INSTANCE.path.add(pos);
                pos = prevPosMap.get(pos);
            }
            //System.out.println("k so we had to do a thing xd, path size is now " + path.size());
        }

        // reverse path
        Collections.reverse(GoToCommand.INSTANCE.path);

        return GoToCommand.INSTANCE.path;
    }

    public void renderPath(boolean debugMode, boolean depthTest) {
        // GL settings
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        if (!depthTest)
            GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);

        GL11.glPushMatrix();
        GL11.glTranslated(
                -ReflectionStuff.getRenderPosX(Minecraft.getMinecraft().getRenderManager()),
                -ReflectionStuff.getRenderPosY(Minecraft.getMinecraft().getRenderManager()),
                -ReflectionStuff.getRenderPosZ(Minecraft.getMinecraft().getRenderManager()));
        GL11.glTranslated(0.5, 0.5, 0.5);

        if (debugMode) {
            int renderedThings = 0;

            // queue (yellow)
            GL11.glLineWidth(2);
            GL11.glColor4f(1, 1, 0, 0.75F);
            for (PathPos element : queue.toArray()) {
                if (renderedThings >= 5000)
                    break;

                PathRenderer.renderNode(element);
                renderedThings++;
            }

            // processed (red)
            GL11.glLineWidth(2);
            for (Entry<PathPos, PathPos> entry : prevPosMap.entrySet()) {
                if (renderedThings >= 5000)
                    break;

                if (entry.getKey().isJumping())
                    GL11.glColor4f(1, 0, 1, 0.75F);
                else
                    GL11.glColor4f(1, 0, 0, 0.75F);

                PathRenderer.renderArrow(entry.getValue(), entry.getKey());
                renderedThings++;
            }
        }

        // path (blue)
        if (debugMode) {
            GL11.glLineWidth(4);
            GL11.glColor4f(0, 0, 1, 0.75F);
        } else {
            GL11.glLineWidth(2);
            GL11.glColor4f(0, 1, 0, 0.75F);
        }
        for (int i = 0; i < GoToCommand.INSTANCE.path.size() - 1; i++)
            PathRenderer.renderArrow(GoToCommand.INSTANCE.path.get(i), GoToCommand.INSTANCE.path.get(i + 1));

        GL11.glPopMatrix();

        // GL resets
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
    }

    public boolean isPathStillValid(int index) {
        if (GoToCommand.INSTANCE.path.isEmpty())
            throw new IllegalStateException("Path is not formatted!");

        // check player abilities
        if (invulnerable != WMinecraft.getPlayer().capabilities.isCreativeMode
                || flying != (creativeFlying || FlightMod.INSTANCE.isEnabled)
                || immuneToFallDamage != (invulnerable
                || NoFallMod.INSTANCE.isEnabled)
                || noWaterSlowdown != NoSlowdownMod.INSTANCE.isEnabled
                || jesus != JesusMod.INSTANCE.isEnabled)
            //TODO: || spider != wurst.mods.spiderMod.isActive())
            return false;

        // check path
        for (int i = Math.max(1, index); i < GoToCommand.INSTANCE.path.size(); i++)
            if (!getNeighbors(GoToCommand.INSTANCE.path.get(i - 1)).contains(GoToCommand.INSTANCE.path.get(i)))
                return false;

        return true;
    }

    public PathProcessor getProcessor() {
        return new WalkPathProcessor(GoToCommand.INSTANCE.path);
    }

    public void setFallingAllowed(boolean fallingAllowed) {
        this.fallingAllowed = fallingAllowed;
    }

    public void setDivingAllowed(boolean divingAllowed) {
        this.divingAllowed = divingAllowed;
    }
}
