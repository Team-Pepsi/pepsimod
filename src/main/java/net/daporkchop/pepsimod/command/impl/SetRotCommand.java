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

public class SetRotCommand extends Command {
    public SetRotCommand() {
        super("setrot");
    }

    @Override
    public void execute(String cmd, String[] args) {
        if (args.length < 3) {
            clientMessage("Usage: .setrot <yaw> <pitch>");
            return;
        }

        try {
            float yaw = Float.parseFloat(args[1]);
            float pitch = Float.parseFloat(args[2]);
            mc.player.setPositionAndRotation(mc.player.posX, mc.player.posY, mc.player.posZ, yaw, pitch);
            clientMessage("Set rotation to yaw: " + yaw + " pitch: " + pitch);
        } catch (NumberFormatException e) {
            clientMessage("Invalid arguemnts!");
        }
    }

    @Override
    public String getSuggestion(String cmd, String[] args) {
        switch (args.length) {
            case 1:
                return ".setrot 0 0";
            case 2:
                return ".setrot " + args[1] + (args[1].length() == 0 ? "0 0" : " 0");
        }
        return ".setrot";
    }
}
