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

package net.daporkchop.pepsimod.asm.event.forge.client;

import net.daporkchop.pepsimod.util.event.EventStatus;
import net.minecraft.client.gui.GuiIngame;
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

    /**
     * Calls {@link net.daporkchop.pepsimod.util.event.EventManager#firePreRenderHUD(float, int, int)} before the Minecraft Forge event is called.
     */
    @Redirect(
            method = "Lnet/minecraftforge/client/GuiIngameForge;renderGameOverlay(F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/client/GuiIngameForge;pre(Lnet/minecraftforge/client/event/RenderGameOverlayEvent$ElementType;)Z"
            ))
    private boolean inject_preRenderHUD(GuiIngameForge gui, RenderGameOverlayEvent.ElementType type, float partialTicks) {
        return EVENT_MANAGER.firePreRenderHUD(partialTicks, RESOLUTION.width(), RESOLUTION.height()) != EventStatus.OK | this.pre(type);
    }

    /**
     * Calls {@link net.daporkchop.pepsimod.util.event.EventManager#firePostRenderHUD(float, int, int)} after the Minecraft Forge event is called.
     */
    @Redirect(
            method = "Lnet/minecraftforge/client/GuiIngameForge;renderGameOverlay(F)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/client/GuiIngameForge;post(Lnet/minecraftforge/client/event/RenderGameOverlayEvent$ElementType;)V"
            ))
    private void inject_postRenderHUD(GuiIngameForge gui, RenderGameOverlayEvent.ElementType type, float partialTicks) {
        this.post(type);
        EVENT_MANAGER.firePostRenderHUD(partialTicks, RESOLUTION.width(), RESOLUTION.height());
    }

    @Shadow
    protected abstract boolean pre(RenderGameOverlayEvent.ElementType type);

    @Shadow
    protected abstract void post(RenderGameOverlayEvent.ElementType type);
}
