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

import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;

/**
 * Interface for WDL mods that handle entities that have non-standard purposes. <br/>
 * This is for holograms and such that are vanilla minecraft entities being
 * (ab)used for new purposes. Use {@link IEntityAdder} for new entities.
 *
 * @deprecated Use {@link IEntityManager} instead
 */
@Deprecated
public interface ISpecialEntityHandler extends IWDLMod {
    /**
     * Gets the special entities handled by this mod.
     *
     * @return A map of vanilla entity name to special entity names. Key is the
     * vanilla entity, values are the overriding entities.
     */
    Multimap<String, String> getSpecialEntities();

    /**
     * Gets the name for the given special entity, if it is one. If it is not a
     * special entity handled by this mod, return <code>null</code>. <br/>
     * The given entity will always have its name be one of the keys in the map
     * from {@link #getSpecialEntities()}, and the return value should be in the
     * list from the value of that map for said key (or <code>null</code>).
     *
     * @param entity The entity to use.
     * @return The special entity name, or null if it is not a special entity
     * for this mod.
     */
    String getSpecialEntityName(Entity entity);

    /**
     * Gets the category for the given special entity. <br/>
     * The given name will always be in the list of values for one of the keys
     * of {@link #getSpecialEntities()}. <br/>
     * The category is used in the entities gui.
     *
     * @param name The name of the special entity.
     * @return The category.
     */
    String getSpecialEntityCategory(String name);

    /**
     * Gets the default track distance for the given entity.<br/>
     * The given name will always be in the list of values for one of the keys
     * of {@link #getSpecialEntities()}.
     *
     * @param name The name of the entity.
     * @return The track distance, or -1 to use the default one.
     */
    int getSpecialEntityTrackDistance(String name);
}
