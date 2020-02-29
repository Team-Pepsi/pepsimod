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
import net.daporkchop.pepsimod.gui.clickgui.ClickGUI;
import net.daporkchop.pepsimod.gui.clickgui.Window;
import net.daporkchop.pepsimod.gui.clickgui.api.IEntry;
import net.daporkchop.pepsimod.util.config.IConfigTranslator;

public class ClickGUITranslator implements IConfigTranslator {
    public static final ClickGUITranslator INSTANCE = new ClickGUITranslator();

    private ClickGUITranslator() {

    }

    public void encode(JsonObject json) {
        for (Window window : ClickGUI.INSTANCE.windows) {
            json.addProperty(window.text + ".x", window.x);
            json.addProperty(window.text + ".y", window.y);
            json.addProperty(window.text + ".open", window.isOpen());

            for (IEntry entry : window.entries) {
                json.addProperty(window.text + '.' + entry.getName() + ".open", entry.isOpen());
            }
        }
    }

    public void decode(String fieldName, JsonObject json) {
        for (Window window : ClickGUI.INSTANCE.windows) {
            window.setX(this.getInt(json, window.text + ".x", window.x));
            window.setY(this.getInt(json, window.text + ".y", window.y));
            window.setOpen(this.getBoolean(json, window.text + ".open", window.isOpen()));

            for (IEntry entry : window.entries) {
                entry.setOpen(this.getBoolean(json, window.text + '.' + entry.getName() + ".open", entry.isOpen()));
            }
        }
    }

    public String name() {
        return "clickgui";
    }
}
