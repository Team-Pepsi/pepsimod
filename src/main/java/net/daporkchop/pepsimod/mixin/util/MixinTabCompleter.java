package net.daporkchop.pepsimod.mixin.util;

import net.daporkchop.pepsimod.command.CommandRegistry;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.TabCompleter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TabCompleter.class)
public class MixinTabCompleter {
    @Shadow
    @Final
    protected GuiTextField textField;

    @Inject(method = "complete", at = @At("HEAD"), cancellable = true)
    public void preComplete(CallbackInfo callbackInfo) {
        if (textField.getText().startsWith(".")) {
            String completed = CommandRegistry.getSuggestionFor(textField.getText());
            if (completed == null || completed.isEmpty() || completed.length() <= textField.getText().length()) {
                return;
                //run vanilla code
            }
            textField.setText(completed);
            callbackInfo.cancel();
            return;
        }
    }
}
