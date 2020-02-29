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

    @Inject(
            method = "Lnet/minecraft/client/gui/GuiMultiplayer;createButtons()V",
            at = @At("RETURN")
    )
    public void createButtons(CallbackInfo ci) {
        this.buttonList.add(new GuiButtonMCLeaks(9, 6, 6, 20, 20));
        this.buttonList.add(new GuiButtonTooBeeTooTee(10, this.width - 26, 6, 20, 20));
    }

    @Inject(
            method = "Lnet/minecraft/client/gui/GuiMultiplayer;actionPerformed(Lnet/minecraft/client/gui/GuiButton;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void actionPerformed(GuiButton button, CallbackInfo ci) {
        if (button.id == 9) {
            GuiScreenMCLeaks mcLeaks = new GuiScreenMCLeaks(this, Minecraft.getMinecraft());
            Minecraft.getMinecraft().displayGuiScreen(mcLeaks);
            ci.cancel();
        } else if (button.id == 10) {
            FMLClientHandler.instance().connectToServer(new GuiMainMenu(), PepsiUtils.TOOBEETOOTEE_DATA);
            ci.cancel();
        }
    }
}
