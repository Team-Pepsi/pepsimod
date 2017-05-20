package net.daporkchop.pepsimod.command;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.command.api.Command;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.minecraft.util.text.TextComponentString;

import java.util.ArrayList;

public class CommandRegistry {
    /**
     * All registered commands are here :P
     */
    public static final ArrayList<Command> COMMANDS = new ArrayList<>();

    /**
     * Gets a completion suggestion for a command
     *
     * @param input the current text input
     * @return a suggestion, or "" if nothing could be recommended
     */
    public static String getSuggestionFor(String input) {
        if (input.length() == 1) {
            return "." + COMMANDS.get(0).name;
        }
        String[] split = input.split(" ");
        try {
            String commandName = split[0].substring(1);
            for (Command command : COMMANDS) {
                if (command.name.equals(commandName)) {
                    return command.getSuggestion(input, split);
                }
            }

            for (Command command : COMMANDS) {
                if (command.name.startsWith(commandName)) {
                    return "." + command.name;
                }
            }
        } catch (StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e) {
            return "." + COMMANDS.get(0).name;
        }
        return "";
    }

    /**
     * Registers a command.
     *
     * @param command the command to register
     */
    public static void registerCommand(Command command) {
        if (!COMMANDS.contains(command)) {
            COMMANDS.add(command);
        }
    }

    /**
     * Runs a command
     *
     * @param command the command given in chat
     */
    public static void runCommand(String command) {
        try {
            String split[] = command.split(" "), commandName = split[0].substring(1);
            for (Command cmd : COMMANDS) {
                if (cmd.name.equals(commandName)) {
                    cmd.execute(command, split);
                    return;
                }
            }
            PepsiMod.INSTANCE.mc.player.sendMessage(new TextComponentString(PepsiMod.chatPrefix + PepsiUtils.COLOR_ESCAPE + "cUnknown command! Use .help for a list of commands!"));
        } catch (ArrayIndexOutOfBoundsException | StringIndexOutOfBoundsException e) {

        }
    }
}
