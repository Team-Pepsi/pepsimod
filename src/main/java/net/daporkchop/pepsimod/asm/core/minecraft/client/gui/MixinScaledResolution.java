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

import net.daporkchop.pepsimod.util.render.BetterScaledResolution;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import static net.daporkchop.pepsimod.util.PepsiConstants.mc;

/**
 * This modifies {@link ScaledResolution} to implement {@link BetterScaledResolution}, which in turn allows me to re-use the same ScaledResolution instance
 * forever instead of the vanilla behavior of every GUI and renderer creating their own new instance every frame.
 *
 * @author DaPorkchop_
 */
@Mixin(ScaledResolution.class)
abstract class MixinScaledResolution implements BetterScaledResolution {
    @Shadow
    private int    scaledWidth;
    @Shadow
    private int    scaledHeight;
    @Shadow
    private int    scaleFactor;
    @Shadow
    @Final
    @Mutable
    private double scaledWidthD;
    @Shadow
    @Final
    @Mutable
    private double scaledHeightD;

    @Override
    public int width() {
        return this.scaledWidth;
    }

    @Override
    public int height() {
        return this.scaledHeight;
    }

    @Override
    public ScaledResolution getAsMinecraft() throws UnsupportedOperationException {
        return (ScaledResolution) (Object) this;
    }

    @Override
    public void update() {
        this.scaledWidth = mc.displayWidth;
        this.scaledHeight = mc.displayHeight;
        this.scaleFactor = 1;
        boolean unicode = mc.isUnicode();
        int i = mc.gameSettings.guiScale;

        if (i == 0) {
            i = 1000;
        }

        while (this.scaleFactor < i && this.scaledWidth / (this.scaleFactor + 1) >= 320 && this.scaledHeight / (this.scaleFactor + 1) >= 240) {
            ++this.scaleFactor;
        }

        if (unicode && this.scaleFactor % 2 != 0 && this.scaleFactor != 1) {
            --this.scaleFactor;
        }

        this.scaledWidthD = (double) this.scaledWidth / (double) this.scaleFactor;
        this.scaledHeightD = (double) this.scaledHeight / (double) this.scaleFactor;
        this.scaledWidth = MathHelper.ceil(this.scaledWidthD);
        this.scaledHeight = MathHelper.ceil(this.scaledHeightD);
    }
}
