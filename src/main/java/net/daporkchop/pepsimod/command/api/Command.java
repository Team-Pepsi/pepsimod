package net.daporkchop.pepsimod.command.api;

public abstract class Command {
    public String name;

    public Command(String name) {
        this.name = name;
    }

    public abstract void execute(String cmd, String[] args);

    public abstract String getSuggestion(String cmd, String[] args);
}
