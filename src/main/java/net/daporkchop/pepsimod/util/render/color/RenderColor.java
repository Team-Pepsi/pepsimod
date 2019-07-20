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

package net.daporkchop.pepsimod.util.render.color;

import net.minecraft.client.renderer.GlStateManager;

/**
 * A container around a single color.
 *
 * @author DaPorkchop_
 */
public interface RenderColor {
    /**
     * Binds this color to the renderer, using {@link net.minecraft.client.renderer.GlStateManager}.
     */
    default void bind() {
        GlStateManager.color(this.fR(), this.fG(), this.fB(), this.fA());
    }

    /**
     * @return this color as a 32-bit ARGB int
     */
    int argb();

    /**
     * @return this color's alpha channel as an int from 0-255
     */
    int iA();

    /**
     * @return this color's red channel as an int from 0-255
     */
    int iR();

    /**
     * @return this color's green channel as an int from 0-255
     */
    int iG();

    /**
     * @return this color's blue channel as an int from 0-255
     */
    int iB();

    /**
     * @return this color's alpha channel as a float from 0-1
     */
    float fA();

    /**
     * @return this color's red channel as a float from 0-1
     */
    float fR();

    /**
     * @return this color's green channel as a float from 0-1
     */
    float fG();

    /**
     * @return this color's blue channel as a float from 0-1
     */
    float fB();
}
