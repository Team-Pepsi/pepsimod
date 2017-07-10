package net.daporkchop.pepsimod.command.impl;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.command.api.Command;

public class SaveCommand extends Command {
    public SaveCommand() {
        super("save");
    }

    @Override
    public void execute(String cmd, String[] args) {
        PepsiMod.INSTANCE.saveConfig();
        clientMessage("Saved config!");
    }

    @Override
    public String getSuggestion(String cmd, String[] args) {
        return ".save";
    }
}
