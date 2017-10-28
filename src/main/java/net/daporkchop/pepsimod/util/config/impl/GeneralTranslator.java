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

package net.daporkchop.pepsimod.util.config.impl;

import com.google.gson.JsonObject;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleSortType;
import net.daporkchop.pepsimod.util.config.IConfigTranslator;

import java.util.HashMap;

public class GeneralTranslator implements IConfigTranslator {
    public static final GeneralTranslator INSTANCE = new GeneralTranslator();
    public boolean autoReconnect = false;
    public HashMap<String, ModuleState> states = new HashMap<>();
    public ModuleSortType sortType = ModuleSortType.SIZE;

    private GeneralTranslator() {

    }

    public JsonObject json = new JsonObject();

    public void encode(JsonObject json) {
        for (Module module : ModuleManager.AVALIBLE_MODULES) {
            json.addProperty("module.enabled." + module.nameFull, module.state.toString());
        }
        json.addProperty("autoReconnect", autoReconnect);
        json.addProperty("sortType", sortType.ordinal());
    }

    public void decode(String fieldName, JsonObject json) {
        this.json = json;
        autoReconnect = getBoolean(json, "autoReconnect", autoReconnect);
        sortType = ModuleSortType.fromOrdinal(getInt(json, "sortType", sortType.ordinal()));
    }

    public ModuleState getState(String name, ModuleState fallback) {
        if (json.has("module.enabled." + name)) {
            return ModuleState.fromString(json.get("module.enabled." + name).getAsString());
        }

        return fallback;
    }

    public String name() {
        return "general";
    }

    public static class ModuleState {
        public static ModuleState DEFAULT = new ModuleState(false, false);
        public boolean enabled;
        public boolean hidden;

        public ModuleState(boolean a, boolean b) {
            enabled = a;
            hidden = b;
        }

        public static ModuleState fromString(String from) {
            String[] split = from.split(" ");
            return new ModuleState(Boolean.parseBoolean(split[0]), Boolean.parseBoolean(split[1]));
        }

        @Override
        public String toString() {
            return enabled + " " + hidden;
        }
    }
}
