/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2019 DaPorkchop_
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

package net.daporkchop.pepsimod.util.misc.waypoints.pathfind;

import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.daporkchop.pepsimod.command.impl.GoToCommand;
import net.daporkchop.pepsimod.module.impl.movement.JesusMod;
import net.daporkchop.pepsimod.module.impl.movement.NoSlowdownMod;
import net.daporkchop.pepsimod.the.wurst.pkg.name.WBlock;
import net.daporkchop.pepsimod.the.wurst.pkg.name.WMinecraft;
import net.daporkchop.pepsimod.util.PepsiConstants;
import net.daporkchop.pepsimod.util.ReflectionStuff;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockMagma;
import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.BlockSign;
import net.minecraft.block.BlockSlime;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.BlockTripWire;
import net.minecraft.block.BlockVine;
import net.minecraft.block.BlockWall;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

public class PathFinder extends PepsiConstants {
    public final HashMap<PathPos, PathPos> prevPosMap = new HashMap<>();
    public final boolean noWaterSlowdown = NoSlowdownMod.INSTANCE.state.enabled;
    public final boolean jesus = JesusMod.INSTANCE.state.enabled;
    public final BlockPos goal;
    public final Object2FloatMap<PathPos> costMap = new Object2FloatOpenHashMap<>();
    public final PathQueue queue = new PathQueue();
    public PathPos start;
    public boolean fallingAllowed = true;
    public boolean divingAllowed = false;
    public PathPos current;
    public boolean done;
    public boolean failed;
    public ArrayList<PathPos> toRemove = new ArrayList<>();
    public boolean goesToGoal = true;

    public PathFinder(BlockPos goal) {
        this.start = mc.player.onGround ? new PathPos(new BlockPos(mc.player.posX, mc.player.posY + 0.5, mc.player.posZ)) : new PathPos(new BlockPos(mc.player));
        this.goal = goal;

        this.costMap.put(this.start, 0F);
        this.queue.add(this.start, this.getHeuristic(this.start));
    }

    public void think() {
        try {
            for (; !this.goal.equals(this.current); ) {
                // get next position from queue
                this.current = this.queue.poll();

                // check if path is found
                if (this.goal.equals(this.current)) {
                    return;
                }

                // add neighbors to queue
                for (PathPos next : this.getNeighbors(this.current)) {
                    // check cost
                    float newCost = this.costMap.get(this.current) + this.getCost(this.current, next);
                    if (this.costMap.containsKey(next) && this.costMap.get(next) <= newCost) {
                        continue;
                    }

                    // add to queue
                    this.costMap.put(next, newCost);
                    this.prevPosMap.put(next, this.current);
                    this.queue.add(next, newCost + this.getHeuristic(next));
                }
            }
        } catch (OutOfWorldException | NullPointerException e) {
            this.done = true;
            this.goesToGoal = false;
            this.queue.cancelledPositions.add(this.current);
        }
    }

    public ArrayList<PathPos> getNeighbors(PathPos pos) {
        ArrayList<PathPos> neighbors = new ArrayList<>();

        // abort if too far away
        if (Math.abs(this.start.getX() - pos.getX()) > 256 || Math.abs(this.start.getZ() - pos.getZ()) > 256) {
            return neighbors;
        }

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
        boolean flying = this.canFlyAt(pos);
        // walking
        boolean onGround = this.canBeSolid(down);

        // player can move sideways if flying, standing on the ground, jumping,
        // or inside of a block that allows sideways movement (ladders, webs,
        // etc.)
        if (flying || onGround || pos.jumping || this.canMoveSidewaysInMidairAt(pos) || this.canClimbUpAt(pos.down())) {
            // north
            if (this.checkHorizontalMovement(pos, north)) {
                neighbors.add(new PathPos(north));
            }

            // east
            if (this.checkHorizontalMovement(pos, east)) {
                neighbors.add(new PathPos(east));
            }

            // south
            if (this.checkHorizontalMovement(pos, south)) {
                neighbors.add(new PathPos(south));
            }

            // west
            if (this.checkHorizontalMovement(pos, west)) {
                neighbors.add(new PathPos(west));
            }

            // north-east
            if (this.checkDiagonalMovement(pos, EnumFacing.NORTH, EnumFacing.EAST)) {
                neighbors.add(new PathPos(northEast));
            }

            // south-east
            if (this.checkDiagonalMovement(pos, EnumFacing.SOUTH, EnumFacing.EAST)) {
                neighbors.add(new PathPos(southEast));
            }

            // south-west
            if (this.checkDiagonalMovement(pos, EnumFacing.SOUTH, EnumFacing.WEST)) {
                neighbors.add(new PathPos(southWest));
            }

            // north-west
            if (this.checkDiagonalMovement(pos, EnumFacing.NORTH, EnumFacing.WEST)) {
                neighbors.add(new PathPos(northWest));
            }
        }

        // up
        if (pos.getY() < 256 && this.canGoThrough(up.up()) && (flying || onGround || this.canClimbUpAt(pos)) && (flying || this.canClimbUpAt(pos) || this.goal.equals(up) || this.canSafelyStandOn(north) || this.canSafelyStandOn(east) || this.canSafelyStandOn(south) || this.canSafelyStandOn(west)) && (this.divingAllowed || WBlock.getMaterial(up.up()) != Material.WATER)) {
            neighbors.add(new PathPos(up, onGround));
        }

        // down
        if (pos.getY() > 0 && this.canGoThrough(down) && this.canGoAbove(down.down()) && (flying || this.canFallBelow(pos)) && (this.divingAllowed || WBlock.getMaterial(pos) != Material.WATER)) {
            neighbors.add(new PathPos(down));
        }

        return neighbors;
    }

    public boolean checkHorizontalMovement(BlockPos current, BlockPos next) {
        if (!WMinecraft.getWorld().isBlockLoaded(next, false)) {
            throw new OutOfWorldException();
        }

        return this.isPassable(next) && (this.canFlyAt(current) || this.canGoThrough(next.down()) || this.canSafelyStandOn(next.down()));

    }

    public boolean checkDiagonalMovement(BlockPos current, EnumFacing direction1, EnumFacing direction2) {
        if (!WMinecraft.getWorld().isBlockLoaded(current.offset(direction1).offset(direction2), false)) {
            throw new OutOfWorldException();
        }

        BlockPos horizontal1 = current.offset(direction1);
        BlockPos horizontal2 = current.offset(direction2);
        BlockPos next = horizontal1.offset(direction2);

        return this.isPassable(horizontal1) && this.isPassable(horizontal2) && this.checkHorizontalMovement(current, next);

    }

    public boolean isPassable(BlockPos pos) {
        return this.canGoThrough(pos) && this.canGoThrough(pos.up()) && this.canGoAbove(pos.down()) && (this.divingAllowed || WBlock.getMaterial(pos.up()) != Material.WATER);
    }

    public boolean canBeSolid(BlockPos pos) {
        Material material = WBlock.getMaterial(pos);
        Block block = WBlock.getBlock(pos);
        return material.blocksMovement() && !(block instanceof BlockSign) || block instanceof BlockLadder || this.jesus && (material == Material.WATER || material == Material.LAVA);
    }

    public boolean canGoThrough(BlockPos pos) {
        // check if loaded
        if (!WMinecraft.getWorld().isBlockLoaded(pos, false)) {
            return false;
        }

        // check if solid
        Material material = WBlock.getMaterial(pos);
        Block block = WBlock.getBlock(pos);
        if (material.blocksMovement() && !(block instanceof BlockSign)) {
            return false;
        }

        // check if trapped
        if (block instanceof BlockTripWire || block instanceof BlockPressurePlate) {
            return false;
        }

        // check if safe
        return !(material == Material.LAVA || material == Material.FIRE);
    }

    public boolean canGoAbove(BlockPos pos) {
        // check for fences, etc.
        Block block = WBlock.getBlock(pos);
        return !(block instanceof BlockFence || block instanceof BlockWall || block instanceof BlockFenceGate);
    }

    public boolean canSafelyStandOn(BlockPos pos) {
        // check if solid
        IBlockState state = WBlock.getState(pos);
        Material material = state.getMaterial();
        if (!this.canBeSolid(pos)) {
            return false;
        }

        // check if safe
        return !(material == Material.CACTUS || material == Material.LAVA || state.getBlock() instanceof BlockMagma);
    }

    public boolean canFallBelow(PathPos pos) {
        // check if player can keep falling
        BlockPos down2 = pos.down(2);
        if (this.fallingAllowed && this.canGoThrough(down2)) {
            return true;
        }

        // check if player can stand below
        if (!this.canSafelyStandOn(down2)) {
            return false;
        }

        // check if fall ends with slime block
        if (WBlock.getBlock(down2) instanceof BlockSlime && this.fallingAllowed) {
            return true;
        }

        // check fall damage
        BlockPos prevPos = pos;
        for (int i = 0; i <= (this.fallingAllowed ? 3 : 1); i++) {
            // check if prevPos does not exist, meaning that the pathfinding
            // started during the fall and fall damage should be ignored because
            // it cannot be prevented
            if (prevPos == null) {
                return true;
            }

            // check if point is not part of this fall, meaning that the fall is
            // too short to cause any damage
            if (!pos.up(i).equals(prevPos)) {
                return true;
            }

            // check if block resets fall damage
            Block prevBlock = WBlock.getBlock(prevPos);
            if (prevBlock instanceof BlockLiquid || prevBlock instanceof BlockLadder || prevBlock instanceof BlockVine || prevBlock instanceof BlockWeb) {
                return true;
            }

            prevPos = this.prevPosMap.get(prevPos);
        }

        return false;
    }

    public boolean canFlyAt(BlockPos pos) {
        return !this.noWaterSlowdown && WBlock.getMaterial(pos) == Material.WATER;
    }

    public boolean canClimbUpAt(BlockPos pos) {
        // check if this block works for climbing
        Block block = WBlock.getBlock(pos);
        if (!(block instanceof BlockLadder) && !(block instanceof BlockVine)) {
            return false;
        }

        // check if any adjacent block is solid
        BlockPos up = pos.up();
        return !(!this.canBeSolid(pos.north()) && !this.canBeSolid(pos.east()) && !this.canBeSolid(pos.south()) && !this.canBeSolid(pos.west()) && !this.canBeSolid(up.north()) && !this.canBeSolid(up.east()) && !this.canBeSolid(up.south()) && !this.canBeSolid(up.west()));
    }

    public boolean canMoveSidewaysInMidairAt(BlockPos pos) {
        // check feet
        Block blockFeet = WBlock.getBlock(pos);
        if (blockFeet instanceof BlockLiquid || blockFeet instanceof BlockLadder || blockFeet instanceof BlockVine || blockFeet instanceof BlockWeb) {
            return true;
        }

        // check head
        Block blockHead = WBlock.getBlock(pos.up());
        return blockHead instanceof BlockLiquid || blockHead instanceof BlockWeb;
    }

    public float getCost(BlockPos current, BlockPos next) {
        float[] costs = {0.5F, 0.5F};
        BlockPos[] positions = {current, next};

        for (int i = 0; i < positions.length; i++) {
            Material material = WBlock.getMaterial(positions[i]);

            // liquids
            if (material == Material.WATER && !this.noWaterSlowdown) {
                costs[i] *= 1.5F;
            } else if (material == Material.LAVA) {
                costs[i] *= 100F;
            }

            // soul sand
            if (!this.canFlyAt(positions[i]) && WBlock.getBlock(positions[i].down()) instanceof BlockSoulSand) {
                costs[i] *= 2.5F;
            }
        }

        float cost = costs[0] + costs[1];

        // diagonal movement
        if (current.getX() != next.getX() && current.getZ() != next.getZ()) {
            cost *= 1.4142135623730951F;
        }

        return cost;
    }

    public float getHeuristic(BlockPos pos) {
        float dx = Math.abs(pos.getX() - this.goal.getX());
        float dy = Math.abs(pos.getY() - this.goal.getY());
        float dz = Math.abs(pos.getZ() - this.goal.getZ());
        return 1.001F * (dx + dy + dz - 0.5857864376269049F * Math.min(dx, dz));
    }

    public ArrayList<PathPos> formatPath() {
        GoToCommand.INSTANCE.path.clear();
        PathPos playerPos = new PathPos(new BlockPos(mc.player));
        for (PathPos pos : this.prevPosMap.keySet()) {
            if (pos.roughEquals(playerPos) || !mc.world.isBlockLoaded(pos)) {
                this.toRemove.add(pos);
            }
        }
        this.toRemove.forEach(pos -> {
            if (pos != null) {
                this.prevPosMap.remove(pos);
                this.costMap.remove(pos);
                this.queue.removePoint(pos);
                ArrayList<PathPos> list = this.getNeighbors(pos);
                for (PathPos a : list) {
                    this.queue.removePoint(a);
                }
            }
        });
        this.toRemove.clear();

        // get last position
        PathPos pos = this.start = this.current;
        if (!this.failed) {
            pos = this.current;
        } else {
            pos = this.start;
            for (PathPos next : this.prevPosMap.keySet()) {
                if (this.getHeuristic(next) < this.getHeuristic(pos)
                        && (this.canFlyAt(next) || this.canBeSolid(next.down()))) {
                    pos = next;
                }
            }
        }

        // get positions
        while (pos != null) {
            GoToCommand.INSTANCE.path.add(pos);
            pos = this.prevPosMap.get(pos);
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
        if (!depthTest) {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
        }
        GL11.glDepthMask(false);

        GL11.glPushMatrix();
        GL11.glTranslated(-ReflectionStuff.getRenderPosX(Minecraft.getMinecraft().getRenderManager()), -ReflectionStuff.getRenderPosY(Minecraft.getMinecraft().getRenderManager()), -ReflectionStuff.getRenderPosZ(Minecraft.getMinecraft().getRenderManager()));
        GL11.glTranslated(0.5, 0.5, 0.5);

        if (debugMode) {
            int renderedThings = 0;

            // queue (yellow)
            GL11.glLineWidth(2);
            GL11.glColor4f(1, 1, 0, 0.75F);
            for (PathPos element : this.queue.toArray()) {
                if (renderedThings >= 5000) {
                    break;
                }

                PathRenderer.renderNode(element);
                renderedThings++;
            }

            // processed (red)
            GL11.glLineWidth(2);
            for (Entry<PathPos, PathPos> entry : this.prevPosMap.entrySet()) {
                if (renderedThings >= 5000) {
                    break;
                }

                if (entry.getKey().jumping) {
                    GL11.glColor4f(1, 0, 1, 0.75F);
                } else {
                    GL11.glColor4f(1, 0, 0, 0.75F);
                }

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
        for (int i = 0; i < GoToCommand.INSTANCE.path.size() - 1; i++) {
            PathRenderer.renderArrow(GoToCommand.INSTANCE.path.get(i), GoToCommand.INSTANCE.path.get(i + 1));
        }

        GL11.glPopMatrix();

        // GL resets
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
    }

    public PathProcessor getProcessor() {
        return new WalkPathProcessor(GoToCommand.INSTANCE.path);
    }
}
