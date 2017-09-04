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
            "gui/pepsimodBackground.png"
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
