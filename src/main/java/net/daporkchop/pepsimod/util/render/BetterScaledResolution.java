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

package net.daporkchop.pepsimod.util.render;

import net.daporkchop.pepsimod.util.capability.Updateable;
import net.minecraft.client.gui.ScaledResolution;

/**
 * An interface injected into {@link ScaledResolution} to prevent it from having to be allocated hundreds of times per frame.
 *
 * @author DaPorkchop_
 */
public interface BetterScaledResolution extends Updateable<BetterScaledResolution> {
    /**
     * A {@link BetterScaledResolution} that does nothing at all, and can serve as a placeholder instead of {@code null}.
     */
    BetterScaledResolution NOOP = new BetterScaledResolution() {
        @Override
        public int width() {
            return 0;
        }

        @Override
        public int height() {
            return 0;
        }

        @Override
        public ScaledResolution getAsMinecraft() throws UnsupportedOperationException {
            throw new UnsupportedOperationException();
        }

        @Override
        public void update() {
        }
    };

    /**
     * @see ScaledResolution#getScaledWidth()
     */
    int width();

    /**
     * @see ScaledResolution#getScaledHeight()
     */
    int height();

    /**
     * @return this instance as a {@link ScaledResolution}
     * @throws UnsupportedOperationException if this isn't an instance of {@link ScaledResolution}
     */
    ScaledResolution getAsMinecraft() throws UnsupportedOperationException;

    @Override
    void update();
}
