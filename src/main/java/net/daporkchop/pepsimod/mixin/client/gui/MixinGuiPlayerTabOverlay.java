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

package net.daporkchop.pepsimod.mixin.client.gui;

import net.daporkchop.pepsimod.misc.data.Group;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

import static net.daporkchop.pepsimod.util.PepsiConstants.pepsimod;

/**
 * @author DaPorkchop_
 */
@Mixin(GuiPlayerTabOverlay.class)
public abstract class MixinGuiPlayerTabOverlay extends Gui {
    @Shadow @Final private Minecraft mc;

    @ModifyConstant(
            method = "Lnet/minecraft/client/gui/GuiPlayerTabOverlay;renderPlayerlist(ILnet/minecraft/scoreboard/Scoreboard;Lnet/minecraft/scoreboard/ScoreObjective;)V",
            constant = @Constant(
                    intValue = 9,
                    ordinal = 0
            ))
    public int changePlayerBoxWidthIncrease(int old) {
        return old + 9;
    }

    @Inject(
            method = "Lnet/minecraft/client/gui/GuiPlayerTabOverlay;renderPlayerlist(ILnet/minecraft/scoreboard/Scoreboard;Lnet/minecraft/scoreboard/ScoreObjective;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiPlayerTabOverlay;drawRect(IIIII)V",
                    ordinal = 2
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void drawPlayerBoxBackgroundCustom(int width, Scoreboard scoreboardIn, ScoreObjective scoreObjectiveIn,
                                              CallbackInfo ci,
                                              NetHandlerPlayClient nethandlerplayclient, List<NetworkPlayerInfo> list, int i, int j, int l3, int i4, int j4, boolean flag, int l, int i1, int j1, int k1, int l1, List<String> list1, List<String> list2, int k4, int l4, int i5, int j2, int k2) {
        int color = 553648126;
        if (k4 < list.size()) {
            Group group = pepsimod.data.getGroup(list.get(k4));
            if (group != null && group.color != 0) {
                color = group.color | 0x80000000;
            }
        }
        drawRect(j2, k2, j2 + i1, k2 + 8, color);
    }

    @ModifyConstant(
            method = "Lnet/minecraft/client/gui/GuiPlayerTabOverlay;renderPlayerlist(ILnet/minecraft/scoreboard/Scoreboard;Lnet/minecraft/scoreboard/ScoreObjective;)V",
            constant = @Constant(
                    intValue = 553648127
            ))
    public int changePlayerBoxBackgroundColor(int old) {
        return 0;
    }

    @Redirect(
            method = "Lnet/minecraft/client/gui/GuiPlayerTabOverlay;drawPing(IIILnet/minecraft/client/network/NetworkPlayerInfo;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/texture/TextureManager;bindTexture(Lnet/minecraft/util/ResourceLocation;)V"
            ))
    public void preventExtraPingTextureBind(TextureManager manager, ResourceLocation location)    {
    }

    @Inject(
            method = "Lnet/minecraft/client/gui/GuiPlayerTabOverlay;drawPing(IIILnet/minecraft/client/network/NetworkPlayerInfo;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/GuiPlayerTabOverlay;drawTexturedModalRect(IIIIII)V"
            ))
    public void drawIconIfPossible(int p_175245_1_, int p_175245_2_, int p_175245_3_, NetworkPlayerInfo info, CallbackInfo callbackInfo)    {
        Group group = pepsimod.data.getGroup(info);
        if (group != null)    {
            group.doWithIconIfPresent(tex -> tex.render(p_175245_2_ + p_175245_1_ - 11 - 9, p_175245_3_, 8, 8));
        }
        this.mc.getTextureManager().bindTexture(ICONS);
    }
}
