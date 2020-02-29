/*
 * Adapted from The MIT License (MIT)
 *
 * Copyright (c) 2016-2020 DaPorkchop_
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * Any persons and/or organizations using this software must include the above copyright notice and this permission notice,
 * provide sufficient credit to the original authors of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package net.daporkchop.pepsimod.command;

import net.daporkchop.pepsimod.Pepsimod;
import net.daporkchop.pepsimod.command.api.Command;
import net.daporkchop.pepsimod.util.PepsiConstants;
import net.daporkchop.pepsimod.util.PepsiUtils;
import net.minecraft.util.text.TextComponentString;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry extends PepsiConstants {
    /**
     * All registered commands are here :P
     */
    public static HashMap<String, Command> commandNames = new HashMap<>();

    /**
     * Gets a completion suggestion for a command
     *
     * @param input the current text input
     * @return a suggestion, or "" if nothing could be recommended
     */
    public static String getSuggestionFor(String input) {
        if (input.length() == 1) {
            return "." + commandNames.values().iterator().next().name;
        }
        String[] split = input.replace(" ", " \u0000").split("\\s+");
        for (int i = split.length - 1; i >= 0; i--) {
            split[i] = split[i].replace("\u0000", "");
        }
        try {
            String commandName = split[0].substring(1);
            Command command = commandNames.get(commandName);
            if (command != null) {
                return command.getSuggestion(input, split);
            }

            for (Map.Entry<String, Command> entry : commandNames.entrySet()) {
                if (entry.getKey().startsWith(commandName)) {
                    return "." + entry.getKey();
                }
            }
        } catch (StringIndexOutOfBoundsException e) {
        }
        return "";
    }

    /**
     * Registers a command.
     *
     * @param command the command to register
     */
    public static void registerCommand(Command command) {
        if (!commandNames.values().contains(command)) {
            for (String s : command.aliases()) {
                commandNames.put(s, command);
            }
        }
    }

    public static void registerCommands(Command... toRegister) {
        for (Command command : toRegister)  {
            registerCommand(command);
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
            for (Map.Entry<String, Command> entry : commandNames.entrySet()) {
                if (entry.getKey().equals(commandName)) {
                    entry.getValue().execute(command, split);
                    return;
                }
            }

            mc.player.sendMessage(new TextComponentString(Pepsimod.CHAT_PREFIX + PepsiUtils.COLOR_ESCAPE + "cUnknown command! Use .help for a list of commands!"));
        } catch (ArrayIndexOutOfBoundsException | StringIndexOutOfBoundsException e) {
        }
    }
}
