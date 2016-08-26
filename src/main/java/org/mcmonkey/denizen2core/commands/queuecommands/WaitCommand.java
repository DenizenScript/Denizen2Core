package org.mcmonkey.denizen2core.commands.queuecommands;

import org.mcmonkey.denizen2core.commands.AbstractCommand;
import org.mcmonkey.denizen2core.commands.CommandEntry;
import org.mcmonkey.denizen2core.commands.CommandQueue;
import org.mcmonkey.denizen2core.tags.objects.NumberTag;

public class WaitCommand extends AbstractCommand {

    // <--[command]
    // @Name wait
    // @Arguments <duration>
    // @Short causes the current queue to wait for a duration.
    // @Updated 2016/04/30
    // @Authors mcmonkey
    // @Group Queue
    // @Minimum 1
    // @Maximum 1
    // @Description
    // Causes the current queue to wait for a duration.
    // TODO: Explain more!
    // @Example
    // # This example waits for 5 seconds.
    // - wait 5
    // -->

    @Override
    public String getName() {
        return "wait";
    }

    @Override
    public String getArguments() {
        return "<duration>";
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
    public void execute(CommandQueue queue, CommandEntry entry) {
        queue.setWait(NumberTag.getFor(queue::handleError, entry.getArgumentObject(queue, 0)).getInternal());
        if (queue.shouldShowGood()) {
            queue.outGood("Waiting for: " + queue.getWait() + " seconds!");
        }
    }
}
