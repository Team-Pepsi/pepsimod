package net.daporkchop.pepsimod.command.impl;

import net.daporkchop.pepsimod.command.CommandRegistry;
import net.daporkchop.pepsimod.command.api.Command;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(String cmd, String[] args) {
        String toSend = "";
        for (Command command : CommandRegistry.COMMANDS) {
            toSend += command.name + ", ";
        }
        toSend = toSend.substring(0, toSend.length() - 2);
        clientMessage(toSend);
    }

    @Override
    public String getSuggestion(String cmd, String[] args) {
        return ".help";
    }
}
