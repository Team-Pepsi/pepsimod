package net.daporkchop.pepsimod.command.impl;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.command.CommandRegistry;
import net.daporkchop.pepsimod.command.api.Command;
import net.minecraft.util.text.TextComponentString;

public class Help extends Command {
    public Help() {
        super("help");
    }

    @Override
    public void execute(String cmd, String[] args) {
        String toSend = PepsiMod.chatPrefix;
        for (Command command : CommandRegistry.COMMANDS) {
            toSend += command.name + ", ";
        }
        toSend = toSend.substring(0, toSend.length() - 2);
        PepsiMod.INSTANCE.mc.player.sendMessage(new TextComponentString(toSend));
    }

    @Override
    public String getSuggestion(String cmd, String[] args) {
        return ".help";
    }
}
