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

import java.util.List;

/**
 * Interface for WDL mods that deal with new types of entities.
 * <br/>
 * This is for handling *new* entities that have names that aren't used in
 * vanilla minecraft.  Use {@link ISpecialEntityHandler} if you want to handle
 * using an entity for a non-intended purpose (eg holograms).
 *
 * @deprecated Use {@link IEntityManager} instead
 */
@Deprecated
public interface IEntityAdder extends IWDLMod {
    /**
     * Gets a List of all entities that this mod handles.
     *
     * @return A list of all the names of entities that this mod handles. Entity
     * names should be the ones that are registered with
     * {@link net.minecraft.entity.EntityList}.
     */
    List<String> getModEntities();

    /**
     * Gets the default track distance for the given entity. <br/>
     * This method will only ever be called with an entity in the list returned
     * by {@link #getModEntities()}.
     *
     * @param entity The name of the entity.
     * @return The default distance at which the entity is removed from the
     * player's view.
     */
    int getDefaultEntityTrackDistance(String entity);

    /**
     * Gets the category to put the given entity under. <br/>
     * It's recommended to put the mod's name here, such as "More creepers". If
     * the mod contains a lot of entities, you can divide it into subcategories.
     * This data used for the Entity gui.
     *
     * @param entity The name of the entity.
     * @return The category.
     */
    String getEntityCategory(String entity);
}
