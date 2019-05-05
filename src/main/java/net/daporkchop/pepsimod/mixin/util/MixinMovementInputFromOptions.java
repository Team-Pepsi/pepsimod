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

package net.daporkchop.pepsimod.mixin.util;

import net.daporkchop.pepsimod.gui.clickgui.ClickGUI;
import net.daporkchop.pepsimod.module.impl.movement.InventoryMoveMod;
import net.daporkchop.pepsimod.optimization.OverrideCounter;
import net.daporkchop.pepsimod.util.ReflectionStuff;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.daporkchop.pepsimod.util.PepsiConstants.mc;

@Mixin(MovementInputFromOptions.class)
public abstract class MixinMovementInputFromOptions extends MovementInput {
    @Redirect(
            method = "Lnet/minecraft/util/MovementInputFromOptions;updatePlayerMoveState()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z"
            ))
    public boolean redirectIsKeyDown(KeyBinding binding)    {
        if (((OverrideCounter) binding).isOverriden()) {
            return true;
        }
        if (InventoryMoveMod.INSTANCE.state.enabled && mc.currentScreen != null) {
            if (mc.currentScreen instanceof InventoryEffectRenderer) {
                return Keyboard.isKeyDown(binding.getKeyCode()) || ReflectionStuff.getPressed(binding);
            } else if (mc.world.isRemote && mc.currentScreen instanceof GuiIngameMenu) {
                return Keyboard.isKeyDown(binding.getKeyCode()) || ReflectionStuff.getPressed(binding);
            } else if (mc.currentScreen instanceof ClickGUI) {
                return Keyboard.isKeyDown(binding.getKeyCode()) || ReflectionStuff.getPressed(binding);
            } else if (mc.currentScreen instanceof GuiChat) {
                return ReflectionStuff.getPressed(binding);
            }
        }
        return binding.isKeyDown();
    }
}
