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
import net.daporkchop.pepsimod.clickgui.ClickGUI;
import net.daporkchop.pepsimod.clickgui.Window;
import net.daporkchop.pepsimod.clickgui.api.IEntry;
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
                json.addProperty(window.text + "." + entry.getName() + ".open", entry.isOpen());
            }
        }
    }

    public void decode(String fieldName, JsonObject json) {
        for (Window window : ClickGUI.INSTANCE.windows) {
            window.setX(getInt(json, window.text + ".x", window.x));
            window.setY(getInt(json, window.text + ".y", window.y));
            window.setOpen(getBoolean(json, window.text + ".open", window.isOpen()));

            for (IEntry entry : window.entries) {
                entry.setOpen(getBoolean(json, window.text + "." + entry.getName() + ".open", entry.isOpen()));
            }
        }
    }

    public String name() {
        return "clickgui";
    }
}
