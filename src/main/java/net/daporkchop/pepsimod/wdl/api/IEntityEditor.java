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

import net.minecraft.entity.Entity;

/**
 * Something that can edit entities as they are being saved.
 */
public interface IEntityEditor extends IWDLMod {
    /**
     * Should the given entity be edited by this {@link IEntityEditor}?
     * <p>
     * A simple implementation may just use a <code>instanceof</code> check, but
     * more fancy things can be done.
     *
     * @param e The entity to check.
     * @return Whether it should be edited.
     */
    boolean shouldEdit(Entity e);

    /**
     * Edit the given tile entity. Will only be called if
     * {@link #shouldEdit(Entity)} returned true. This entity should be modified
     * "in-place".
     *
     * @param e The entity to edit.
     */
    void editEntity(Entity e);
}
