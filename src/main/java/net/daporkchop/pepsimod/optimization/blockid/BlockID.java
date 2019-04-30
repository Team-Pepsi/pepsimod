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

package net.daporkchop.pepsimod.optimization.blockid;

import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

/**
 * Injected into {@link net.minecraft.block.Block} at runtime for fast access to block IDs.
 *
 * @author DaPorkchop_
 */
public interface BlockID {
    /**
     * A lookup table of block IDs to block instances.
     */
    List<Block> BLOCK_LOOKUP = new ArrayList<>(); //basically as fast as it gets

    /**
     * Gets the numeric ID of this block.
     *
     * @return this block's ID
     */
    int getBlockId();

    /**
     * Sets the cached numeric ID of this block.
     * <p>
     * Only used internally, don't touch!
     *
     * @param id the new ID
     */
    void internal_setBlockId(int id);
}
