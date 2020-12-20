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

package net.daporkchop.pepsimod.asm.core.client.renderer.entity;

import net.daporkchop.pepsimod.module.impl.render.ESPMod;
import net.daporkchop.pepsimod.module.impl.render.HealthTagsMod;
import net.daporkchop.pepsimod.module.impl.render.NameTagsMod;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.RenderColor;
import net.daporkchop.pepsimod.util.config.impl.ESPTranslator;
import net.daporkchop.pepsimod.util.config.impl.FriendsTranslator;
import net.daporkchop.pepsimod.util.config.impl.NameTagsTranslator;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
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

    @Inject(
            method = "Lnet/minecraft/client/renderer/entity/Render;getTeamColor(Lnet/minecraft/entity/Entity;)I",
            at = @At("HEAD"))
    public void changeDefaultTeamColor(Entity entity, CallbackInfoReturnable<Integer> ci) {
        if (ESPMod.INSTANCE.state.enabled && !ESPTranslator.INSTANCE.box) {
            RenderColor color = ESPMod.INSTANCE.chooseColor(entity);
            if (color != null) {
                ci.setReturnValue(color.getIntColor());
                ci.cancel();
            }
        }
    }
}
