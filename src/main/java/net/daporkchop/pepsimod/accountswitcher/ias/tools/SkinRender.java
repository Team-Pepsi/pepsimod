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

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Takes care of loading and drawing images to the screen. Adapted from http://www.minecraftforge.net/forum/index.php?topic=11991.0
 *
 * @author dayanto
 * @author The_Fireplace
 */
public class SkinRender {
    private final File file;
    private final TextureManager textureManager;
    private DynamicTexture previewTexture;
    private ResourceLocation resourceLocation;

    public SkinRender(TextureManager textureManager, File file) {
        this.textureManager = textureManager;
        this.file = file;
    }

    /**
     * Attempts to load the image. Returns whether it was successful or not.
     */
    private boolean loadPreview() {
        try {
            BufferedImage image = ImageIO.read(file);
            previewTexture = new DynamicTexture(image);
            resourceLocation = textureManager.getDynamicTextureLocation(Reference.MODID, previewTexture);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void drawImage(int xPos, int yPos, int width, int height) {
        if (previewTexture == null) {
            boolean successful = loadPreview();
            if (!successful) {
                System.out.println("Failure to load preview.");
                return;
            }
        }
        previewTexture.updateDynamicTexture();

        textureManager.bindTexture(resourceLocation);
        Gui.drawModalRectWithCustomSizedTexture(xPos, yPos, 0, 0, width, height, 16 * 4, 32 * 4);
    }
}