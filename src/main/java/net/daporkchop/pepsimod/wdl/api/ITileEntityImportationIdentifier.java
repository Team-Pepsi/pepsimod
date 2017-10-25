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

package net.daporkchop.pepsimod.wdl.api;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

/**
 * {@link IWDLMod} that helps identify "problematic" tile entities that need to
 * be imported from previously saved chunks (IE, tile entities that do not have
 * their data sent unless opened, such as chests).
 */
public interface ITileEntityImportationIdentifier extends IWDLMod {
    /**
     * Checks if the TileEntity should be imported. Only "problematic" (IE,
     * those that require manual interaction such as chests) TileEntities will
     * be imported. Additionally, the block at the tile entity's coordinates
     * must be one that would normally be used with that tile entity.
     *
     * @param entityID      The tile entity's ID, as found in the 'id' tag.
     * @param pos           The location of the tile entity, as created by its 'x', 'y',
     *                      and 'z' tags.
     * @param block         The block at the given position.
     * @param tileEntityNBT The full NBT tag of the existing tile entity. May be used if
     *                      further identification is needed.
     * @param chunk         The chunk for which entities are being imported. May be used
     *                      if further identification is needed (eg nearby blocks).
     * @return <code>true</code> if that tile entity should be imported.
     */
    boolean shouldImportTileEntity(String entityID, BlockPos pos,
                                   Block block, NBTTagCompound tileEntityNBT, Chunk chunk);
}
