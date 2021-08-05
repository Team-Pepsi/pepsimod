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

package net.daporkchop.pepsimod.asm.core.client.entity;

import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.impl.misc.HUDMod;
import net.daporkchop.pepsimod.module.impl.movement.NoSlowdownMod;
import net.daporkchop.pepsimod.module.impl.movement.StepMod;
import net.daporkchop.pepsimod.util.event.MoveEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {
    @Shadow
    @Final
    public NetHandlerPlayClient connection;
    @Shadow
    protected Minecraft mc;

    @Unique
    private MoveEvent event = new MoveEvent();

    public MixinEntityPlayerSP() {
        super(null, null);
    }

    @Redirect(method = "Lnet/minecraft/client/entity/EntityPlayerSP;move(Lnet/minecraft/entity/MoverType;DDD)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/AbstractClientPlayer;move(Lnet/minecraft/entity/MoverType;DDD)V"))
    private void pepsimod_move_fireOnPlayerMoveModuleEvent(AbstractClientPlayer entity, MoverType type, double x, double y, double z) {
        this.event.x = x;
        this.event.y = y;
        this.event.z = z;
        for (Module module : ModuleManager.ENABLED_MODULES) {
            module.onPlayerMove(this.event);
        }
        super.move(type, this.event.x, this.event.y, this.event.z);
    }

    @Redirect(method = "Lnet/minecraft/client/entity/EntityPlayerSP;onLivingUpdate()V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/EntityPlayerSP;isRiding()Z"))
    public boolean fixNoSlowdown(EntityPlayerSP entity) {
        return entity.isRiding() && NoSlowdownMod.INSTANCE.state.enabled;
    }

    @Inject(
            method = "Lnet/minecraft/client/entity/EntityPlayerSP;isAutoJumpEnabled()Z",
            at = @At("HEAD"),
            cancellable = true
    )
    public void preisAutoJumpEnabled(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (StepMod.INSTANCE.state.enabled) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }

    @Inject(
            method = "Lnet/minecraft/client/entity/EntityPlayerSP;setServerBrand(Ljava/lang/String;)V",
            at = @At("HEAD")
    )
    public void setHUDBrand(String brand, CallbackInfo callbackInfo) {
        HUDMod.INSTANCE.serverBrand = brand;
    }

    //pepsimod: prevent guis from being impossible to open while in a portal
    @Redirect(
            method = "Lnet/minecraft/client/entity/EntityPlayerSP;onLivingUpdate()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/entity/EntityPlayerSP;closeScreen()V",
                    ordinal = 0
            ))
    public void fixPortalGUIs_1(EntityPlayerSP player) {
    }

    @Redirect(
            method = "Lnet/minecraft/client/entity/EntityPlayerSP;onLivingUpdate()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V",
                    ordinal = 0
            ))
    public void fixPortalGUIs_2(Minecraft mc, GuiScreen screen) {
    }
}
