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

package net.daporkchop.pepsimod.module.impl.movement;

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.api.OptionCompletions;
import net.daporkchop.pepsimod.module.api.option.ExtensionSlider;
import net.daporkchop.pepsimod.module.api.option.ExtensionType;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class StepMod extends Module {
    public static StepMod INSTANCE;

    public StepMod() {
        super("Step");
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {
        if (pepsiMod.hasInitializedModules) {
            mc.player.stepHeight = 0.5F;
        }
    }

    @Override
    public void tick() {
        if (pepsiMod.miscOptions.step_legit) {
            EntityPlayerSP player = mc.player;

            player.stepHeight = 0.5f;

            if (!player.collidedHorizontally) {
                return;
            }

            if (!player.onGround || player.isOnLadder() || player.isInWater() || player.isInLava()) {
                return;
            }

            if (player.movementInput.moveForward == 0 && player.movementInput.moveStrafe == 0) {
                return;
            }

            if (player.movementInput.jump) {
                return;
            }

            AxisAlignedBB bb = player.getEntityBoundingBox().offset(0, 0.05d, 0).expand(0.05, 0.05, 0.05).expand(-0.05, -0.05, -0.05);
            if (!mc.world.getCollisionBoxes(player, bb.offset(0, 1, 0)).isEmpty()) {
                return;
            }

            double stepHeight = -1;
            List<AxisAlignedBB> bbs = mc.world.getCollisionBoxes(player, bb);
            for (AxisAlignedBB box : bbs) {
                if (box.maxY > stepHeight) {
                    stepHeight = box.maxY;
                }
            }

            stepHeight -= player.posY;

            if (stepHeight < 0 || stepHeight > 1) {
                return;
            }

            mc.player.connection.sendPacket(new CPacketPlayer.Position(player.posX, player.posY + 0.42 * stepHeight, player.posZ, player.onGround));
            mc.player.connection.sendPacket(new CPacketPlayer.Position(player.posX, player.posY + 0.753 * stepHeight, player.posZ, player.onGround));
            player.setPosition(player.posX, player.posY + 1 * stepHeight, player.posZ);
        } else {
            mc.player.stepHeight = pepsiMod.miscOptions.step_height;
        }
    }

    @Override
    public void init() {
        INSTANCE = this;
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{
                new ModuleOption<>(pepsiMod.miscOptions.step_height, "height", OptionCompletions.INTEGER,
                        (value) -> {
                            pepsiMod.miscOptions.step_height = Math.max(0, value);
                            return true;
                        },
                        () -> {
                            return pepsiMod.miscOptions.step_height;
                        }, "Height", new ExtensionSlider(ExtensionType.VALUE_INT, 1, 64, 1)),
                new ModuleOption<>(pepsiMod.miscOptions.step_legit, "legit", OptionCompletions.BOOLEAN,
                        (value) -> {
                            pepsiMod.miscOptions.step_legit = value;
                            return true;
                        },
                        () -> {
                            return pepsiMod.miscOptions.step_legit;
                        }, "Legit")
        };
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.MOVEMENT;
    }

    @Override
    public boolean hasModeInName() {
        return true;
    }

    @Override
    public String getModeForName() {
        if (pepsiMod.miscOptions.step_legit) {
            return "Legit";
        } else {
            return String.valueOf(pepsiMod.miscOptions.step_height);
        }
    }
}
