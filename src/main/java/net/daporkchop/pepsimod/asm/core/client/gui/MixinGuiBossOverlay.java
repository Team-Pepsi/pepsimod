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

import net.daporkchop.pepsimod.util.BossinfoCounted;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.BossInfoClient;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.BossInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

/**
 * I made this for Future!
 * of course i added it here :P
 */
@Mixin(GuiBossOverlay.class)
public abstract class MixinGuiBossOverlay extends Gui {
    private final ArrayList<BossinfoCounted> counted_cache = new ArrayList<>();
    public ResourceLocation GUI_BARS_TEXTURES_ALT = new ResourceLocation("textures/gui/bars.png");
    @Shadow
    @Final
    private Map<UUID, BossInfoClient> mapBossInfos;
    @Shadow
    @Final
    private Minecraft client;

    @Inject(
            method = "Lnet/minecraft/client/gui/GuiBossOverlay;renderBossHealth()V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void preRenderBossHealth(CallbackInfo callbackInfo) {
        if (!this.mapBossInfos.isEmpty()) {
            ScaledResolution scaledresolution = new ScaledResolution(this.client);
            int i = scaledresolution.getScaledWidth();
            int j = 12;
            for (BossinfoCounted counted : this.counted_cache) {
                int k = i / 2 - 91;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.client.getTextureManager().bindTexture(this.GUI_BARS_TEXTURES_ALT);
                this.render(k, j, counted.info);
                String s = counted.info.getName().getFormattedText() + (counted.count > 1 ? " (x" + counted.count + ')' : "");
                this.client.fontRenderer.drawStringWithShadow(s, (float) (i / 2 - this.client.fontRenderer.getStringWidth(s) / 2), (float) (j - 9), 16777215);
                j += 10 + this.client.fontRenderer.FONT_HEIGHT;

                if (j >= scaledresolution.getScaledHeight() / 3) {
                    break;
                }
            }
        }
        callbackInfo.cancel();
    }

    @Shadow
    private void render(int x, int y, BossInfo info) {
    }

    @Inject(
            method = "Lnet/minecraft/client/gui/GuiBossOverlay;read(Lnet/minecraft/network/play/server/SPacketUpdateBossInfo;)V",
            at = @At("HEAD")
    )
    public void read(SPacketUpdateBossInfo packetIn, CallbackInfo callbackInfo) {
        this.counted_cache.clear();
        ArrayList<String> known = new ArrayList<>();
        for (BossInfoClient infoLerping : this.mapBossInfos.values()) {
            if (known.contains(infoLerping.getName().getFormattedText())) {
                continue;
            }
            String formattedText = infoLerping.getName().getFormattedText();
            BossinfoCounted counted = new BossinfoCounted();
            counted.info = infoLerping;
            for (BossInfoClient infoLerping2 : this.mapBossInfos.values()) {
                if (infoLerping2.getName().getFormattedText().equals(formattedText)) {
                    counted.count++;
                }
            }
            known.add(formattedText);
            this.counted_cache.add(counted);
        }
    }
}
