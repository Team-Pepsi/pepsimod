/*
 * Adapted from The MIT License (MIT)
 *
 * Copyright (c) 2016-2020 DaPorkchop_
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * Any persons and/or organizations using this software must include the above copyright notice and this permission notice,
 * provide sufficient credit to the original authors of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
