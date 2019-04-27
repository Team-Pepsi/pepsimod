/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2018 DaPorkchop_
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from DaPorkchop_.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original author of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
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
    public JsonObject json = new JsonObject();

    private GeneralTranslator() {

    }

    public void encode(JsonObject json) {
        for (Module module : ModuleManager.AVALIBLE_MODULES) {
            json.addProperty("module.enabled." + module.nameFull, module.state.toString());
        }
        json.addProperty("autoReconnect", this.autoReconnect);
        json.addProperty("sortType", this.sortType.ordinal());
    }

    public void decode(String fieldName, JsonObject json) {
        this.json = json;
        this.autoReconnect = this.getBoolean(json, "autoReconnect", this.autoReconnect);
        this.sortType = ModuleSortType.fromOrdinal(this.getInt(json, "sortType", this.sortType.ordinal()));
    }

    public ModuleState getState(String name, ModuleState fallback) {
        if (this.json.has("module.enabled." + name)) {
            return ModuleState.fromString(this.json.get("module.enabled." + name).getAsString());
        }

        return fallback;
    }

    public String name() {
        return "general";
    }

    public static class ModuleState {
        public static ModuleState DEFAULT = new ModuleState(false, false);

        public static ModuleState fromString(String from) {
            String[] split = from.split(" ");
            return new ModuleState(Boolean.parseBoolean(split[0]), Boolean.parseBoolean(split[1]));
        }
        public boolean enabled;
        public boolean hidden;

        public ModuleState(boolean a, boolean b) {
            this.enabled = a;
            this.hidden = b;
        }

        @Override
        public String toString() {
            return this.enabled + " " + this.hidden;
        }
    }
}
