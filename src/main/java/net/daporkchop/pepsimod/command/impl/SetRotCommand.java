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

package net.daporkchop.pepsimod.command.impl;

import net.daporkchop.pepsimod.PepsiMod;
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
            PepsiMod.INSTANCE.mc.player.setPositionAndRotation(PepsiMod.INSTANCE.mc.player.posX, PepsiMod.INSTANCE.mc.player.posY, PepsiMod.INSTANCE.mc.player.posZ, yaw, pitch);
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
