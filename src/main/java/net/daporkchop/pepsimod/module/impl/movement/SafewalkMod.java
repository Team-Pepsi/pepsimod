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
import net.daporkchop.pepsimod.util.event.MoveEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.Vector3d;

public class SafewalkMod extends Module {
    public static SafewalkMod INSTANCE;
    private Vector3d vec = new Vector3d();

    {
        INSTANCE = this;
    }

    public SafewalkMod() {
        super("Safewalk");
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void tick() {

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

    @Override
    public void onPlayerMove(MoveEvent event) {
        double x = event.x;
        double y = event.y;
        double z = event.z;

        if (mc.player.onGround) {
            double increment;
            for (increment = 0.05D; x != 0.0D && this.isOffsetBBEmpty(x, -1.0D, 0.0D); ) {
                if (x < increment && x >= -increment) {
                    x = 0.0D;
                } else if (x > 0.0D) {
                    x -= increment;
                } else {
                    x += increment;
                }
            }
            for (; z != 0.0D && this.isOffsetBBEmpty(0.0D, -1.0D, z); ) {
                if (z < increment && z >= -increment) {
                    z = 0.0D;
                } else if (z > 0.0D) {
                    z -= increment;
                } else {
                    z += increment;
                }
            }
            for (; x != 0.0D && z != 0.0D && this.isOffsetBBEmpty(x, -1.0D, z); ) {
                if (x < increment && x >= -increment) {
                    x = 0.0D;
                } else if (x > 0.0D) {
                    x -= increment;
                } else {
                    x += increment;
                }
                if (z < increment && z >= -increment) {
                    z = 0.0D;
                } else if (z > 0.0D) {
                    z -= increment;
                } else {
                    z += increment;
                }
            }
        }

        event.x = x;
        event.y = y;
        event.z = z;
    }

    public boolean isOffsetBBEmpty(double offsetX, double offsetY, double offsetZ) {
        EntityPlayerSP playerSP = mc.player;
        this.vec.x = offsetX;
        this.vec.y = offsetY;
        this.vec.z = offsetZ;
        return mc.world.getCollisionBoxes(playerSP, playerSP.getEntityBoundingBox().offset(this.vec.x, this.vec.y, this.vec.z)).isEmpty();
    }
}
