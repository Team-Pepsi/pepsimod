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

package net.daporkchop.pepsimod.accountswitcher.ias.tools;

import net.daporkchop.pepsimod.accountswitcher.tools.alt.AccountData;
import net.daporkchop.pepsimod.accountswitcher.tools.alt.AltDatabase;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

/**
 * Tools that have to do with Skins
 *
 * @author The_Fireplace
 */
@SideOnly(Side.CLIENT)
public class SkinTools {
    public static final File cachedir = new File(Minecraft.getMinecraft().mcDataDir, "cachedImages/skins/");
    private static final File skinOut = new File(cachedir, "temp.png");

    public static void buildSkin(String name) {
        BufferedImage skin;
        try {
            skin = ImageIO.read(new File(cachedir, name + ".png"));
        } catch (IOException e) {
            if (skinOut.exists())
                skinOut.delete();
            return;
        }
        BufferedImage drawing = new BufferedImage(16, 32, BufferedImage.TYPE_INT_ARGB);
        if (skin.getHeight() == 64) {//New skin type
            int[] head = skin.getRGB(8, 8, 8, 8, null, 0, 8);
            int[] torso = skin.getRGB(20, 20, 8, 12, null, 0, 8);
            int[] larm = skin.getRGB(44, 20, 4, 12, null, 0, 4);
            int[] rarm = skin.getRGB(36, 52, 4, 12, null, 0, 4);
            int[] lleg = skin.getRGB(4, 20, 4, 12, null, 0, 4);
            int[] rleg = skin.getRGB(20, 52, 4, 12, null, 0, 4);
            int[] hat = skin.getRGB(40, 8, 8, 8, null, 0, 8);
            int[] jacket = skin.getRGB(20, 36, 8, 12, null, 0, 8);
            int[] larm2 = skin.getRGB(44, 36, 4, 12, null, 0, 4);
            int[] rarm2 = skin.getRGB(52, 52, 4, 12, null, 0, 4);
            int[] lleg2 = skin.getRGB(4, 36, 4, 12, null, 0, 4);
            int[] rleg2 = skin.getRGB(4, 52, 4, 12, null, 0, 4);
            for (int i = 0; i < hat.length; i++)
                if (hat[i] == 0)
                    hat[i] = head[i];
            for (int i = 0; i < jacket.length; i++)
                if (jacket[i] == 0)
                    jacket[i] = torso[i];
            for (int i = 0; i < larm2.length; i++)
                if (larm2[i] == 0)
                    larm2[i] = larm[i];
            for (int i = 0; i < rarm2.length; i++)
                if (rarm2[i] == 0)
                    rarm2[i] = rarm[i];
            for (int i = 0; i < lleg2.length; i++)
                if (lleg2[i] == 0)
                    lleg2[i] = lleg[i];
            for (int i = 0; i < rleg2.length; i++)
                if (rleg2[i] == 0)
                    rleg2[i] = rleg[i];
            drawing.setRGB(4, 0, 8, 8, hat, 0, 8);
            drawing.setRGB(4, 8, 8, 12, jacket, 0, 8);
            drawing.setRGB(0, 8, 4, 12, larm2, 0, 4);
            drawing.setRGB(12, 8, 4, 12, rarm2, 0, 4);
            drawing.setRGB(4, 20, 4, 12, lleg2, 0, 4);
            drawing.setRGB(8, 20, 4, 12, rleg2, 0, 4);
        } else {//old skin type
            int[] head = skin.getRGB(8, 8, 8, 8, null, 0, 8);
            int[] torso = skin.getRGB(20, 20, 8, 12, null, 0, 8);
            int[] arm = skin.getRGB(44, 20, 4, 12, null, 0, 4);
            int[] leg = skin.getRGB(4, 20, 4, 12, null, 0, 4);
            int[] hat = skin.getRGB(40, 8, 8, 8, null, 0, 8);
            for (int i = 0; i < hat.length; i++)
                if (hat[i] == 0)
                    hat[i] = head[i];
            drawing.setRGB(4, 0, 8, 8, hat, 0, 8);
            drawing.setRGB(4, 8, 8, 12, torso, 0, 8);
            drawing.setRGB(0, 8, 4, 12, arm, 0, 4);
            drawing.setRGB(12, 8, 4, 12, arm, 0, 4);
            drawing.setRGB(4, 20, 4, 12, leg, 0, 4);
            drawing.setRGB(8, 20, 4, 12, leg, 0, 4);
        }
        try {
            ImageIO.write(drawing, "png", skinOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Renders the skin built in buildSkin(String name)
     *
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public static void javDrawSkin(int x, int y, int width, int height) {
        if (!skinOut.exists())
            return;
        SkinRender r = new SkinRender(Minecraft.getMinecraft().getTextureManager(), skinOut);
        r.drawImage(x, y, width, height);
    }

    public static void cacheSkins() {
        if (!cachedir.exists())
            if (!cachedir.mkdirs())
                System.out.println("Skin cache directory creation failed.");
        for (AccountData data : AltDatabase.getInstance().getAlts()) {
            File file = new File(cachedir, data.alias + ".png");
            try {
                URL url = new URL(String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", data.alias));
                InputStream is = url.openStream();
                if (file.exists())
                    file.delete();
                file.createNewFile();
                OutputStream os = new FileOutputStream(file);

                byte[] b = new byte[2048];
                int length;

                while ((length = is.read(b)) != -1) {
                    os.write(b, 0, length);
                }
                is.close();
                os.close();
            } catch (IOException e) {
                try {
                    URL url = new URL("http://skins.minecraft.net/MinecraftSkins/direwolf20.png");
                    InputStream is = url.openStream();
                    if (file.exists())
                        file.delete();
                    file.createNewFile();
                    OutputStream os = new FileOutputStream(file);

                    byte[] b = new byte[2048];
                    int length;

                    while ((length = is.read(b)) != -1) {
                        os.write(b, 0, length);
                    }
                    is.close();
                    os.close();
                } catch (IOException i) {
                }
            }
        }
    }
}
