package net.daporkchop.pepsimod.command.impl;

import net.daporkchop.pepsimod.command.api.Command;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.util.PepsiUtils;

public class List extends Command {
    public List() {
        super("list");
    }

    @Override
    public void execute(String cmd, String[] args) {
        String s = "";
        for (int i = 0; i < ModuleManager.AVALIBLE_MODULES.size(); i++) {
            s += ModuleManager.AVALIBLE_MODULES.get(i) + (i + 1 == ModuleManager.AVALIBLE_MODULES.size() ? "" : ", ");
        }
        clientMessage("Available modules: " + PepsiUtils.COLOR_ESCAPE + "o" + s);
    }

    @Override
    public String getSuggestion(String cmd, String[] args) {
        return ".list";
    }
}
