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

package net.daporkchop.pepsimod.module.impl.render;

import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.totally.not.skidded.RenderUtils;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.ReflectionStuff;
import net.daporkchop.pepsimod.util.RenderColor;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class TrajectoriesMod extends Module {
    public static final RenderColor lineColor = new RenderColor(51, 196, 191, 128);

    public TrajectoriesMod() {
        super("Trajectories");
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

    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[]{

        };
    }

    @Override
    public void onRender(float partialTicks) {
        EntityPlayerSP player = mc.player;

        ItemStack stack = player.inventory.getCurrentItem();
        if (stack == null) {
            return;
        }

        if (!PepsiUtils.isThrowable(stack)) {
            return;
        }

        boolean usingBow = stack.getItem() instanceof ItemBow;

        // calculate starting position
        double arrowPosX = player.lastTickPosX + (player.posX - player.lastTickPosX) * ReflectionStuff.getTimer().renderPartialTicks - Math.cos((float) Math.toRadians(player.rotationYaw)) * 0.16F;
        double arrowPosY = player.lastTickPosY + (player.posY - player.lastTickPosY) * ReflectionStuff.getTimer().renderPartialTicks + player.getEyeHeight() - 0.1;
        double arrowPosZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * ReflectionStuff.getTimer().renderPartialTicks - Math.sin((float) Math.toRadians(player.rotationYaw)) * 0.16F;

        // calculate starting motion
        float arrowMotionFactor = usingBow ? 1F : 0.4F;
        float yaw = (float) Math.toRadians(player.rotationYaw);
        float pitch = (float) Math.toRadians(player.rotationPitch);
        float arrowMotionX = (float) (-Math.sin(yaw) * Math.cos(pitch) * arrowMotionFactor);
        float arrowMotionY = (float) (-Math.sin(pitch) * arrowMotionFactor);
        float arrowMotionZ = (float) (Math.cos(yaw) * Math.cos(pitch) * arrowMotionFactor);
        double arrowMotion = Math.sqrt(arrowMotionX * arrowMotionX + arrowMotionY * arrowMotionY + arrowMotionZ * arrowMotionZ);
        arrowMotionX /= arrowMotion;
        arrowMotionY /= arrowMotion;
        arrowMotionZ /= arrowMotion;
        if (usingBow) {
            float bowPower = (72000 - player.getItemInUseCount()) / 20F;
            bowPower = (bowPower * bowPower + bowPower * 2F) / 3F;

            if (bowPower > 1F || bowPower <= 0.1F) {
                bowPower = 1F;
            }

            bowPower *= 3F;
            arrowMotionX *= bowPower;
            arrowMotionY *= bowPower;
            arrowMotionZ *= bowPower;
        } else {
            arrowMotionX *= 1.5D;
            arrowMotionY *= 1.5D;
            arrowMotionZ *= 1.5D;
        }

        // GL settings
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(2);

        RenderManager renderManager = mc.getRenderManager();

        // draw trajectory line
        double gravity = usingBow ? 0.05D : stack.getItem() instanceof ItemPotion ? 0.4D : stack.getItem() instanceof ItemFishingRod ? 0.15D : 0.03D;
        Vec3d playerVector = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        PepsiUtils.glColor(lineColor);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        for (int i = 0; i < 1000; i++) {
            GL11.glVertex3d(arrowPosX - ReflectionStuff.getRenderPosX(renderManager), arrowPosY - ReflectionStuff.getRenderPosY(renderManager), arrowPosZ - ReflectionStuff.getRenderPosZ(renderManager));

            arrowPosX += arrowMotionX * 0.1;
            arrowPosY += arrowMotionY * 0.1;
            arrowPosZ += arrowMotionZ * 0.1;
            arrowMotionX *= 0.999D;
            arrowMotionY *= 0.999D;
            arrowMotionZ *= 0.999D;
            arrowMotionY -= gravity * 0.1;

            if (mc.world.rayTraceBlocks(playerVector, new Vec3d(arrowPosX, arrowPosY, arrowPosZ)) != null) {
                break;
            }
        }
        GL11.glEnd();

        // draw end of trajectory line
        double renderX = arrowPosX - ReflectionStuff.getRenderPosX(renderManager);
        double renderY = arrowPosY - ReflectionStuff.getRenderPosY(renderManager);
        double renderZ = arrowPosZ - ReflectionStuff.getRenderPosZ(renderManager);

        GL11.glPushMatrix();
        GL11.glTranslated(renderX - 0.5, renderY - 0.5, renderZ - 0.5);

        GL11.glColor4f(0F, 1F, 0F, 0.25F);
        RenderUtils.drawSolidBox();
        GL11.glColor4f(0, 1, 0, 0.75F);
        RenderUtils.drawOutlinedBox();

        GL11.glPopMatrix();

        // GL resets
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glPopMatrix();
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.RENDER;
    }
}
