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

import com.google.common.collect.ImmutableMap;
import net.daporkchop.pepsimod.wdl.*;
import net.daporkchop.pepsimod.wdl.api.WDLApi.ModInfo;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link WDLApi.APIInstance} implementation.
 */
class APIImpl implements WDLApi.APIInstance {
    private static final Logger LOGGER = LogManager.getLogger();

    private static Map<String, ModInfo<?>> wdlMods = new HashMap<>();

    private static boolean hasLegacyEntityHandler = false;

    static {
        LOGGER.debug("Setting instance");
        WDLApi.APIInstance instance = new APIImpl();
        WDLApi.setInstance(instance);
        LOGGER.debug("Loading default WDL extensions");
        // Don't do this statically to avoid problems
        instance.addWDLMod("Hologram", "2.0", new HologramHandler());
        instance.addWDLMod("EntityRealigner", "1.0", new EntityRealigner());
    }

    /**
     * Writes out the current stacktrace to the logger in warn mode.
     */
    private static void logStackTrace() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (StackTraceElement e : elements) {
            LOGGER.warn(e.toString());
        }
    }

    @Override
    public void saveTileEntity(BlockPos pos, TileEntity te) {
        if (!WDLPluginChannels.canSaveTileEntities(pos.getX() << 16,
                pos.getZ() << 16)) {
            LOGGER.warn("API attempted to call saveTileEntity when " +
                    "saving TileEntities is not allowed!  Pos: " + pos +
                    ", te: " + te + ".  StackTrace: ");
            logStackTrace();

            return;
        }

        WDL.saveTileEntity(pos, te);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void addWDLMod(String id, String version, IWDLMod mod) {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null!  (mod="
                    + mod + ", version=" + version + ")");
        }
        if (version == null) {
            throw new IllegalArgumentException("version must not be null!  "
                    + "(mod=" + mod + ", id=" + version + ")");
        }
        if (mod == null) {
            throw new IllegalArgumentException("mod must not be null!  " +
                    "(id=" + id + ", version=" + version + ")");
        }

        ModInfo<IWDLMod> info = new ModInfo<>(id, version, mod);
        if (wdlMods.containsKey(id)) {
            throw new IllegalArgumentException("A mod by the name of '"
                    + id + "' is already registered by "
                    + wdlMods.get(id) + " (tried to register "
                    + info + " over it)");
        }
        if (!mod.isValidEnvironment(VersionConstants.getModVersion())) {
            String errorMessage = mod
                    .getEnvironmentErrorMessage(VersionConstants
                            .getModVersion());
            if (errorMessage != null) {
                throw new IllegalArgumentException(errorMessage);
            } else {
                throw new IllegalArgumentException("Environment for " + info
                        + " is incorrect!  Perhaps it is for a different"
                        + " version of WDL?  You are running "
                        + VersionConstants.getModVersion() + ".");
            }
        }

        wdlMods.put(id, info);

        // IMessageAdder doesn't seem possible to do dynamically
        if (mod instanceof IMessageTypeAdder) {
            Map<String, IWDLMessageType> types =
                    ((IMessageTypeAdder) mod).getMessageTypes();

            ModMessageTypeCategory category = new ModMessageTypeCategory(info);

            for (Map.Entry<String, IWDLMessageType> e : types.entrySet()) {
                WDLMessages.registerMessage(e.getKey(), e.getValue(), category);
            }
        }
        // Needs callback for legacy interfaces
        if (!hasLegacyEntityHandler
                && (mod instanceof IEntityAdder || mod instanceof ISpecialEntityHandler)) {
            addWDLMod("LegacyEntitySupport", "1.0", new LegacyEntityManager());
        }
    }

    @Override
    public <T extends IWDLMod> List<ModInfo<T>> getImplementingExtensions(
            Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz must not be null!");
        }
        List<ModInfo<T>> returned = new ArrayList<>();

        for (ModInfo<?> info : wdlMods.values()) {
            if (!info.isEnabled()) {
                continue;
            }

            if (clazz.isAssignableFrom(info.mod.getClass())) {
                // We know the actual type of the given mod is correct,
                // so it's safe to do this cast.
                @SuppressWarnings("unchecked")
                ModInfo<T> infoCasted = (ModInfo<T>) info;
                returned.add(infoCasted);
            }
        }

        return returned;
    }

    @Override
    public <T extends IWDLMod> List<ModInfo<T>> getAllImplementingExtensions(
            Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("clazz must not be null!");
        }
        List<ModInfo<T>> returned = new ArrayList<>();

        for (ModInfo<?> info : wdlMods.values()) {
            if (clazz.isAssignableFrom(info.mod.getClass())) {
                // We know the actual type of the given mod is correct,
                // so it's safe to do this cast.
                @SuppressWarnings("unchecked")
                ModInfo<T> infoCasted = (ModInfo<T>) info;
                returned.add(infoCasted);
            }
        }

        return returned;
    }

    @Override
    public Map<String, ModInfo<?>> getWDLMods() {
        return ImmutableMap.copyOf(wdlMods);
    }

    @Override
    public String getModInfo(String name) {
        if (!wdlMods.containsKey(name)) {
            return null;
        }

        return wdlMods.get(name).getInfo();
    }

    @Override
    public boolean isEnabled(String modID) {
        return WDL.globalProps.getProperty("Extensions." + modID + ".enabled",
                "true").equals("true");
    }

    @Override
    public void setEnabled(String modID, boolean enabled) {
        WDL.globalProps.setProperty("Extensions." + modID + ".enabled",
                Boolean.toString(enabled));
        WDL.saveGlobalProps();
    }

    /**
     * Implementation of {@link MessageTypeCategory} for {@link IWDLMod}s.
     */
    private static class ModMessageTypeCategory extends MessageTypeCategory {
        private ModInfo<?> mod;

        public ModMessageTypeCategory(ModInfo<?> mod) {
            super(mod.id);
        }

        @Override
        public String getDisplayName() {
            return mod.getDisplayName();
        }
    }
}
