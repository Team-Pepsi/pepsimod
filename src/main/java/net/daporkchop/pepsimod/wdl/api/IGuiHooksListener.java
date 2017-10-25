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

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.BlockPos;

public interface IGuiHooksListener extends IWDLMod {
    /**
     * Called when a block's GUI closes. <br/>
     * Note that the given position may not always be a block of the type for
     * the container. Double-check that it is. This can happen if there is lag
     * and the player looks away from the block before the GUI opens. It may
     * also be an entity's GUI that the player looked away from.
     *
     * @param world     The world the block is in.
     * @param pos       The position of the block. (Actually, the position that the
     *                  player was looking at when the GUI was opened, and may not be
     *                  the actual location of the block.)
     * @param container The container that the closing player had open.
     * @return Whether the given mod handled the event (if <code>false</code> is
     * returned, it'll be passed on to the next mod).
     */
    boolean onBlockGuiClosed(WorldClient world, BlockPos pos,
                             Container container);

    /**
     * Called when an entity's GUI closes. <br/>
     * Note that the given entity may not have been the one coresponding to the
     * entity. Double-check that it is. This can happen if there is lag and the
     * player looks at an entity before the GUI opens (or if an entity walks in
     * the way).
     *
     * @param world     The world the block is in.
     * @param entity    The entity whose GUI was closed.
     * @param container The container that the closing player had open.
     * @return Whether the given mod handled the event (if <code>false</code> is
     * returned, it'll be passed on to the next mod).
     */
    boolean onEntityGuiClosed(WorldClient world, Entity entity,
                              Container container);
}
