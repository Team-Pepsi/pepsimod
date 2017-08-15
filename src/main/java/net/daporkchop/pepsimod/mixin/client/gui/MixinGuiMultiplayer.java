package net.daporkchop.pepsimod.mixin.client.gui;

import net.daporkchop.pepsimod.gui.mcleaks.GuiButtonMCLeaks;
import net.daporkchop.pepsimod.gui.mcleaks.GuiScreenMCLeaks;
import net.daporkchop.pepsimod.gui.misc.GuiButtonTooBeeTooTee;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMultiplayer.class)
public abstract class MixinGuiMultiplayer extends GuiScreen {

    @Inject(method = "createButtons", at = @At("RETURN"))
    public void createButtons(CallbackInfo ci) {
        this.buttonList.add(new GuiButtonMCLeaks(9, 6, 6, 20, 20));
        this.buttonList.add(new GuiButtonTooBeeTooTee(10, this.width - 26, 6, 20, 20));
        this.buttonList.add(PepsiUtils.protocolSwitchButton);
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"), cancellable = true)
    public void actionPerformed(GuiButton button, CallbackInfo ci) {
        if (button.id == 9) {
            GuiScreenMCLeaks mcLeaks = new GuiScreenMCLeaks(this, Minecraft.getMinecraft());
            Minecraft.getMinecraft().displayGuiScreen(mcLeaks);
            ci.cancel();
        } else if (button.id == 10) {
            FMLClientHandler.instance().connectToServer(new GuiMainMenu(), PepsiUtils.TOOBEETOOTEE_DATA);
            ci.cancel();
        } else if (button.id == 11) {
            PepsiUtils.versionIndex++;
            if (PepsiUtils.versionIndex == PepsiUtils.protocols.length) {
                PepsiUtils.versionIndex = 0;
            }
            PepsiUtils.protocolVersion = PepsiUtils.protocols[PepsiUtils.versionIndex];
            PepsiUtils.updateProtocolButton();
            ci.cancel();
        }
    }
}
