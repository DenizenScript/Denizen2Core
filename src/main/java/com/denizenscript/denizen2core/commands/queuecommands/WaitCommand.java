package com.denizenscript.denizen2core.commands.queuecommands;

import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.tags.objects.DurationTag;

public class WaitCommand extends AbstractCommand {

    // <--[command]
// @Since 0.3.0
    // @Name wait
    // @Arguments <duration>
    // @Short causes the current queue to wait for a duration.
    // @Updated 2016/04/30
    // @Group Queue
    // @Minimum 1
    // @Maximum 1
    // @Description
    // Causes the current queue to wait for a duration.
    // TODO: Explain more!
    // @Example
    // # This example waits for 5 seconds.
    // - wait 5
    // @Example
    // # This example waits for three and a half minutes.
    // - wait 3.5m
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
        queue.setWait(DurationTag.getFor(queue.error, entry.getArgumentObject(queue, 0)).seconds());
        if (queue.shouldShowGood()) {
            queue.outGood("Waiting for: " + queue.getWait() + " seconds!");
        }
    }
}
