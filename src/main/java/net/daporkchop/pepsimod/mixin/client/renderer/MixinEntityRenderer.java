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

package net.daporkchop.pepsimod.mixin.client.renderer;

import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.impl.render.AntiBlindMod;
import net.daporkchop.pepsimod.module.impl.render.AntiTotemAnimationMod;
import net.daporkchop.pepsimod.module.impl.render.FullbrightMod;
import net.daporkchop.pepsimod.module.impl.render.NoHurtCamMod;
import net.daporkchop.pepsimod.module.impl.render.NoOverlayMod;
import net.daporkchop.pepsimod.the.wurst.pkg.name.RotationUtils;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.render.WorldRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.util.glu.Project;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {
    @Shadow
    protected abstract void setupCameraTransform(float partialTicks, int pass);

    @Shadow
    @Final
    private Minecraft mc;

    @Inject(
            method = "Lnet/minecraft/client/renderer/EntityRenderer;renderWorldPass(IFJ)V",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/profiler/Profiler;endStartSection(Ljava/lang/String;)V",
                    ordinal = 19
            ))
    public void preRenderHand(CallbackInfo ci, int pass, float partialTicks, long finishTimeNano) {
        PepsiUtils.toRemoveWurstRenderListeners.forEach(PepsiUtils.wurstRenderListeners::remove);
        PepsiUtils.toRemoveWurstRenderListeners.clear();
        PepsiUtils.wurstRenderListeners.forEach(listener -> listener.render(partialTicks));
    }

    @Inject(
            method = "Lnet/minecraft/client/renderer/EntityRenderer;renderWorldPass(IFJ)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/renderer/EntityRenderer;renderHand:Z",
                    shift = At.Shift.BEFORE
            ))
    public void renderLines(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci) {
        if (this.mc.gameSettings.viewBobbing) {
            this.mc.gameSettings.viewBobbing = false;

            glPushMatrix();
            this.setupCameraTransform(partialTicks, pass);
            try (WorldRenderer renderer = new WorldRenderer(RotationUtils.getClientLookVec(), PepsiUtils.getPlayerPos(partialTicks), partialTicks)) {
                for (Module module : ModuleManager.ENABLED_MODULES) {
                    module.renderOverlay(renderer);
                }
            }
            glPopMatrix();

            this.mc.gameSettings.viewBobbing = true;

            glPushMatrix();
            this.setupCameraTransform(partialTicks, pass);
            try (WorldRenderer renderer = new WorldRenderer(RotationUtils.getClientLookVec(), PepsiUtils.getPlayerPos(partialTicks), partialTicks)) {
                for (Module module : ModuleManager.ENABLED_MODULES) {
                    module.renderWorld(renderer);
                }
            }
            glPopMatrix();
        } else {
            try (WorldRenderer renderer = new WorldRenderer(RotationUtils.getClientLookVec(), PepsiUtils.getPlayerPos(partialTicks), partialTicks)) {
                for (Module module : ModuleManager.ENABLED_MODULES) {
                    module.renderOverlay(renderer);
                    module.renderWorld(renderer);
                }
            }
        }
    }

    @ModifyConstant(
            method = "Lnet/minecraft/client/renderer/EntityRenderer;setupCameraTransform(FI)V",
            constant = {
                    @Constant(intValue = 20),
                    @Constant(intValue = 7)
            })
    public int preventNauseaEffect(int orig)    {
        return AntiBlindMod.INSTANCE.state.enabled ? 0 : orig;
    }

    @Inject(
            method = "Lnet/minecraft/client/renderer/EntityRenderer;hurtCameraEffect(F)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void preHurtCameraEffect(float partialTicks, CallbackInfo callbackInfo) {
        if (NoHurtCamMod.INSTANCE.state.enabled) {
            callbackInfo.cancel();
        }
    }

    @Redirect(
            method = "Lnet/minecraft/client/renderer/EntityRenderer;setupFog(IF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/GlStateManager;setFogDensity(F)V"
            ))
    public void changeFog(float density) {
        if (NoOverlayMod.INSTANCE.state.enabled) {
            GlStateManager.setFogDensity(0.01f);
        } else {
            GlStateManager.setFogDensity(density);
        }
    }

    @Inject(
            method = "Lnet/minecraft/client/renderer/EntityRenderer;displayItemActivation(Lnet/minecraft/item/ItemStack;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void preDisplayItemActivation(ItemStack stack, CallbackInfo callbackInfo) {
        if (AntiTotemAnimationMod.INSTANCE.state.enabled) {
            callbackInfo.cancel();
        }
    }

    @Inject(
            method = "Lnet/minecraft/client/renderer/EntityRenderer;renderItemActivation(IIF)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void preRenderItemActivation(int a, int b, float c, CallbackInfo callbackInfo) {
        if (AntiTotemAnimationMod.INSTANCE.state.enabled) {
            callbackInfo.cancel();
        }
    }

    @Redirect(
            method = "Lnet/minecraft/client/renderer/EntityRenderer;updateLightmap(F)V",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/client/settings/GameSettings;gammaSetting:F"
            ))
    public float redirectGammaSetting(GameSettings settings) {
        return Math.max(FullbrightMod.INSTANCE.level * 0.5f, settings.gammaSetting);
    }

    @Redirect(
            method = "Lnet/minecraft/client/renderer/EntityRenderer;getMouseOver(F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;getEntityBoundingBox()Lnet/minecraft/util/math/AxisAlignedBB;",
                    ordinal = 1
            ))
    public AxisAlignedBB preventMousingOverRiddenEntity(Entity possiblyRidden) {
        if (possiblyRidden.isPassenger(this.mc.getRenderViewEntity())) {
            return new AxisAlignedBB(0, 0, 0, 0, 0, 0);
        } else {
            return possiblyRidden.getEntityBoundingBox();
        }
    }
}
