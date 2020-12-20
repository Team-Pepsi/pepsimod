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

package net.daporkchop.pepsimod.asm.core.client.gui;

import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.impl.render.ZoomMod;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.config.impl.GeneralTranslator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiDisconnected.class)
public abstract class MixinGuiDisconnected extends GuiScreen {

    @Shadow
    private int textHeight;

    @Inject(
            method = "Lnet/minecraft/client/gui/GuiDisconnected;drawScreen(IIF)V",
            at = @At("HEAD")
    )
    public void preDrawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo callbackInfo) {
        if (ZoomMod.INSTANCE.state.enabled) {
            ModuleManager.disableModule(ZoomMod.INSTANCE);
            this.mc.gameSettings.fovSetting = ZoomMod.INSTANCE.fov;
        }
    }

    @Inject(
            method = "Lnet/minecraft/client/gui/GuiDisconnected;initGui()V",
            at = @At("RETURN")
    )
    public void postInitGui(CallbackInfo callbackInfo) {
        PepsiUtils.autoReconnectWaitTime = 5;
        this.buttonList.add(PepsiUtils.reconnectButton = new GuiButton(1, this.width / 2 - 100, Math.min(this.height / 2 + this.textHeight / 2 + this.fontRenderer.FONT_HEIGHT + 22, this.height - 30 + 22), "Reconnect"));
        this.buttonList.add(PepsiUtils.autoReconnectButton = new GuiButton(2, this.width / 2 - 100, Math.min(this.height / 2 + this.textHeight / 2 + this.fontRenderer.FONT_HEIGHT + 44, this.height - 30 + 44), "AutoReconnect"));
        if (!GeneralTranslator.INSTANCE.autoReconnect) {
            PepsiUtils.autoReconnectButton.displayString = "AutoReconnect (\u00A7cDisabled\u00A7r)";
        }
    }

    @Inject(
            method = "Lnet/minecraft/client/gui/GuiDisconnected;actionPerformed(Lnet/minecraft/client/gui/GuiButton;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void preActionPerformed(GuiButton button, CallbackInfo callbackInfo) {
        if (button.id == 1) {
            ServerData data = new ServerData("", PepsiUtils.lastIp + ':' + PepsiUtils.lastPort, false);
            data.setResourceMode(ServerData.ServerResourceMode.PROMPT);
            FMLClientHandler.instance().connectToServer(this.mc.currentScreen, data);
            callbackInfo.cancel();
        } else if (button.id == 2) {
            GeneralTranslator.INSTANCE.autoReconnect = !GeneralTranslator.INSTANCE.autoReconnect;
            if (GeneralTranslator.INSTANCE.autoReconnect) {
                PepsiUtils.autoReconnectWaitTime = 5;
                PepsiUtils.autoReconnectButton.displayString = "AutoReconnect (\u00A7a" + PepsiUtils.autoReconnectWaitTime + "\u00A7r)";
            } else {
                PepsiUtils.autoReconnectButton.displayString = "AutoReconnect (\u00A7cDisabled\u00A7r)";
            }
            callbackInfo.cancel();
        }
    }
}
