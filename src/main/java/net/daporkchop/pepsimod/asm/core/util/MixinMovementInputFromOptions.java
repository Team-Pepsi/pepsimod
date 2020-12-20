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

package net.daporkchop.pepsimod.asm.core.util;

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
