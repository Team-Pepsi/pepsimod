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

package net.daporkchop.pepsimod.module.impl.misc;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.clickgui.ClickGUI;
import net.daporkchop.pepsimod.clickgui.Window;
import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class ClickGuiMod extends Module {
    public static ClickGuiMod INSTANCE;

    public ClickGuiMod(boolean isEnabled, int key, boolean hide) {
        super(isEnabled, "ClickGUI", key, true);
    }

    @Override
    public void onEnable() {
        if (PepsiMod.INSTANCE.isInitialized) {
            for (Window window : ClickGUI.INSTANCE.windows) {
                window.openGui();
            }

            mc.displayGuiScreen(ClickGUI.INSTANCE);
        }
    }

    @Override
    public void onDisable() {
        if (mc.currentScreen instanceof ClickGUI) {
            mc.displayGuiScreen(null);
        }
    }

    @Override
    public void tick() {

    }

    @Override
    public void init() {
        INSTANCE = this;
    }

    @Override
    public ModuleOption[] getDefaultOptions() {
        return new ModuleOption[0];
    }

    @Override
    public void registerKeybind(String name, int key)   {
        this.keybind = new KeyBinding("\u00A7cOpen ClickGUI", Keyboard.KEY_RSHIFT, "key.categories.pepsimod");
        ClientRegistry.registerKeyBinding(this.keybind);
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.MISC;
    }
}
