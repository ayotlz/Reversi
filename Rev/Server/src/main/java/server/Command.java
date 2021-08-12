package server;

public enum Command {
    DOWN_CLIENT("down service");

    private final String commandName;

    Command(final String commandName) {
        this.commandName = commandName;
    }

    public String getCommand() {
        return commandName;
    }

    boolean equalCommand(final String message) {
        return commandName.equals(message);
    }
}
