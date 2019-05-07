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

package net.daporkchop.pepsimod.mixin.client.entity;

import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.impl.misc.FreecamMod;
import net.daporkchop.pepsimod.module.impl.misc.HUDMod;
import net.daporkchop.pepsimod.module.impl.movement.FlightMod;
import net.daporkchop.pepsimod.module.impl.movement.NoSlowdownMod;
import net.daporkchop.pepsimod.module.impl.movement.StepMod;
import net.daporkchop.pepsimod.the.wurst.pkg.name.RotationUtils;
import net.daporkchop.pepsimod.util.event.MoveEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
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

    public MoveEvent event = new MoveEvent();

    public MixinEntityPlayerSP() {
        super(null, null);
    }

    @Overwrite
    public void move(MoverType type, double x, double y, double z) {
        this.event.x = x;
        this.event.y = y;
        this.event.z = z;
        for (Module module : ModuleManager.ENABLED_MODULES) {
            module.onPlayerMove(this.event);
        }
        super.move(type, this.event.x, this.event.y, this.event.z);
    }

    @Redirect(
            method = "Lnet/minecraft/client/entity/EntityPlayerSP;onLivingUpdate()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;isRiding()Z"
            ))
    public boolean fixNoSlowdown(Entity entity) {
        return entity.isRiding() && NoSlowdownMod.INSTANCE.state.enabled;
    }

    @Shadow
    protected boolean isCurrentViewEntity() {
        return false;
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
    public void fixPortalGUIs_1(EntityPlayerSP player)  {
    }

    @Redirect(
            method = "Lnet/minecraft/client/entity/EntityPlayerSP;onLivingUpdate()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V",
                    ordinal = 0
            ))
    public void fixPortalGUIs_2(Minecraft mc, GuiScreen screen)  {
    }
}
