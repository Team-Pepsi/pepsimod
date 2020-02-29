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
import net.daporkchop.pepsimod.module.api.Module;
import net.daporkchop.pepsimod.util.PepsiUtils;

public class ToggleCommand extends Command {
    public ToggleCommand() {
        super("toggle");
    }

    @Override
    public void execute(String cmd, String[] args) {
        if (args.length < 2) {
            String s = "";
            for (int i = 0; i < ModuleManager.AVALIBLE_MODULES.size(); i++) {
                s += ModuleManager.AVALIBLE_MODULES.get(i).name + (i + 1 == ModuleManager.AVALIBLE_MODULES.size() ? "" : ", ");
            }
            clientMessage("Available modules: " + PepsiUtils.COLOR_ESCAPE + "o" + s);
            return;
        }
        Module module = ModuleManager.getModuleByName(args[1]);
        if (module == null) {
            clientMessage("No module was found by the name: " + PepsiUtils.COLOR_ESCAPE + "o" + args[1]);
        } else {
            ModuleManager.toggleModule(module);
            clientMessage("Toggled module: " + PepsiUtils.COLOR_ESCAPE + "o" + module.name);
        }
    }

    @Override
    public String getSuggestion(String cmd, String[] args) {
        switch (args.length) {
            case 1:
                return ".toggle " + ModuleManager.AVALIBLE_MODULES.get(0).name;
            case 2:
                if (args[1].isEmpty()) {
                    return ".toggle " + ModuleManager.AVALIBLE_MODULES.get(0).name;
                }
                for (Module module : ModuleManager.AVALIBLE_MODULES) {
                    if (module.name.startsWith(args[1])) {
                        return ".toggle " + module.name;
                    }
                }
                return "";
        }

        return ".toggle";
    }
}
