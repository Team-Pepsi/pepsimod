/*
 * Adapted from the Wizardry License
 *
 * Copyright (c) 2017 Team Pepsi
 *
 * Permission is hereby granted to any persons and/or organizations using this software to copy, modify, merge, publish, and distribute it.
 * Said persons and/or organizations are not allowed to use the software or any derivatives of the work for commercial use or any other means to generate income, nor are they allowed to claim this software as their own.
 *
 * The persons and/or organizations are also disallowed from sub-licensing and/or trademarking this software without explicit permission from Team Pepsi.
 *
 * Any persons and/or organizations using this software must disclose their source code and have it publicly available, include this license, provide sufficient credit to the original authors of the project (IE: Team Pepsi), as well as provide a link to the original project.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
