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

package net.daporkchop.pepsimod.wdl;

import net.daporkchop.pepsimod.wdl.api.IEntityEditor;
import net.daporkchop.pepsimod.wdl.api.IWDLMod;
import net.daporkchop.pepsimod.wdl.api.IWDLModDescripted;
import net.minecraft.entity.Entity;

/**
 * Realigns entities to their serverside positions, to mitigate entity drift.
 * This is necessary for entities that move clientside, most importantly boats
 * (example: http://i.imgur.com/3QQchZL.gifv).
 * <br/>
 * This is also an example of how an {@link IWDLMod} would be implemented.
 */
public class EntityRealigner implements IEntityEditor, IWDLModDescripted {
    /**
     * Converts a position from the fixed-point version that a packet
     * (or {@link Entity#serverPosX} and the like use) into a double.
     *
     * @param serverPos
     * @return The double version of the position.
     * @see <a href="http://wiki.vg/Protocol#Fixed-point_numbers">
     * wiki.vg on Fixed-point numbers</a>
     */
    private static double convertServerPos(long serverPos) {
        return serverPos / 4096.0;
    }

    @Override
    public boolean isValidEnvironment(String version) {
        return true;
    }

    @Override
    public String getEnvironmentErrorMessage(String version) {
        return null;
    }

    @Override
    public String getDisplayName() {
        return "Entity realigner";
    }

    @Override
    public String getMainAuthor() {
        return "Pokechu22";
    }

    @Override
    public String[] getAuthors() {
        return null;
    }

    @Override
    public String getURL() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Realigns entities to their serverside position to deal with " +
                "entities that drift clientside (for example, boats).";
    }

    @Override
    public boolean shouldEdit(Entity e) {
        // We make sure that at least one of serverPosX, y, and
        // z is not 0 because an entity with a server pos of 0,
        // 0, 0 probably has a different way of setting up its
        // position (for example, paintings).
        // No sane entity will be at 0, 0, 0.  And moving them
        // to it can effectively delete entities - see
        // https://github.com/uyjulian/LiteModWDL/issues/4.
        // (I also think this is the cause for the "world going
        // invisible" issue).
        return e.serverPosX != 0 || e.serverPosY != 0 || e.serverPosZ != 0;
    }

    @Override
    public void editEntity(Entity e) {
        e.posX = convertServerPos(e.serverPosX);
        e.posY = convertServerPos(e.serverPosY);
        e.posZ = convertServerPos(e.serverPosZ);
    }
}
