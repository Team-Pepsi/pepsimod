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

package net.daporkchop.pepsimod.mixin.client.renderer.entity;

import net.daporkchop.pepsimod.misc.data.DataLoader;
import net.daporkchop.pepsimod.misc.data.Group;
import net.daporkchop.pepsimod.module.impl.render.ESPMod;
import net.daporkchop.pepsimod.module.impl.render.HealthTagsMod;
import net.daporkchop.pepsimod.module.impl.render.NameTagsMod;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.RenderColor;
import net.daporkchop.pepsimod.util.config.impl.ESPTranslator;
import net.daporkchop.pepsimod.util.config.impl.FriendsTranslator;
import net.daporkchop.pepsimod.util.config.impl.NameTagsTranslator;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import static net.daporkchop.pepsimod.util.PepsiConstants.pepsimod;

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
        double d0 = entityIn.getDistanceSq(this.renderManager.renderViewEntity);

        if (d0 <= (double) (maxDistance * maxDistance)) {
            boolean flag = entityIn.isSneaking();
            float f = this.renderManager.playerViewY;
            float f1 = this.renderManager.playerViewX;
            boolean flag1 = this.renderManager.options.thirdPersonView == 2;
            float f2 = entityIn.height + 0.5F - (flag ? 0.25F : 0.0F);
            int i = "deadmau5".equals(str) ? -10 : 0;

            if (entityIn instanceof EntityLivingBase) {
                if (entityIn instanceof EntityPlayer && FriendsTranslator.INSTANCE.isFriend(entityIn)) {
                    str = PepsiUtils.COLOR_ESCAPE + "b" + str;
                }

                if (HealthTagsMod.INSTANCE.state.enabled) {
                    str += " ";
                    int health = (int) ((EntityLivingBase) entityIn).getHealth();
                    if (health <= 5) {
                        str += "\u00A74";
                    } else if (health <= 10) {
                        str += "\u00A76";
                    } else if (health <= 15) {
                        str += "\u00A7e";
                    } else {
                        str += "\u00A7a";
                    }
                    str += health;
                }
            }

            if (NameTagsMod.INSTANCE.state.enabled) {
                PepsiUtils.drawNameplateNoScale(this.getFontRendererFromRenderManager(), str, (float) x, (float) y, (float) z, i, f, f1, flag1, f2, NameTagsTranslator.INSTANCE.scale);
            } else {
                EntityRenderer.drawNameplate(this.getFontRendererFromRenderManager(), str, (float) x, (float) y + f2, (float) z, i, f, f1, flag1, flag);
            }
        }
    }

    @ModifyConstant(
            method = "Lnet/minecraft/client/renderer/entity/Render;getTeamColor(Lnet/minecraft/entity/Entity;)I",
            constant = @Constant(
                    intValue = 16777215 // 0xFFFFFF
            ))
    public int changeDefaultTeamColor(int old, Entity entity) {
        if (entity instanceof EntityPlayer) {
            Group group = pepsimod.data.getGroup((EntityPlayer) entity);
            if (group != null && group.color != 0) {
                return group.color;
            }
        }
        if (ESPMod.INSTANCE.state.enabled && !ESPTranslator.INSTANCE.box) {
            RenderColor color = ESPMod.INSTANCE.chooseColor(entity);
            if (color != null) {
                return color.getIntColor();
            }
        }
        return old;
    }
}
