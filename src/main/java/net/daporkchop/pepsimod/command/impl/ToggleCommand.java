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
