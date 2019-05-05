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

package net.daporkchop.pepsimod.module.impl.movement;

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.util.ReflectionStuff;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

public class JesusMod extends Module {
    public static JesusMod INSTANCE;
    private int tickTimer = 10;
    private int packetTimer = 0;

    {
        INSTANCE = this;
    }

    public JesusMod() {
        super("Jesus");
    }

    public boolean isOverLiquid() {
        if (mc.player == null) {
            return false;
        }

        Entity entity = mc.player.isRiding() ? mc.player.getRidingEntity() : mc.player;

        boolean foundLiquid = false;
        boolean foundSolid = false;

        // check collision boxes below player
        for (AxisAlignedBB bb : mc.world.getCollisionBoxes(entity, entity.getEntityBoundingBox().offset(0, -0.5, 0))) {
            BlockPos pos = new BlockPos(bb.getCenter());
            IBlockState state = mc.world.getBlockState(pos);
            Material material = state.getBlock().getMaterial(state);

            if (material == Material.WATER || material == Material.LAVA) {
                foundLiquid = true;
            } else if (material != Material.AIR) {
                foundSolid = true;
            }
        }

        return foundLiquid && !foundSolid;
    }

    public boolean shouldBeSolid() {
        if (mc.player == null) {
            return false;
        }

        Entity entity = mc.player.isRiding() ? mc.player.getRidingEntity() : mc.player;

        return this.state.enabled && entity.fallDistance <= 3 && !mc.gameSettings.keyBindSneak.isPressed() && !entity.isInWater();
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void tick() {
        if (mc.player == null) {
            return;
        }

        Entity entity = mc.player.isRiding() ? mc.player.getRidingEntity() : mc.player;

        // check if sneaking
        if (mc.gameSettings.keyBindSneak.isPressed()) {
            return;
        }

        // move up in water
        if (entity.isInWater()) {
            entity.motionY = 0.11;
            this.tickTimer = 0;
            return;
        }

        // simulate jumping out of water
        if (this.tickTimer == 0) {
            entity.motionY = 0.30;
        } else if (this.tickTimer == 1) {
            entity.motionY = 0;
        }

        // update timer
        this.tickTimer++;
    }

    @Override
    public boolean preSendPacket(Packet<?> packet) {
        if (mc.player == null) {
            return false;
        }

        Entity entity = mc.player.isRiding() ? mc.player.getRidingEntity() : mc.player;

        RETURN:
        if (!mc.player.isRiding() && packet instanceof CPacketPlayer) {
            // check if packet contains a position
            if (!(packet instanceof CPacketPlayer.Position || packet instanceof CPacketPlayer.PositionRotation)) {
                break RETURN;
            }

            // check inWater
            if (entity.isInWater()) {
                break RETURN;
            }

            // check fall distance
            if (entity.fallDistance > 3F) {
                break RETURN;
            }

            if (!this.isOverLiquid()) {
                break RETURN;
            }

            // if not actually moving, cancel packet
            if (mc.player.movementInput == null) {
                return true;
            }

            // wait for timer
            this.packetTimer++;
            if (this.packetTimer < 4) {
                break RETURN;
            }

            CPacketPlayer pck = (CPacketPlayer) packet;

            // get position
            double y = pck.getY(0);

            // offset y
            if (entity.ticksExisted % 2 == 0) {
                y -= 0.05;
            } else {
                y += 0.05;
            }

            ReflectionStuff.setCPacketPlayer_y(pck, y);
            ReflectionStuff.setcPacketPlayer_onGround(pck, true);
        } else if (mc.player.isRiding() && packet instanceof CPacketVehicleMove) {
            // check inWater
            if (entity.isInWater()) {
                break RETURN;
            }

            // check fall distance
            if (entity.fallDistance > 3F) {
                break RETURN;
            }

            if (!this.isOverLiquid()) {
                break RETURN;
            }

            // if not actually moving, cancel packet
            if (mc.player.movementInput == null) {
                return true;
            }

            // wait for timer
            this.packetTimer++;
            if (this.packetTimer < 4) {
                break RETURN;
            }

            CPacketVehicleMove pck = (CPacketVehicleMove) packet;

            // get position
            double y = pck.getY();

            // offset y
            if (entity.ticksExisted % 2 == 0) {
                y -= 0.05;
            } else {
                y += 0.05;
            }

            ReflectionStuff.setcPacketVehicleMove_y(pck, y);
        }

        return false;
    }

    @Override
    public void init() {
        INSTANCE = this;
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[0];
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.MOVEMENT;
    }
}
