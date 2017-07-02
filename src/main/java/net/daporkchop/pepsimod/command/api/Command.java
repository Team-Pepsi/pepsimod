package net.daporkchop.pepsimod.command.api;

import net.daporkchop.pepsimod.PepsiMod;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public abstract class Command {
    public String name;

    public Command(String name) {
        this.name = name;
    }

    public static void clientMessage(String toSend) {
        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(PepsiMod.chatPrefix + toSend));
    }

    public abstract void execute(String cmd, String[] args);

    public abstract String getSuggestion(String cmd, String[] args);
}
