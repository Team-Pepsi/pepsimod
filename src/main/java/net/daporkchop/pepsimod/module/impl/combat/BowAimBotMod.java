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

package net.daporkchop.pepsimod.module.impl.combat;

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.the.wurst.pkg.name.RenderUtils;
import net.daporkchop.pepsimod.the.wurst.pkg.name.RotationUtils;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.ReflectionStuff;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemBow;
import net.minecraft.util.math.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

public class BowAimBotMod extends Module {
    public static BowAimBotMod INSTANCE;
    public EntityLivingBase target;
    public float velocity;

    {
        INSTANCE = this;
    }

    public BowAimBotMod() {
        super("BowAimBot");
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
    public void onRenderGUI(float partialTicks, int width, int height, GuiIngame gui) {
        if (this.velocity != -1.0f) {
            if (this.velocity > 0.0f) {
                gui.drawCenteredString(mc.fontRenderer, "Ready!", width / 2, height / 2 - 20, 16777215);
            } else {
                gui.drawCenteredString(mc.fontRenderer, "Charging...", width / 2, height / 2 - 20, 16740352);
            }
        }
    }

    @Override
    public void onRender(float partialTicks) {
        if (this.target != null) {
            double[] pos = PepsiUtils.interpolate(this.target);
            double x = pos[0] - ReflectionStuff.getRenderPosX();
            double y = pos[1] - ReflectionStuff.getRenderPosY();
            double z = pos[2] - ReflectionStuff.getRenderPosZ();

            GL11.glPushMatrix();
            GL11.glTranslated(x, y, z);
            GL11.glRotatef(-this.target.rotationYaw, 0.0F, 1.0F, 0.0F);
            PepsiUtils.glColor(Color.RED);
            RenderUtils.drawOutlinedBox(new AxisAlignedBB(this.target.width / 2.0D, 0.0D, -(this.target.width / 2.0D), -this.target.width / 2.0D, this.target.height + 0.1D, this.target.width / 2.0D));
            GL11.glPopMatrix();
        }

        this.target = null;
        if (mc.player.inventory.getCurrentItem() != null) {
            if (mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow &&
                    mc.gameSettings.keyBindUseItem.isKeyDown()) {
                this.target = PepsiUtils.getClosestEntityWithoutReachFactor();
                this.aimAtTarget();
                return;
            }
        }
        this.velocity = -1.0F;
    }

    private void aimAtTarget() {
        if (this.target == null) {
            return;
        }
        this.velocity = ((72000 - mc.player.getItemInUseCount()) / 20.0F);
        this.velocity = ((this.velocity * this.velocity + this.velocity * 2.0F) / 3.0F);
        if (this.velocity > 1.0F) {
            this.velocity = 1.0F;
        }
        if (this.velocity < 0.1D) {
            if ((this.target instanceof EntityLivingBase)) {
                RotationUtils.faceEntityClient(this.target);
                RotationUtils.faceEntityPacket(this.target);
            }
            return;
        }
        if (this.velocity > 1.0F) {
            this.velocity = 1.0F;
        }
        double posX = this.target.posX - mc.player.posX;
        double posY = this.target.posY + this.target.getEyeHeight() - 0.15D -
                mc.player.posY -
                mc.player.getEyeHeight();
        double posZ = this.target.posZ - mc.player.posZ;

        float yaw = (float) (Math.atan2(posZ, posX) * 180.0D / 3.141592653589793D) - 90.0F;
        double y2 = Math.sqrt(posX * posX + posZ * posZ);
        float g = 0.006F;
        float tmp = (float) (this.velocity * this.velocity * this.velocity * this.velocity -
                g * (g * (y2 * y2) + 2.0D * posY * (this.velocity * this.velocity)));
        float pitch = (float) -Math.toDegrees(
                Math.atan((this.velocity * this.velocity - Math.sqrt(tmp)) / (g * y2)));
        mc.player.rotationYaw = yaw;
        mc.player.rotationPitch = pitch;
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
        return ModuleCategory.COMBAT;
    }
}
