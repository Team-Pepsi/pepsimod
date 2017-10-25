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

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.math.BlockPos;

/**
 * Something that can edit tile entities as they are being saved.
 */
public interface ITileEntityEditor extends IWDLMod {
    /**
     * Should the given tile entity be edited by this {@link ITileEntityEditor}?
     * <p>
     * To get the type of the entity, look at the "id" String tag. See
     * {@link TileEntity}'s static initializer block for a list of tile entity
     * ids.
     * <p>
     * Example:
     * <p>
     * <pre>
     * String type = compound.{@link NBTTagCompound#getString(String) getString("id")}
     *
     * // "Trap" is dispenser
     * if (type.equals("Trap") || type.equals("Dropper")) {
     *     return true;
     * }
     *
     * return false;
     * </pre>
     *
     * @param pos          The location of the tile entity in the world.
     * @param compound     The tile entity to check.
     * @param creationMode How the tile entity was created.
     * @return Whether it should be edited.
     */
    boolean shouldEdit(BlockPos pos, NBTTagCompound compound,
                       TileEntityCreationMode creationMode);

    /**
     * Edit the given tile entity. Will only be called if
     * {@link #shouldEdit(NBTTagCompound, TileEntityCreationMode)} returned
     * true.
     * <p>
     * The given NBT tag must be edited in-place.
     * <p>
     * If you want to work with a TileEntity object instead of a
     * {@link NBTTagCompound}, you can serialize and deserialize it:
     * <p>
     * <pre>
     * {@link TileEntity} te = {@link TileEntity#readFromNBT(NBTTagCompound) TileEntity.readFromNBT(compound)};
     *
     * if (te instanceof {@link TileEntityDispenser}) {
     *     TileEntityDispenser dispenser = (TileEntityDispenser)te;
     *
     *     for (int i = 0; i < dispenser.{@link TileEntityDispenser#getSizeInventory() getSizeInventory()}; i++) {
     *         dispenser.{@link TileEntityDispenser#setInventorySlotContents(int, net.minecraft.item.ItemStack) setInventorySlotContents}(i, new {@link ItemStack}({@link Blocks#tnt}, 64));
     *     }
     *
     *     dispenser.{@link TileEntity#writeToNBT(NBTTagCompound) writeToNBT(compound)};
     * }
     * </pre>
     *
     * @param pos          The location of the tile entity in the world
     * @param compound     The tile entity to edit.
     * @param creationMode How the tile entity was created.
     */
    void editTileEntity(BlockPos pos, NBTTagCompound compound,
                        TileEntityCreationMode creationMode);

    /**
     * Identifies how the tile entity was created/loaded.
     */
    enum TileEntityCreationMode {
        /**
         * The tile entity was imported from an older version of the chunk.
         */
        IMPORTED,
        /**
         * The tile entity already existed in the chunk (for instance, signs and
         * player skulls).
         */
        EXISTING,
        /**
         * The tile entity was manually saved, generally by having its GUI
         * manually opened.
         */
        NEW
    }
}
