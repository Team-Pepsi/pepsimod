/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2019 DaPorkchop_
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
import net.daporkchop.pepsimod.PepsiModMixinLoader;
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
import static net.daporkchop.pepsimod.util.PepsiConstants.pepsimod;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Shadow
    public EntityPlayerSP player;

    @Inject(method = "shutdown()V", at = @At("HEAD"))
    public void saveSettingsOnShutdown(CallbackInfo ci) {
        System.out.println("[PEPSIMOD] Saving config...");
        pepsimod.saveConfig();
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
                                if (FriendsTranslator.INSTANCE.isFriend(result.entityHit)) {
                                    this.player.sendMessage(new TextComponentString(PepsiMod.chatPrefix + "Removed \u00A7c" + result.entityHit.getName() + "\u00A7r as a friend"));
                                    FriendsTranslator.INSTANCE.friends.remove(result.entityHit.getUniqueID());
                                } else {
                                    this.player.sendMessage(new TextComponentString(PepsiMod.chatPrefix + "Added \u00A79" + result.entityHit.getName() + "\u00A7r as a friend"));
                                    FriendsTranslator.INSTANCE.friends.add(result.entityHit.getUniqueID());
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
        Display.setTitle(PepsiModMixinLoader.isObfuscatedEnvironment ? "pepsimod 11.1" : "pepsimod 11.1 (dev environment)");
    }

    @Inject(method = "setWindowIcon", at = @At("HEAD"), cancellable = true)
    public void preSetWindowIcon(CallbackInfo callbackInfo) {
        try (InputStream in = PepsiMod.class.getResourceAsStream("/pepsilogo.png")) {
            Display.setIcon(new ByteBuffer[]{this.readImageToBuffer(in)});
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
