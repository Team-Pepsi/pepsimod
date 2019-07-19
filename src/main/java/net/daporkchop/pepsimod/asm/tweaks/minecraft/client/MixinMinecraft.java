/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2016-2019 DaPorkchop_
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

package net.daporkchop.pepsimod.asm.tweaks.minecraft.client;

import net.daporkchop.pepsimod.asm.PepsimodMixinLoader;
import net.daporkchop.pepsimod.util.PepsiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Util;
import org.lwjgl.opengl.Display;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

/**
 * @author DaPorkchop_
 */
@Mixin(Minecraft.class)
abstract class MixinMinecraft {
    @ModifyConstant(
            method = "Lnet/minecraft/client/Minecraft;createDisplay()V",
            constant = @Constant(stringValue = "Minecraft 1.12.2")
    )
    private String changeWindowTitle(String oldTitle) {
        return String.format("pepsimod %s", PepsimodMixinLoader.OBFUSCATED ? "VERSION_FULL" : "(dev environment)");
    }

    /**
     * Use the Pepsi logo as the window icon instead of the default crafting table.
     *
     * @author DaPorkchop_
     * @reason we change the whole thing!
     */
    @Overwrite
    private void setWindowIcon() {
        if (Util.getOSType() != Util.EnumOS.OSX) {
            ByteBuffer[] buffers = new ByteBuffer[PepsiUtil.PEPSI_LOGOS.length];
            for (int i = buffers.length - 1; i >= 0; i--) {
                int size = PepsiUtil.PEPSI_LOGO_SIZES[i];
                ByteBuffer buffer = ByteBuffer.allocate((size * size) << 2);
                BufferedImage img = PepsiUtil.PEPSI_LOGOS[i];
                for (int y = 0; y < size; y++) {
                    for (int x = 0; x < size; x++) {
                        int c = img.getRGB(x, y);
                        buffer.putInt(c << 8 | ((c >> 24) & 255));
                    }
                }
                buffers[i] = (ByteBuffer) buffer.flip();
            }
            Display.setIcon(buffers);
        }
    }
}
