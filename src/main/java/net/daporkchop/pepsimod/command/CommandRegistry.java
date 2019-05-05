/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017-2019 DaPorkchop_
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from DaPorkchop_.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original author of the project (IE: DaPorkchop_), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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

            mc.player.sendMessage(new TextComponentString(Pepsimod.chatPrefix + PepsiUtils.COLOR_ESCAPE + "cUnknown command! Use .help for a list of commands!"));
        } catch (ArrayIndexOutOfBoundsException | StringIndexOutOfBoundsException e) {
        }
    }
}
