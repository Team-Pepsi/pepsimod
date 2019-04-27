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

package net.daporkchop.pepsimod.mixin.client;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.impl.render.UnfocusedCPUMod;
import net.daporkchop.pepsimod.module.impl.render.ZoomMod;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.daporkchop.pepsimod.util.config.impl.CpuLimitTranslator;
import net.daporkchop.pepsimod.util.config.impl.FriendsTranslator;
import net.daporkchop.pepsimod.util.misc.ITickListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLLog;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Base64;

import static net.daporkchop.pepsimod.util.PepsiConstants.mc;
import static net.daporkchop.pepsimod.util.PepsiConstants.pepsiMod;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Shadow
    public EntityPlayerSP player;

    @Inject(method = "shutdown()V", at = @At("HEAD"))
    public void saveSettingsOnShutdown(CallbackInfo ci) {
        System.out.println("[PEPSIMOD] Saving config...");
        pepsiMod.saveConfig();
        System.out.println("[PEPSIMOD] Saved.");

        if (ZoomMod.INSTANCE.state.enabled) {
            ModuleManager.disableModule(ZoomMod.INSTANCE);
            mc.gameSettings.fovSetting = ZoomMod.INSTANCE.fov;
        }
    }

    @Inject(method = "runGameLoop", at = @At("RETURN"))
    public void postOnClientPreTick(CallbackInfo callbackInfo) {
        if (mc.player != null && mc.player.movementInput != null) { // is ingame
            for (Module module : ModuleManager.AVALIBLE_MODULES) {
                if (module.shouldTick()) {
                    module.tick();
                }
            }
            for (ITickListener listener : PepsiUtils.toRemoveTickListeners) {
                PepsiUtils.tickListeners.remove(listener);
            }
            PepsiUtils.toRemoveTickListeners.clear();
            for (ITickListener listener : PepsiUtils.tickListeners) {
                listener.tick();
            }
        }
    }

    @Inject(method = "runTickMouse", at = @At(value = "INVOKE_ASSIGN", target = "Lorg/lwjgl/input/Mouse;getEventButton()I"))
    public void onMouseClick(CallbackInfo ci) {
        try {
            if (Mouse.getEventButtonState()) {
                int buttonID = Mouse.getEventButton();
                switch (buttonID) {
                    case 2:
                        if (Minecraft.getMinecraft().objectMouseOver != null) {
                            RayTraceResult result = Minecraft.getMinecraft().objectMouseOver;
                            if (result.typeOfHit == RayTraceResult.Type.ENTITY && result.entityHit instanceof EntityPlayer) {
                                if (FriendsTranslator.INSTANCE.friends.contains(result.entityHit.getUniqueID().toString())) {
                                    this.player.sendMessage(new TextComponentString(PepsiMod.chatPrefix + "Removed \u00A7c" + result.entityHit.getName() + "\u00A7r as a friend"));
                                    FriendsTranslator.INSTANCE.friends.remove(result.entityHit.getUniqueID().toString());
                                } else {
                                    this.player.sendMessage(new TextComponentString(PepsiMod.chatPrefix + "Added \u00A79" + result.entityHit.getName() + "\u00A7r as a friend"));
                                    FriendsTranslator.INSTANCE.friends.add(result.entityHit.getUniqueID().toString());
                                }
                            }
                            FMLLog.log.info(result.entityHit.getClass().getCanonicalName());
                        }
                        break;
                }
            }
        } catch (NullPointerException e) {
            //wtf who cares
        }
    }

    @Redirect(method = "createDisplay", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;setTitle(Ljava/lang/String;)V"))
    public void changeWindowTitle(String title) {
        Display.setTitle("pepsimod 11.1");
    }

    @Inject(method = "setWindowIcon", at = @At("HEAD"), cancellable = true)
    public void preSetWindowIcon(CallbackInfo callbackInfo) {
        try {
            byte[] logo32 = Base64.getDecoder()
                                  .decode("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"
                                          .getBytes());
            byte[] logo16 = Base64.getDecoder()
                                  .decode("iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAMAAAAoLQ9TAAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAACDVBMVEUAAAD////++fneAAD1j5z0+fz0+Pru9PgAWJH//////Pz82+D5uMH3pK/3oa34r7n7ztT+8vT////////84uX2nKjwWW3vSF/0g5L70Nb////////819zzd4f3q7X////////96Orze4v////8/f7////3p7LY5e7k7vP96ezyaXujwte60eH6zNLuQ1uTt9D5u8N9qcf5u8SAq8j6ztTvRVyYu9L96+7ybX8rc6PA1eP////4rLZwocHx9vn/7e7WuMZGha/H2uf////Z6fFuocFLiLG60eH////////l7vObvtRRjLM9f6t/q8jO3+r////////9/v7g6/K70uGjwtegwNawy93T4uz2+fsAWJHtNE3sJ0LsJkHsLkjsKkTrGjfrHDjrGjbsJD/rIj3rHTnrGzftOlL71drtL0nrGzjzfY3/+vv7/f7uPVX709j////9/v7sJUD2nqr//f76/P1yosLtNU7rHzvze4v+8vTf6vE2eqftNU/rIDzzcoP96uz3+vt9qscKXZTsKUT1g5L+7O7n7/WCrckQYpcMX5XtITzyTmP6r7j68/bs9PjL3emQts8/gawHXJQAVY/uP1bqS2Lbh5q+ucuWutJonb8+gKsaaJwEWpIAVpAAV5AFWpJqia0vd6USZZoAWJEAV5EBV5EUZJkAVo8IXJQjbqASY5gQYpgbaZwAAAAprTAwAAAAW3RSTlMAAAAAAAAAAAACKHrA4ufPlUIICWDO+f3jiRsHdu/7qRxW7PyRC8HoT13y/qqZ/ua4/Lf8lf3jWPD+pBm65UhO5/qHBWrp+Z4XBlPD9frbexUBH2mt0te9gjUElCjBbQAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAEDSURBVBjTY2BgYOTk4ubh5eMXEBRiZAACRmERUbHomNg4cQlJKSZGBkZpGdn4hEQgSEqWk1dgZGBWVEpJTAWBtPQMZRVVBgW1zCwwPzUxOydXXYNBUysNwk/Lyy8o1NZh0NWDCCQWFZcUlJbpMxiUg01IrKisKiiorjFkMKoFCSTW1TcUFDQ2NRszmJgCtSS0tLYVFLR3dHaZMZhbpKV19/T29U+YOGnyFEsrBmubqdOmz5g5a/acufPmL7C1Y2Cxd1i4aPHcefPmL1m6zNFJiIHR2cV1+Yp585fOn7fSzd2DFeg5Ty9vn1Wr16z19fMPYAP5lzEwKDgkNCw8ItKZnSMKAAihVlWjuDlCAAAAJXRFWHRkYXRlOmNyZWF0ZQAyMDE3LTA5LTA5VDE3OjE3OjAyKzAyOjAw4iAIuQAAACV0RVh0ZGF0ZTptb2RpZnkAMjAxNy0wOS0wOVQxNzoxNzowMiswMjowMJN9sAUAAAAASUVORK5CYII="
                                          .getBytes());
            ByteArrayInputStream is32 = new ByteArrayInputStream(logo32);
            ByteArrayInputStream is16 = new ByteArrayInputStream(logo16);
            Display.setIcon(new ByteBuffer[]{this.readImageToBuffer(is16), this.readImageToBuffer(is32)});
            is32.close();
            is16.close();
            callbackInfo.cancel();
        } catch (IOException e) {
            e.printStackTrace();
            //thonk
        }

        //in case of exception vanilla code will run
    }

    @Shadow
    private ByteBuffer readImageToBuffer(InputStream imageStream) throws IOException {
        return null;
    }

    @Inject(method = "displayGuiScreen", at = @At("HEAD"))
    public void preDisplayGuiScreen(GuiScreen guiScreen, CallbackInfo callbackInfo) {
        if (ZoomMod.INSTANCE.state.enabled) {
            ModuleManager.disableModule(ZoomMod.INSTANCE);
            mc.gameSettings.fovSetting = ZoomMod.INSTANCE.fov;
        }
    }

    @Inject(method = "getLimitFramerate", at = @At("HEAD"), cancellable = true)
    public void preGetLimitFramerate(CallbackInfoReturnable<Integer> callbackInfoReturnable) {
        try {
            if (UnfocusedCPUMod.INSTANCE.state.enabled && !Display.isActive()) {
                callbackInfoReturnable.setReturnValue(CpuLimitTranslator.INSTANCE.limit);
            }
        } catch (NullPointerException e) {

        }
    }

    @Inject(method = "processKeyBinds", at = @At("HEAD"))
    public void preProcessKeyBinds(CallbackInfo ci) {
        // If . is typed open GuiChat
        // Bypass the keybind system because the command prefix is not configurable
        if (mc.currentScreen == null && Keyboard.getEventCharacter() == '.') {
            mc.displayGuiScreen(new GuiChat("."));
        }
    }
}
