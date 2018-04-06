/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2018 DaPorkchop_
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
        }
    }
}
