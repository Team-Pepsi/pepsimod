/*
 * Adapted from The MIT License (MIT)
 *
 * Copyright (c) 2016-2021 DaPorkchop_
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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Render.class)
public abstract class MixinRender<T extends Entity> {
    @Redirect(method = "Lnet/minecraft/client/renderer/entity/Render;renderLivingLabel(Lnet/minecraft/entity/Entity;Ljava/lang/String;DDDI)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;getDistanceSq(Lnet/minecraft/entity/Entity;)D"))
    private double pepsimod_renderLivingLabel_alwaysDrawNameplate(Entity entity, Entity otherEntity) {
        return NameTagsMod.INSTANCE.state.enabled ? Double.NEGATIVE_INFINITY : entity.getDistanceSq(otherEntity);
    }

    @Redirect(method = "Lnet/minecraft/client/renderer/entity/Render;renderLivingLabel(Lnet/minecraft/entity/Entity;Ljava/lang/String;DDDI)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/EntityRenderer;drawNameplate(Lnet/minecraft/client/gui/FontRenderer;Ljava/lang/String;FFFIFFZZ)V"))
    private void pepsimod_renderLivingLabel_redirectDrawNameplate(
            FontRenderer fontRenderer, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, boolean isSneaking,
            Entity entityIn, String _strAgain, double _x, double _y, double _z, int maxDistance) {
        if (entityIn instanceof EntityLivingBase) {
            if (entityIn instanceof EntityPlayer && FriendsTranslator.INSTANCE.isFriend(entityIn)) {
                str = PepsiUtils.COLOR_ESCAPE + "b" + str;
            }

            if (HealthTagsMod.INSTANCE.state.enabled) {
                float health = ((EntityLivingBase) entityIn).getHealth();
                float relativeHealth = health / ((EntityLivingBase) entityIn).getMaxHealth();
                char colorCode = '7';
                if (relativeHealth >= 0.0f && relativeHealth <= 1.0f) {
                    if (relativeHealth <= 0.25f) {
                        colorCode = '4';
                    } else if (relativeHealth <= 0.5f) {
                        colorCode = '6';
                    } else if (relativeHealth <= 0.75f) {
                        colorCode = 'e';
                    } else {
                        colorCode = 'a';
                    }
                }
                str += " " + PepsiUtils.COLOR_ESCAPE + colorCode + (int) health;
            }
        }

        if (NameTagsMod.INSTANCE.state.enabled) {
            float offset = entityIn.height + 0.5F - (entityIn.isSneaking() ? 0.25F : 0.0F);
            PepsiUtils.drawNameplateNoScale(fontRenderer, str, x, y - offset, z, verticalShift, viewerYaw, viewerPitch, isThirdPersonFrontal, offset, NameTagsTranslator.INSTANCE.scale);
        } else {
            EntityRenderer.drawNameplate(fontRenderer, str, x, y, z, verticalShift, viewerYaw, viewerPitch, isThirdPersonFrontal, isSneaking);
        }
    }

    @Inject(
            method = "Lnet/minecraft/client/renderer/entity/Render;getTeamColor(Lnet/minecraft/entity/Entity;)I",
            at = @At("HEAD"),
            cancellable = true)
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
