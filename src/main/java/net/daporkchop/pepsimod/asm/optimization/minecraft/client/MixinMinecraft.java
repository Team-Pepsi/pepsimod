/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2016-2019 DaPorkchop_
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

package net.daporkchop.pepsimod.asm.optimization.minecraft.client;

import net.daporkchop.pepsimod.util.render.BetterScaledResolution;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.daporkchop.pepsimod.util.PepsiConstants.RESOLUTION;

/**
 * @author DaPorkchop_
 */
@Mixin(Minecraft.class)
abstract class MixinMinecraft {
    /**
     * Prevents creation of a new {@link ScaledResolution} instance.
     */
    @Redirect(
            method = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/client/Minecraft;)Lnet/minecraft/client/gui/ScaledResolution;"
            ))
    private ScaledResolution noScaledResolutionInstances_displayGuiScreen(Minecraft mc)  {
        return RESOLUTION.getAsMinecraft();
    }

    /**
     * Prevents creation of a new {@link ScaledResolution} instance.
     */
    @Redirect(
            method = "Lnet/minecraft/client/Minecraft;resize(II)V",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/client/Minecraft;)Lnet/minecraft/client/gui/ScaledResolution;"
            ))
    private ScaledResolution noScaledResolutionInstances_resize(Minecraft mc)  {
        return RESOLUTION == BetterScaledResolution.NOOP ? new ScaledResolution(mc) : RESOLUTION.updateChained().getAsMinecraft(); //don't create new instance if pepsimod isn't initialized yet
    }

    /**
     * Prevents creation of a new {@link ScaledResolution} instance.
     */
    @Redirect(
            method = "Lnet/minecraft/client/Minecraft;runGameLoop()V",
            at = @At(
                    value = "NEW",
                    target = "(Lnet/minecraft/client/Minecraft;)Lnet/minecraft/client/gui/ScaledResolution;"
            ))
    private ScaledResolution noScaledResolutionInstances_runGameLoop(Minecraft mc)  {
        return RESOLUTION.getAsMinecraft();
    }
}
