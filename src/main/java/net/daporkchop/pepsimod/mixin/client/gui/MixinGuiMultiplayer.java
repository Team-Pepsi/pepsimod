package net.daporkchop.pepsimod.mixin.client.gui;

import com.google.common.collect.Lists;
import net.daporkchop.pepsimod.gui.mcleaks.GuiButtonMCLeaks;
import net.daporkchop.pepsimod.gui.mcleaks.GuiScreenMCLeaks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiMultiplayer.class)
public abstract class MixinGuiMultiplayer extends GuiScreen {

    @Inject(method = "createButtons", at = @At("RETURN"))
    public void createButtons(CallbackInfo ci)  {
        this.buttonList.add(new GuiButtonMCLeaks(9, 6, 6, 20, 20));
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
    public void actionPerformed(GuiButton button, CallbackInfo ci)  {
        if (button.id == 9) {
            GuiScreenMCLeaks mcLeaks = new GuiScreenMCLeaks(this, Minecraft.getMinecraft());
            Minecraft.getMinecraft().displayGuiScreen(mcLeaks);
            ci.cancel();
        }
    }
}
