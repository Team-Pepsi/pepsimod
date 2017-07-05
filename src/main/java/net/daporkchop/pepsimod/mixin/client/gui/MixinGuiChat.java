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

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiTextField;drawTextBox()V"))
    public void drawSemiTransparentText(CallbackInfo ci) {
        if (inputField.getText().startsWith(".")) {
            GL11.glPushMatrix();
            GL11.glEnable(GL11.GL_BLEND);
            int x = inputField.xPosition;
            int y = inputField.yPosition;
            if (!prevText.equals(inputField.getText())) {
                prevText = inputField.getText();
                prevSuggestion = CommandRegistry.getSuggestionFor(prevText);
            }
            fontRenderer.drawString(prevSuggestion, x, y, 0x5FFFFFFF, false);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
    }

    @Inject(method = "keyTyped", at = @At("HEAD"), cancellable = true)
    public void checkIfIsCommandAndProcess(char typedChar, int keyCode, CallbackInfo ci) {
        if (keyCode == 28 || keyCode == 156) {
            if (this.inputField.getText().startsWith(".")) {
                CommandRegistry.runCommand(this.inputField.getText());
                this.mc.ingameGUI.getChatGUI().addToSentMessages(this.inputField.getText());
                this.mc.displayGuiScreen(null);
                ci.cancel();
            }
        }
    }
}
