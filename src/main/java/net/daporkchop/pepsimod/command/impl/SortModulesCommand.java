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

package net.daporkchop.pepsimod.command.impl;

import net.daporkchop.pepsimod.command.api.Command;
import net.daporkchop.pepsimod.module.ModuleManager;
import net.daporkchop.pepsimod.module.api.ModuleSortType;

public class SortModulesCommand extends Command {
    public static final String[] MODES = new String[]{"alphabetical", "default", "size", "random"};

    public SortModulesCommand() {
        super("sortmodules");
    }

    @Override
    public void execute(String cmd, String[] args) {
        String resulttype = null;
        for (int i = 0; i < MODES.length; i++) {
            String toanalyze = MODES[i];
            if (toanalyze.startsWith(args[1])) {
                resulttype = toanalyze;
                break;
            }
        }
        if (resulttype == null) {
            clientMessage("Invalid type: " + args[1]);
            clientMessage("Valid types are: alphabetical, default, size, random");
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
    public String getSuggestion(String cmd, String[] args) {
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
