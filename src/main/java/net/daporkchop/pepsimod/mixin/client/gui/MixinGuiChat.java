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
