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
