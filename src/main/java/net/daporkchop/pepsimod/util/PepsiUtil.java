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

package net.daporkchop.pepsimod.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

/**
 * Common methods used throughout the mod.
 *
 * @author DaPorkchop_
 */
public final class PepsiUtil implements PepsiConstants {
    public static final int[]           PEPSI_LOGO_SIZES = {16, 32, 64, 128, 256};
    public static final BufferedImage[] PEPSI_LOGOS      = new BufferedImage[PEPSI_LOGO_SIZES.length];
    public static final char[]          RANDOM_COLORS    = {'c', '9', 'f', '1', '4'};

    static {
        for (int i = PEPSI_LOGOS.length - 1; i >= 0; i--) {
            try (InputStream in = PepsiUtil.class.getResourceAsStream(String.format("/assets/pepsimod/textures/icon/pepsilogo-%d.png", PEPSI_LOGO_SIZES[i]))) {
                PEPSI_LOGOS[i] = ImageIO.read(in);
            } catch (Exception e) {
                log.error("Unable to load pepsilogo at %1$dx%1$d resolution!", PEPSI_LOGO_SIZES[i]);
            }
        }
    }

    /**
     * This returns {@code null} in a very roundabout way. This is to work around warnings in IntelliJ that certain values are always
     * {@code null} when they're actually initialized reflectively at runtime.
     *
     * @param <T> the type of {@code null} to get
     * @return {@code null}
     */
    @SuppressWarnings("unchecked")
    public static <T> T getNull() {
        Object[] o = new Object[1];
        return (T) o[0];
    }

    /**
     * This returns the input value in a very roundabout way. This is to work around warnings in IntelliJ that certain values are constant when
     * they're actually initialized reflectively at runtime.
     *
     * @param val the value to get
     * @param <T> the type of value to get
     * @return the input value
     */
    public static <T> T getSelfValue(T val) {
        return val;
    }
}
