package net.daporkchop.pepsimod.mixin.client.gui;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.util.colors.rainbow.RainbowText;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class MixinGuiIngame extends Gui {

    @Shadow
    protected final Minecraft mc;

    public RainbowText PEPSI_NAME = new RainbowText("PepsiMod " + PepsiMod.VERSION);

    {
        mc = null;
    }

    @Inject(method = "renderGameOverlay", at = @At("RETURN"))
    public void renderGameOverlay(float partialTicks, CallbackInfo ci) {
        PEPSI_NAME.drawAtPos(this, 2, 2);

        for (int i = 0; i < ModuleManager.ENABLED_MODULES.size(); i++) {
            Module module = ModuleManager.ENABLED_MODULES.get(i);
            module.text.drawAtPos(this, 2, 22 + i * 10);
        }
    }
}
