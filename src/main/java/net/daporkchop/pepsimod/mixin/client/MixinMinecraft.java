/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017 Team Pepsi
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from Team Pepsi.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: Team Pepsi), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.daporkchop.pepsimod.mixin.client;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.util.Friends;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.input.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
    @Shadow
    public EntityPlayerSP player;

    @Inject(method = "shutdown()V", at = @At("HEAD"))
    public void saveSettingsOnShutdown(CallbackInfo ci) {
        PepsiMod.INSTANCE.saveConfig();
    }

    @Inject(method = "runGameLoop", at = @At("RETURN"))
    public void onClientPreTick(CallbackInfo callbackInfo)  {
        if (PepsiMod.INSTANCE.mc.player != null) { // is ingame
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
                                if (Friends.isFriend(result.entityHit.getUniqueID().toString())) {
                                    player.sendMessage(new TextComponentString(PepsiMod.chatPrefix + "Removed \u00A7c" + result.entityHit.getName() + "\u00A7r as a friend"));
                                    Friends.removeFriend(result.entityHit.getUniqueID().toString());
                                } else {
                                    player.sendMessage(new TextComponentString(PepsiMod.chatPrefix + "Added \u00A79" + result.entityHit.getName() + "\u00A7r as a friend"));
                                    Friends.addFriend(result.entityHit.getUniqueID().toString(), result.entityHit.getName());
                                }
                            }
                            System.out.println(result.entityHit.getClass().getCanonicalName());
                        }
                        break;
                }
            }
        } catch (NullPointerException e) {
            //wtf who cares
        }
    }
}
