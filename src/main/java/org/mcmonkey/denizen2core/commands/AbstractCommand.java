package org.mcmonkey.denizen2core.commands;

/**
 * Represents a Denizen2-enabled command.
 */
public abstract class AbstractCommand {

    public abstract String getName();

    public abstract String getArguments();

    public abstract String getDescription();

    public abstract int getMinimumArguments();

    public abstract int getMaximumArguments();

    public abstract boolean isWaitable();

    public abstract void execute(CommandQueue queue, CommandEntry entry);
}
