/*
 * Adapted from The MIT License (MIT)
 *
 * Copyright (c) 2016-2020 DaPorkchop_
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * Any persons and/or organizations using this software must include the above copyright notice and this permission notice,
 * provide sufficient credit to the original authors of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.daporkchop.pepsimod.module.impl.movement;

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.OptionCompletions;
import net.daporkchop.pepsimod.module.api.option.ExtensionSlider;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;
import net.daporkchop.pepsimod.util.ReflectionStuff;
import net.daporkchop.pepsimod.util.config.impl.EntitySpeedTranslator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketVehicleMove;
import net.minecraft.network.play.server.SPacketMoveVehicle;
import net.minecraft.util.MovementInput;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class EntitySpeedMod extends Module {
    public static EntitySpeedMod INSTANCE;

    public static AxisAlignedBB getMergedBBs(Entity entity, AxisAlignedBB bb) {
        if (entity.world.isRemote) { //only run on client to fix stuff in single player
            for (Entity passenger : entity.getPassengers()) {
                AxisAlignedBB bb2 = passenger.getEntityBoundingBox();
                ReflectionStuff.setMaxY(bb2, passenger.getPositionEyes(0.0f).y);
                bb = bb.union(bb2);
            }
        }
        return bb;
    }

    {
        INSTANCE = this;
    }

    public float fakedStepHeight = 0.5f;
    protected int stepDelay = 0;

    public EntitySpeedMod() {
        super("EntitySpeed");
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
        this.fakedStepHeight = 0.5f;
    }

    @Override
    public void tick() {
        Entity ridingEntity = ReflectionStuff.getRidingEntity(mc.player);
        if (ridingEntity != null) {
            PIG_STEP:
            if (ridingEntity instanceof EntityPig)  {
                if (this.stepDelay > 0) {
                    this.stepDelay--;
                    return;
                }

                EntityPig pig = (EntityPig) ridingEntity;

                if (mc.player.movementInput.moveForward == 0 && mc.player.movementInput.moveStrafe == 0) {
                    pig.stepHeight = this.fakedStepHeight = 1.0f;
                    break PIG_STEP;
                } else {
                    pig.stepHeight = this.fakedStepHeight = 0.5f;
                }

                if (!pig.collidedHorizontally) {
                    break PIG_STEP;
                }

                if (!pig.onGround || pig.isOnLadder() || pig.isInWater() || pig.isInLava()) {
                    break PIG_STEP;
                }

                if (mc.player.movementInput.jump) {
                    break PIG_STEP;
                }

                double stepHeight = -1;
                Vec3d stepDir = null;
                {
                    boolean found = false;
                    Vec3d[] dirs = {
                            new Vec3d(1, 0, 0),
                            new Vec3d(-1, 0, 0),
                            new Vec3d(0, 0, 1),
                            new Vec3d(0, 0, -1)
                    };
                    YLOOP:
                    for (double d = 1.0d; d > 0.0d; d -= 0.0625d) {
                        for (Vec3d dir : dirs)  {
                            AxisAlignedBB bb = pig.getEntityBoundingBox().offset(dir.scale(0.0625d));
                            if (mc.world.getCollisionBoxes(pig, bb.offset(0, d, 0)).isEmpty()) {
                                found = true;
                                stepDir = dir;
                                for (AxisAlignedBB box : mc.world.getCollisionBoxes(pig, bb)) {
                                    if (box.maxY > stepHeight) {
                                        stepHeight = box.maxY;
                                    }
                                }
                                break YLOOP;
                            }
                        }
                    }

                    if (!found) {
                        break PIG_STEP;
                    }
                }

                stepHeight -= pig.posY;

                if (stepHeight < 0 || stepHeight > 1) {
                    break PIG_STEP;
                }

                double yOrig = pig.posY;
                mc.player.connection.sendPacket(new CPacketVehicleMove(pig));
                pig.posY = yOrig + 0.24d * stepHeight;
                mc.player.connection.sendPacket(new CPacketVehicleMove(pig));
                pig.posY = yOrig + 0.48d * stepHeight;
                mc.player.connection.sendPacket(new CPacketVehicleMove(pig));
                pig.posY = yOrig + 0.72d * stepHeight;
                mc.player.connection.sendPacket(new CPacketVehicleMove(pig));
                pig.posY = yOrig + 0.96d * stepHeight;
                mc.player.connection.sendPacket(new CPacketVehicleMove(pig));
                pig.posY = yOrig + stepHeight;
                mc.player.connection.sendPacket(new CPacketVehicleMove(pig));

                pig.setPosition(pig.posX + stepDir.x * 0.0625d, yOrig + stepHeight, pig.posZ + stepDir.z * 0.0625d);
                //System.out.println("Stepping at x=" + pig.getEntityBoundingBox().maxX);
                this.stepDelay = 5;
                return;
            }

            MovementInput movementInput = mc.player.movementInput;
            double forward = movementInput.moveForward;
            double strafe = movementInput.moveStrafe;
            float yaw = mc.player.rotationYaw;
            if ((forward == 0.0D) && (strafe == 0.0D)) {
                ridingEntity.motionX = 0.0D;
                ridingEntity.motionZ = 0.0D;
            } else {
                if (forward != 0.0D) {
                    if (strafe > 0.0D) {
                        yaw += (forward > 0.0D ? -45 : 45);
                    } else if (strafe < 0.0D) {
                        yaw += (forward > 0.0D ? 45 : -45);
                    }
                    strafe = 0.0D;
                    if (forward > 0.0D) {
                        forward = 1.0D;
                    } else if (forward < 0.0D) {
                        forward = -1.0D;
                    }
                }
                ridingEntity.motionX = (forward * EntitySpeedTranslator.INSTANCE.speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * EntitySpeedTranslator.INSTANCE.speed * Math.sin(Math.toRadians(yaw + 90.0F)));
                ridingEntity.motionZ = (forward * EntitySpeedTranslator.INSTANCE.speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * EntitySpeedTranslator.INSTANCE.speed * Math.cos(Math.toRadians(yaw + 90.0F)));
            }
        }
    }

    @Override
    public void init() {
        INSTANCE = this;
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new ModuleOption<>(EntitySpeedTranslator.INSTANCE.speed, "speed", OptionCompletions.FLOAT,
                        (value) -> {
                            EntitySpeedTranslator.INSTANCE.speed = Math.max(0, value);
                            return true;
                        },
                        () -> {
                            return EntitySpeedTranslator.INSTANCE.speed;
                        }, "Speed", new ExtensionSlider(ExtensionType.VALUE_FLOAT, 0f, 4f, 0.1f)),
                new ModuleOption<>(EntitySpeedTranslator.INSTANCE.idleSpeed, "idleSpeed", OptionCompletions.FLOAT,
                        (value) -> {
                            EntitySpeedTranslator.INSTANCE.idleSpeed = Math.max(0, value);
                            return true;
                        },
                        () -> {
                            return EntitySpeedTranslator.INSTANCE.idleSpeed;
                        }, "Idle Speed", new ExtensionSlider(ExtensionType.VALUE_FLOAT, 0f, 2f, 0.1f))
        };
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.MOVEMENT;
    }
}
