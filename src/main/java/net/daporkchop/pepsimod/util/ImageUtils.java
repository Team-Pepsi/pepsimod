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

package net.daporkchop.pepsimod.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageUtils {
    public static HashMap<Integer, ResourceLocation> imgs = null;

    public static String[] names = new String[]{
            "gui/pepsibuttons.png",
            "gui/pepsimod.png",
            "gui/pepsimodBackground.png",
            "misc/cape.png"
    };

    public static void init(HashMap<String, byte[]> images) {
        HashMap<Integer, DynamicTexture> temp = new HashMap<>();
        for (Map.Entry<String, byte[]> entry : images.entrySet()) {
            for (int i = 0; i < names.length; i++) {
                if (entry.getKey().endsWith(names[i])) {
                    temp.put(i, new DynamicTexture(createImageFromBytes(entry.getValue())));
                }
            }
        }
        imgs = new HashMap<>();
        for (Map.Entry<Integer, DynamicTexture> entry : temp.entrySet()) {
            imgs.put(entry.getKey(), Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("pepsimod_texture_" + entry.getKey(), entry.getValue()));
        }
    }

    public static BufferedImage createImageFromBytes(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            BufferedImage a = ImageIO.read(bais);
            bais.close();
            return a;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
