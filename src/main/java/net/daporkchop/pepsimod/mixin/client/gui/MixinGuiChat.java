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

package net.daporkchop.pepsimod.mixin.client.gui;

import net.daporkchop.pepsimod.command.CommandRegistry;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiChat.class)
public abstract class MixinGuiChat extends GuiScreen {

    public String prevText = "";
    public String prevSuggestion = "";

    @Shadow
    protected GuiTextField inputField;

    @Inject(
            method = "Lnet/minecraft/client/gui/GuiChat;drawScreen(IIF)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiTextField;drawTextBox()V"
            ))
    public void drawSemiTransparentText(CallbackInfo ci) {
        if (this.inputField.getText().startsWith(".")) {
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            int x = this.inputField.x;
            int y = this.inputField.y;
            if (!this.prevText.equals(this.inputField.getText())) {
                this.prevText = this.inputField.getText();
                this.prevSuggestion = CommandRegistry.getSuggestionFor(this.prevText);
            }
            this.fontRenderer.drawString(this.prevSuggestion, x, y, 0x5FFFFFFF, false);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }

    @Inject(
            method = "Lnet/minecraft/client/gui/GuiChat;keyTyped(CI)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void checkIfIsCommandAndProcess(char typedChar, int keyCode, CallbackInfo ci) {
        if (keyCode == 28 || keyCode == 156) {
            if (this.inputField.getText().startsWith(".")) {
                this.mc.ingameGUI.getChatGUI().addToSentMessages(this.inputField.getText());
                this.mc.displayGuiScreen(null);
                CommandRegistry.runCommand(this.inputField.getText());
                ci.cancel();
            }
        }
    }
}
