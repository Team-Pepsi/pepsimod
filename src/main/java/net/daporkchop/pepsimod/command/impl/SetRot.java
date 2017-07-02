package net.daporkchop.pepsimod.command.impl;

import net.daporkchop.pepsimod.PepsiMod;
import net.daporkchop.pepsimod.command.api.Command;

public class SetRot extends Command {
    public SetRot() {
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
