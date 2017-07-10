package net.daporkchop.pepsimod.command.impl;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.command.api.Command;

public class LoadCommand extends Command {
    public LoadCommand() {
        super("load");
    }

    @Override
    public void execute(String cmd, String[] args) {
        PepsiMod.INSTANCE.saveConfig();
        clientMessage("Loaded config!");
    }

    @Override
    public String getSuggestion(String cmd, String[] args) {
        return ".load";
    }
}
