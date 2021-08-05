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

import net.daporkchop.pepsimod.module.impl.render.NameTagsMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author DaPorkchop_
 */
@Mixin(RenderLivingBase.class)
public abstract class MixinRenderLivingBase<T extends Entity> extends Render<T> {
    protected MixinRenderLivingBase() {
        super(null);
    }

    @Redirect(method = "Lnet/minecraft/client/renderer/entity/RenderLivingBase;renderName(Lnet/minecraft/entity/EntityLivingBase;DDD)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/entity/EntityLivingBase;getDistanceSq(Lnet/minecraft/entity/Entity;)D"))
    private double pepsimod_renderLivingLabel_alwaysRenderName(EntityLivingBase entity, Entity otherEntity) {
        return NameTagsMod.INSTANCE.state.enabled ? Double.NEGATIVE_INFINITY : entity.getDistanceSq(otherEntity);
    }

    @Inject(method = "Lnet/minecraft/client/renderer/entity/RenderLivingBase;canRenderName(Lnet/minecraft/entity/EntityLivingBase;)Z",
            at = @At("HEAD"),
            cancellable = true)
    private void pepsimod_canRenderName_alwaysRenderName(EntityLivingBase entity, CallbackInfoReturnable<Boolean> ci) {
        if (NameTagsMod.INSTANCE.state.enabled) {
            ci.setReturnValue(Minecraft.isGuiEnabled() && entity != this.renderManager.renderViewEntity && !entity.isBeingRidden());
        }
    }
}
