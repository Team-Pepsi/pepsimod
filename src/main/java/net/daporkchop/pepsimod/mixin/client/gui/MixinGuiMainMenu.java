package net.daporkchop.pepsimod.mixin.client.gui;

import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public abstract class MixinGuiMainMenu extends GuiScreen {

    @Shadow
    private String splashText;

    @Inject(method = "initGui", at = @At("RETURN"))
    public void setup(CallbackInfo ci) {
        this.splashText = "§c§lpepsimod";
    }
}
