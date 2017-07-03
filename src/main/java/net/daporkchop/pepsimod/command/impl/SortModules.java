package net.daporkchop.pepsimod.command.impl;

import net.daporkchop.pepsimod.command.api.Command;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.ModuleSortType;

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
            clientMessage("Invalid type: " + args[1]);
            clientMessage("Valid types are: alphabetical, default, size, random");
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
            clientMessage("Sorted modules according to: \u00A7l" + resulttype);
        }
    }

    @Override
    public String getSuggestion(String cmd, String[] args)  {
        switch (args.length) {
            case 1:
                return ".sortmodules " + MODES[0];
            case 2:
                if (args[1].isEmpty()) {
                    return ".sortmodules " + MODES[0];
                }
                for (String mode : MODES) {
                    if (mode.startsWith(args[1])) {
                        return ".sortmodules " + mode;
                    }
                }
        }

        return ".sortmodules";
    }
}
