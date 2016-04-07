package org.mcmonkey.denizen2core.commands.queuecommands;

import org.mcmonkey.denizen2core.commands.AbstractCommand;
import org.mcmonkey.denizen2core.commands.CommandEntry;
import org.mcmonkey.denizen2core.commands.CommandQueue;

public class MarkCommand extends AbstractCommand {

    // <--[command]
    // @Name mark
    // @Arguments <name>
    // @Short marks a location for the goto command.
    // @Updated 2016/04/06
    // @Authors mcmonkey
    // @Group Queue
    // @Minimum 1
    // @Maximum 1
    // @Description
    // Marks a location for the goto command.
    // TODO: Explain more!
    // @Example
    // # This example marks the location 'test'.
    // - mark test
    // @Tags
    // None.
    // -->

    @Override
    public String getName() {
        return "mark";
    }

    @Override
    public String getArguments() {
        return "<name>";
    }

    @Override
    public String getDescription() {
        return "Marks a location for the goto command.";
    }

    @Override
    public int getMinimumArguments() {
        return 1;
    }

    @Override
    public int getMaximumArguments() {
        return 1;
    }

    @Override
    public boolean isWaitable() {
        return false;
    }

    @Override
    public void execute(CommandQueue queue, CommandEntry entry) {
        if (queue.shouldShowGood()) {
            queue.outGood("Passing mark...");
        }
    }
}
