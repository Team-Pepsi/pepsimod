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

package net.daporkchop.pepsimod.mixin.client.gui;

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
import org.spongepowered.asm.mixin.Overwrite;
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
    private final ArrayList<BossinfoCounted> counted_cache = new ArrayList<BossinfoCounted>();
    public ResourceLocation GUI_BARS_TEXTURES_ALT = new ResourceLocation("textures/gui/bars.png");
    @Shadow
    @Final
    private Map<UUID, BossInfoClient> mapBossInfos;
    @Shadow
    @Final
    private Minecraft client;

    /**
     * this hides a stupid warning
     */
    @Overwrite
    public void renderBossHealth() {
        if (!this.mapBossInfos.isEmpty()) {
            ScaledResolution scaledresolution = new ScaledResolution(this.client);
            int i = scaledresolution.getScaledWidth();
            int j = 12;
            for (BossinfoCounted counted : this.counted_cache) {
                int k = i / 2 - 91;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.client.getTextureManager().bindTexture(GUI_BARS_TEXTURES_ALT);
                this.render(k, j, counted.info);
                String s = counted.info.getName().getFormattedText() + (counted.count > 1 ? " (x" + counted.count + ")" : "");
                this.client.fontRenderer.drawStringWithShadow(s, (float) (i / 2 - this.client.fontRenderer.getStringWidth(s) / 2), (float) (j - 9), 16777215);
                j += 10 + this.client.fontRenderer.FONT_HEIGHT;

                if (j >= scaledresolution.getScaledHeight() / 3) {
                    break;
                }
            }
        }
    }

    @Shadow
    private void render(int x, int y, BossInfo info) {

    }

    @Inject(method = "read", at = @At("HEAD"))
    public void read(SPacketUpdateBossInfo packetIn, CallbackInfo callbackInfo) {
        updateCounter();
    }

    public void updateCounter() {
        counted_cache.clear();
        ArrayList<String> known = new ArrayList<>();
        for (BossInfoClient infoLerping : mapBossInfos.values()) {
            if (known.contains(infoLerping.getName().getFormattedText()))
                continue;
            String formattedText = infoLerping.getName().getFormattedText();
            BossinfoCounted counted = new BossinfoCounted();
            counted.info = infoLerping;
            for (BossInfoClient infoLerping2 : mapBossInfos.values()) {
                if (infoLerping2.getName().getFormattedText().equals(formattedText))
                    counted.count++;
            }
            known.add(formattedText);
            counted_cache.add(counted);
        }
    }
}
