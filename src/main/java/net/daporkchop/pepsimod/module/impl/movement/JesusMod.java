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

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.module.impl.misc.FreecamMod;
import net.daporkchop.pepsimod.util.ReflectionStuff;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class JesusMod extends Module {
    public static JesusMod INSTANCE;

    public JesusMod(boolean isEnabled, int key, boolean hide) {
        super(isEnabled, "Jesus", key, hide);
    }

    private static boolean isAboveLand(Entity entity) {
        if (entity == null) return false;

        double y = entity.posY - 0.01;

        for (int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); x++) {
            for (int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); z++) {
                BlockPos pos = new BlockPos(x, MathHelper.floor(y), z);

                if (PepsiMod.INSTANCE.mc.world.getBlockState(pos).getBlock().isFullBlock(PepsiMod.INSTANCE.mc.world.getBlockState(pos))) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean isAboveWater(Entity entity) {
        double y = entity.posY - 0.03;

        for (int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); x++) {
            for (int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); z++) {
                BlockPos pos = new BlockPos(x, MathHelper.floor(y), z);

                if (PepsiMod.INSTANCE.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean isInWater(Entity entity) {
        if (entity == null) return false;

        double y = entity.posY + 0.01;

        for (int x = MathHelper.floor(entity.posX); x < MathHelper.ceil(entity.posX); x++) {
            for (int z = MathHelper.floor(entity.posZ); z < MathHelper.ceil(entity.posZ); z++) {
                BlockPos pos = new BlockPos(x, (int) y, z);

                if (PepsiMod.INSTANCE.mc.world.getBlockState(pos).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void tick() {
        if (!FreecamMod.INSTANCE.isEnabled) {
            if (isInWater(PepsiMod.INSTANCE.mc.player) && !PepsiMod.INSTANCE.mc.player.isSneaking()) {
                PepsiMod.INSTANCE.mc.player.motionY = 0.1f;

                if (PepsiMod.INSTANCE.mc.player.getRidingEntity() != null) {
                    PepsiMod.INSTANCE.mc.player.getRidingEntity().motionY = 0.2f;
                }
            }
        }
    }

    @Override
    public boolean preSendPacket(Packet<?> packetIn) {
        if (packetIn instanceof CPacketPlayer) {
            if (isAboveWater(PepsiMod.INSTANCE.mc.player) && !isInWater(PepsiMod.INSTANCE.mc.player) && !isAboveLand(PepsiMod.INSTANCE.mc.player)) {
                int ticks = PepsiMod.INSTANCE.mc.player.ticksExisted % 2;

                if (ticks == 0) {
                    ReflectionStuff.setCPacketPlayer_y((CPacketPlayer) packetIn, ReflectionStuff.getCPacketPlayer_y((CPacketPlayer) packetIn) + 0.2f);
                }
            }
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
