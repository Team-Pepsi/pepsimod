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

package net.daporkchop.pepsimod.mixin.forge.client;

import net.daporkchop.pepsimod.util.PepsiConstants;
import net.daporkchop.pepsimod.util.event.EventStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.daporkchop.pepsimod.util.PepsiConstants.EVENT_MANAGER;
import static net.daporkchop.pepsimod.util.PepsiConstants.RESOLUTION;

/**
 * @author DaPorkchop_
 */
@Mixin(GuiIngameForge.class)
abstract class MixinGuiIngameForge extends GuiIngame {
    public MixinGuiIngameForge() {
        super(null);
    }

    @Redirect(
            method = "Lnet/minecraftforge/client/GuiIngameForge;renderGameOverlay(F)V",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/client/Minecraft;)Lnet/minecraft/client/gui/ScaledResolution;"
            ))
    private ScaledResolution preventNewScaledResolutionInstance(Minecraft mc) {
        return PepsiConstants.RESOLUTION.getAsMinecraft();
    }

    @Redirect(
            method = "Lnet/minecraftforge/client/GuiIngameForge;renderGameOverlay(F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/client/GuiIngameForge;pre(Lnet/minecraftforge/client/event/RenderGameOverlayEvent$ElementType;)Z"
            ))
    private boolean checkIfOverlayShouldBeSkipped(GuiIngameForge gui, RenderGameOverlayEvent.ElementType type, float partialTicks) {
        return EVENT_MANAGER.firePreRenderHUD(partialTicks, RESOLUTION.width(), RESOLUTION.height()) != EventStatus.OK | this.pre(type);
    }

    @Redirect(
            method = "Lnet/minecraftforge/client/GuiIngameForge;renderGameOverlay(F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/client/GuiIngameForge;post(Lnet/minecraftforge/client/event/RenderGameOverlayEvent$ElementType;)V"
            ))
    private void firePostRenderHUD(GuiIngameForge gui, RenderGameOverlayEvent.ElementType type, float partialTicks) {
        EVENT_MANAGER.firePostRenderHUD(partialTicks, RESOLUTION.width(), RESOLUTION.height());
        this.post(type);
    }

    @Shadow
    protected abstract boolean pre(RenderGameOverlayEvent.ElementType type);

    @Shadow
    protected abstract void post(RenderGameOverlayEvent.ElementType type);
}
