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

package net.daporkchop.pepsimod.asm.core.minecraft.client.gui;

import net.daporkchop.pepsimod.util.mixin.client.gui.GuiBossOverlay.MergedBossInfo;
import net.minecraft.client.gui.BossInfoClient;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.play.server.SPacketUpdateBossInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.BossInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.daporkchop.pepsimod.util.PepsiConstants.RESOLUTION;
import static net.daporkchop.pepsimod.util.PepsiConstants.mc;

/**
 * Inspired by the version I made for Future.
 * <p>
 * This merges boss bars with identical names together and adds a count behind boss bars that appear multiple times.
 * <p>
 * A major difference from the Future version (aside from the implementation, which is basically completely different) is that it renders all boss bars
 * simultaneously, adjusting the opacity according to the count.
 *
 * @author DaPorkchop_
 */
@Mixin(GuiBossOverlay.class)
abstract class MixinGuiBossOverlay extends Gui {
    @Shadow
    @Final
    private static ResourceLocation          GUI_BARS_TEXTURES;
    @Shadow
    @Final
    private        Map<UUID, BossInfoClient> mapBossInfos;

    private final Map<String, MergedBossInfo> mergedBossInfos = new HashMap<>();

    /**
     * This method is overwritten because I change nearly the whole thing.
     *
     * @author DaPorkchop_
     * @reason efficiency
     */
    @Overwrite
    public void renderBossHealth() {
        if (!this.mergedBossInfos.isEmpty()) {
            int abortHeight = RESOLUTION.height() / 3; //the Y position at which we will stop rendering boss bars to avoid filling the screen

            int center = RESOLUTION.width() >> 1;
            int x = center - 91;
            int y = 12;

            for (MergedBossInfo merged : this.mergedBossInfos.values()) {
                { //render boss bars
                    mc.getTextureManager().bindTexture(GUI_BARS_TEXTURES);
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

                    //draw background
                    this.drawTexturedModalRect(x, y, 0, merged.color().ordinal() * 10, 182, 5);

                    //draw health bars
                    //we use an alpha level equal to 1/count to make sure that one can see the health of all of the bars individually, even though they're drawn on top of each other.
                    //TODO: for whatever reason the alpha seems to be more of an exponential curve than a linear one, or maybe that's just an illusion. either way it's hard to differentiate between individual boss bars, maybe i need to improve the contrast somehow or add a marker.
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f / merged.entries().size());
                    for (BossInfoClient info : merged.entries()) {
                        int progress = (int) (info.getPercent() * 183.0f);
                        if (progress > 0) {
                            this.drawTexturedModalRect(x, y, 0, info.getColor().ordinal() * 10 + 5, progress, 5);
                        }
                    }

                    //draw progress overlay thing (has notches for steps)
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    if (merged.overlay() != BossInfo.Overlay.PROGRESS) {
                        this.drawTexturedModalRect(x, y, 0, 80 + (merged.overlay().ordinal() - 1) * 10, 182, 5);
                    }
                }

                { //draw name
                    //we only add the count suffix if there's multiple boss bars on top of each other
                    String s = merged.count() == 1 ? merged.name() : String.format("%s§r §7(§fx%d§7)", merged.name(), merged.count());
                    mc.fontRenderer.drawStringWithShadow(s, center - mc.fontRenderer.getStringWidth(s) * 0.5f, y - 9.0f, 16777215);
                }

                //move down
                y += 10 + mc.fontRenderer.FONT_HEIGHT;
                if (y >= abortHeight) {
                    //abort if the vertical position is too far down the screen
                    break;
                }
            }
        }
    }

    /**
     * This method is overwritten because I change nearly the whole thing.
     *
     * @author DaPorkchop_
     * @reason efficiency
     */
    @Overwrite
    public void read(SPacketUpdateBossInfo packetIn) {
        switch (packetIn.getOperation()) {
            case ADD: {
                BossInfoClient info = new BossInfoClient(packetIn);
                this.mapBossInfos.put(packetIn.getUniqueId(), info);
                this.mergedBossInfos.computeIfAbsent(info.getName().getFormattedText(), MergedBossInfo::new).add(info);
            }
            break;
            case REMOVE: {
                BossInfoClient info = this.mapBossInfos.remove(packetIn.getUniqueId());
                String text = info.getName().getFormattedText();
                MergedBossInfo merged = this.mergedBossInfos.get(text);
                if (merged != null && merged.remove(info)) { //remove the old MergedBossInfo if it's empty
                    this.mergedBossInfos.remove(text);
                }
            }
            break;
            case UPDATE_NAME: {
                //remove this entry from the old name and add it to the new name
                BossInfoClient info = this.mapBossInfos.get(packetIn.getUniqueId());
                String text = info.getName().getFormattedText();
                MergedBossInfo merged = this.mergedBossInfos.get(text);
                if (merged != null && merged.remove(info)) {
                    this.mergedBossInfos.remove(text);
                }
                info.setName(packetIn.getName());
                this.mergedBossInfos.computeIfAbsent(info.getName().getFormattedText(), MergedBossInfo::new).add(info);
            }
            break;
            default:
                //otherwise we let the packet handle it
                this.mapBossInfos.get(packetIn.getUniqueId()).updateFromPacket(packetIn);
        }
    }

    /**
     * This method is overwritten for efficiency since I add one line.
     *
     * @author DaPorkchop_
     * @reason efficiency
     */
    @Overwrite
    public void clearBossInfos() {
        this.mapBossInfos.clear();
        this.mergedBossInfos.clear();
    }
}

