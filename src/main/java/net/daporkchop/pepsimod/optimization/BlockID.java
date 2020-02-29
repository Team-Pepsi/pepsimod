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

package net.daporkchop.pepsimod.optimization;

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
