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

package net.daporkchop.pepsimod.mixin.client.renderer.entity;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.impl.render.ESPMod;
import net.daporkchop.pepsimod.module.impl.render.HealthTagsMod;
import net.daporkchop.pepsimod.util.Friends;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.module.ESPSettings;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Render.class)
public abstract class MixinRender<T extends Entity> {
    @Shadow
    @Final
    protected RenderManager renderManager;

    @Shadow
    public FontRenderer getFontRendererFromRenderManager() {
        return null;
    }

    @Overwrite
    protected void renderLivingLabel(T entityIn, String str, double x, double y, double z, int maxDistance) {
        double d0 = entityIn.getDistanceSqToEntity(this.renderManager.renderViewEntity);

        if (d0 <= (double) (maxDistance * maxDistance)) {
            boolean flag = entityIn.isSneaking();
            float f = this.renderManager.playerViewY;
            float f1 = this.renderManager.playerViewX;
            boolean flag1 = this.renderManager.options.thirdPersonView == 2;
            float f2 = entityIn.height + 0.5F - (flag ? 0.25F : 0.0F);
            int i = "deadmau5".equals(str) ? -10 : 0;

            if (entityIn instanceof EntityLivingBase) {
                if (entityIn instanceof EntityPlayer && Friends.isFriend(entityIn.getUniqueID().toString())) {
                    str = PepsiUtils.COLOR_ESCAPE + "b" + str;
                }

                if (HealthTagsMod.INSTANCE.isEnabled) {
                    str += " ";
                    int health = (int) ((EntityLivingBase) entityIn).getHealth();
                    if (health <= 5) {
                        str += "§4";
                    } else if (health <= 10) {
                        str += "§6";
                    } else if (health <= 15) {
                        str += "§e";
                    } else {
                        str += "§a";
                    }
                    str += health;
                }
            }

            EntityRenderer.drawNameplate(this.getFontRendererFromRenderManager(), str, (float) x, (float) y + f2, (float) z, i, f, f1, flag1, flag);
        }
    }

    @Inject(method = "getTeamColor", at = @At("HEAD"), cancellable = true)
    public void pregetTeamColor(T entity, CallbackInfoReturnable<Integer> callbackInfoReturnable) {
        if (ESPMod.INSTANCE.isEnabled) {
            ESPSettings settings = PepsiMod.INSTANCE.espSettings;
            if (entity.isInvisible()) {
                if (!settings.invisible) {
                    return;
                }
            }
            if (settings.animals && entity instanceof EntityAnimal) {
                callbackInfoReturnable.setReturnValue(ESPMod.animalColor.getIntColor());
            } else if (settings.monsters && entity instanceof EntityMob) {
                callbackInfoReturnable.setReturnValue(ESPMod.monsterColor.getIntColor());
            } else if (settings.players && entity instanceof EntityPlayer) {
                if (settings.friendColors && Friends.isFriend(entity.getUniqueID().toString())) {
                    callbackInfoReturnable.setReturnValue(ESPMod.friendColor.getIntColor());
                }
                callbackInfoReturnable.setReturnValue(ESPMod.playerColor.getIntColor());
            } else if (settings.golems && entity instanceof EntityGolem) {
                callbackInfoReturnable.setReturnValue(ESPMod.golemColor.getIntColor());
            }
        }
    }
}
