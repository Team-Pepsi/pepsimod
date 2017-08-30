package net.daporkchop.pepsimod.module.impl.misc;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.module.api.ModuleOption;
import net.daporkchop.pepsimod.totally.not.skidded.clickgui.GuiClick;
import net.daporkchop.pepsimod.totally.not.skidded.clickgui.elements.Window;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class ClickGuiMod extends Module {
    public static ClickGuiMod INSTANCE;

    public ClickGuiMod(boolean isEnabled, int key, boolean hide) {
        super(isEnabled, "ClickGUI", key, hide);
    }

    @Override
    public void onEnable() {
        PepsiMod.INSTANCE.mc.displayGuiScreen(PepsiUtils.clickGui);
    }

    @Override
    public void onDisable() {

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
}
