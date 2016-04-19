package org.mcmonkey.denizen2core.commands;

import org.mcmonkey.denizen2core.arguments.Argument;
import org.mcmonkey.denizen2core.arguments.TextArgumentBit;

import java.util.Arrays;
import java.util.List;

/**
 * Represents a Denizen2-enabled command.
 */
public abstract class AbstractCommand {

    public abstract String getName();

    public abstract String getArguments();

    public abstract int getMinimumArguments();

    public abstract int getMaximumArguments();

    public abstract boolean isWaitable();

    public CommandEntry GetFollower(CommandEntry entry) {
        Argument arg = new Argument();
        arg.addBit(new TextArgumentBit("\0CALLBACK", false));
        List<Argument> args = Arrays.asList(arg);
        CommandEntry ent = new CommandEntry(entry.scriptName, entry.command, args, entry.cmdName + " \0CALLBACK", entry.cmdName, false);
        ent.ownIndex = entry.blockEnd;
        ent.blockStart = entry.blockStart;
        ent.blockEnd = entry.blockEnd;
        return ent;
    }

    public abstract void execute(CommandQueue queue, CommandEntry entry);

    public void adaptBlockFollowers(CommandEntry entry, List<CommandEntry> input, List<CommandEntry> fblock) {
        input.add(GetFollower(entry));
    }

}
