package net.daporkchop.pepsimod.command.impl;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.command.api.Command;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.minecraft.util.text.TextComponentString;

public class Toggle extends Command {
    public Toggle() {
        super("toggle");
    }

    @Override
    public void execute(String cmd, String[] args) {
        if (args.length < 2)    {
            PepsiMod.INSTANCE.mc.player.sendMessage(new TextComponentString(PepsiMod.chatPrefix + "Usage: .toggle <moduleName>"));
            return;
        }
        Module module = ModuleManager.getModuleByName(args[1]);
        if (module == null) {
            PepsiMod.INSTANCE.mc.player.sendMessage(new TextComponentString(PepsiMod.chatPrefix + "No module was found by the name: " + PepsiUtils.COLOR_ESCAPE + "o" + args[1]));
        } else {
            ModuleManager.toggleModule(module);
            PepsiMod.INSTANCE.mc.player.sendMessage(new TextComponentString(PepsiMod.chatPrefix + "Toggled module: " + PepsiUtils.COLOR_ESCAPE + "o" + module.name));
        }
    }

    @Override
    public String getSuggestion(String cmd, String[] args) {
        switch (args.length) {
            case 1:
                return ".toggle " + ModuleManager.AVALIBLE_MODULES.get(0).name;
            case 2:
                for (Module module : ModuleManager.AVALIBLE_MODULES)    {
                    if (module.name.startsWith(args[1]))    {
                        return ".toggle " + module.name;
                    }
                }
                return "";
        }

        return ".toggle";
    }
}
