package net.daporkchop.pepsimod.command.impl;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.command.api.Command;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.ModuleSortType;
import net.minecraft.util.text.TextComponentString;

public class SortModules extends Command {
    public static final String[] MODES = new String[] {"alphabetical", "default", "size", "random"};

    public SortModules()    {
        super("sortmodules");
    }

    @Override
    public void execute(String cmd, String[] args) {
        String resulttype = null;
        for (int i = 0; i < MODES.length; i++)  {
            String toanalyze = MODES[i];
            if (toanalyze.startsWith(args[1]))  {
                resulttype = toanalyze;
                break;
            }
        }
        if (resulttype == null) {
            PepsiMod.INSTANCE.mc.player.sendMessage(new TextComponentString(PepsiMod.chatPrefix + "Invalid type: " + args[1]));
            PepsiMod.INSTANCE.mc.player.sendMessage(new TextComponentString(PepsiMod.chatPrefix + "Valid types are: alphabetical, default, size, random"));
            return;
        } else {
            switch (resulttype) {
                case "alphabetical":
                    ModuleManager.sortModules(ModuleSortType.ALPHABETICAL);
                    break;
                case "default":
                    ModuleManager.sortModules(ModuleSortType.DEFAULT);
                    break;
                case "size":
                    ModuleManager.sortModules(ModuleSortType.SIZE);
                    break;
                case "random":
                    ModuleManager.sortModules(ModuleSortType.RANDOM);
                    break;
            }
            PepsiMod.INSTANCE.mc.player.sendMessage(new TextComponentString(PepsiMod.chatPrefix + "Sorted modules according to: " + resulttype));
        }
    }

    @Override
    public String getSuggestion(String cmd, String[] args)  {
        return "";
    }
}
