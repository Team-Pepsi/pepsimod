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

package net.daporkchop.pepsimod.module.impl.misc;

import net.daporkchop.pepsimod.gui.clickgui.ClickGUI;
import net.daporkchop.pepsimod.gui.clickgui.Window;
import net.daporkchop.pepsimod.module.ModuleCategory;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class ClickGuiMod extends Module {
    public static ClickGuiMod INSTANCE;

    {
        INSTANCE = this;
    }

    public ClickGuiMod(boolean isEnabled, int key) {
        super(isEnabled, "ClickGUI", key, true);
    }

    @Override
    public void onEnable() {
        if (pepsimod.isInitialized) {
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
    public void registerKeybind(String name, int key) {
        this.keybind = new KeyBinding("\u00A7cOpen ClickGUI", Keyboard.KEY_RSHIFT, "key.categories.pepsimod");
        ClientRegistry.registerKeyBinding(this.keybind);
    }

    public ModuleCategory getCategory() {
        return ModuleCategory.MISC;
    }
}
