package org.mcmonkey.denizen2core.commands.queuecommands;

import org.mcmonkey.denizen2core.commands.AbstractCommand;
import org.mcmonkey.denizen2core.commands.CommandEntry;
import org.mcmonkey.denizen2core.commands.CommandQueue;

public class StopCommand extends AbstractCommand {

    // <--[command]
    // @Name stop
    // @Arguments
    // @Short Stops the current queue.
    // @Updated 2016/08/11
    // @Authors mcmonkey
    // @Group Queue
    // @Minimum 0
    // @Maximum 0
    // @Description
    // Stops the current queue.
    // TODO: Explain more!
    // @Example
    // # This example echoes "hello".
    // - echo "hello"
    // - stop
    // - echo "This won't show"
    // -->

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getArguments() {
        return "";
    }

    @Override
    public int getMinimumArguments() {
        return 0;
    }

    @Override
    public int getMaximumArguments() {
        return 0;
    }

    @Override
    public boolean isWaitable() {
        return false;
    }

    @Override
    public void execute(CommandQueue queue, CommandEntry entry) {
        if (queue.shouldShowGood()) {
            queue.outGood("Stopping queue.");
        }
        queue.stop();
    }
}
