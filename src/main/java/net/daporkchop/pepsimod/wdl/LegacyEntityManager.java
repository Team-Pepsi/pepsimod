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

import net.daporkchop.pepsimod.wdl.api.*;
import net.daporkchop.pepsimod.wdl.api.WDLApi.ModInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;

import java.util.HashSet;
import java.util.Set;

/**
 * {@link IEntityManager} implementation that manages instances of
 * {@link IEntityAdder} or {@link ISpecialEntityHandler}.
 */
@SuppressWarnings("deprecation")
public class LegacyEntityManager implements IEntityManager, IWDLModDescripted {
    public LegacyEntityManager() {
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
        return "Legacy entity API support";
    }

    @Override
    public String getMainAuthor() {
        return null;
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
        StringBuilder sb = new StringBuilder();
        sb.append("One or more extensions is using the deprecated entity API"
                + " (IEntityAdder or ISpecialEntityHandler).  This extension"
                + " is automatically added to provide compatability.");
        sb.append("\nImplementations of ISpecialEntityHandler:");
        for (ModInfo<ISpecialEntityHandler> info : WDLApi
                .getAllImplementingExtensions(ISpecialEntityHandler.class)) {
            sb.append('\n').append(info);
        }
        sb.append("\nImplementations of IEntityAdder:");
        for (ModInfo<IEntityAdder> info : WDLApi
                .getAllImplementingExtensions(IEntityAdder.class)) {
            sb.append('\n').append(info);
        }

        return sb.toString();
    }

    @Override
    public Set<String> getProvidedEntities() {
        Set<String> set = new HashSet<>();
        for (ModInfo<ISpecialEntityHandler> info : WDLApi
                .getImplementingExtensions(ISpecialEntityHandler.class)) {
            set.addAll(info.mod.getSpecialEntities().values());
        }
        for (ModInfo<IEntityAdder> info : WDLApi
                .getImplementingExtensions(IEntityAdder.class)) {
            set.addAll(info.mod.getModEntities());
        }

        return set;
    }

    @Override
    public String getIdentifierFor(Entity entity) {
        // This only provides old names, not minecraft:xxx names, but the old API
        // didn't support the new names anyways.
        String vanillaName = EntityList.getEntityString(entity);

        for (ModInfo<ISpecialEntityHandler> info : WDLApi
                .getImplementingExtensions(ISpecialEntityHandler.class)) {
            if (info.mod.getSpecialEntities().containsKey(vanillaName)) {
                return info.mod.getSpecialEntityName(entity);
            }
        }
        for (ModInfo<IEntityAdder> info : WDLApi
                .getImplementingExtensions(IEntityAdder.class)) {
            if (info.mod.getModEntities().contains(vanillaName)) {
                // Confirmed that one of the extensions uses that name.
                return vanillaName;
            }
        }
        return null;
    }

    @Override
    public int getTrackDistance(String identifier, Entity entity) {
        for (ModInfo<ISpecialEntityHandler> info : WDLApi
                .getImplementingExtensions(ISpecialEntityHandler.class)) {
            if (info.mod.getSpecialEntities().containsKey(identifier)) {
                return info.mod.getSpecialEntityTrackDistance(identifier);
            }
        }
        for (ModInfo<IEntityAdder> info : WDLApi
                .getImplementingExtensions(IEntityAdder.class)) {
            if (info.mod.getModEntities().contains(identifier)) {
                return info.mod.getDefaultEntityTrackDistance(identifier);
            }
        }
        return -1;
    }

    @Override
    public String getGroup(String identifier) {
        for (ModInfo<ISpecialEntityHandler> info : WDLApi
                .getImplementingExtensions(ISpecialEntityHandler.class)) {
            if (info.mod.getSpecialEntities().containsKey(identifier)) {
                return info.mod.getSpecialEntityCategory(identifier);
            }
        }
        for (ModInfo<IEntityAdder> info : WDLApi
                .getImplementingExtensions(IEntityAdder.class)) {
            if (info.mod.getModEntities().contains(identifier)) {
                return info.mod.getEntityCategory(identifier);
            }
        }
        return null;
    }

    @Override
    public String getDisplayIdentifier(String identifier) {
        return null;
    }

    @Override
    public String getDisplayGroup(String group) {
        return null;
    }

    @Override
    public boolean enabledByDefault(String identifier) {
        return true;
    }

}
