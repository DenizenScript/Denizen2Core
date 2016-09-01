package com.denizenscript.denizen2core.commands.queuecommands;

import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.tags.objects.QueueTag;

public class PauseCommand extends AbstractCommand {

    // <--[command]
    // @Name pause
    // @Arguments [queue]
    // @Short pauses the current queue, or the one specified if available.
    // @Updated 2016/08/12
    // @Group Queue
    // @Minimum 0
    // @Maximum 1
    // @Description
    // Pauses the current queue, or the one specified if available.
    // TODO: Explain more!
    // @Example
    // # This example pauses the queue.
    // - pause
    // @Example
    // # This example runs the task script "test", then pauses it right away.
    // - run test
    // - pause <def[run_queue]>
    // -->

    @Override
    public String getName() {
        return "pause";
    }

    @Override
    public String getArguments() {
        return "[queue]";
    }

    @Override
    public int getMinimumArguments() {
        return 0;
    }

    @Override
    public int getMaximumArguments() {
        return 1;
    }

    @Override
    public void execute(CommandQueue queue, CommandEntry entry) {
        if (entry.arguments.size() > 0) {
            QueueTag qid = QueueTag.getFor(queue.error, entry.getArgumentObject(queue, 0));
            CommandQueue q = qid.getInternal();
            if (queue.shouldShowGood()) {
                queue.outGood("Pausing queue: " + q.qID);
            }
            q.paused = true;
        }
        else {
            if (queue.shouldShowGood()) {
                queue.outGood("Pausing current queue.");
            }
            queue.paused = true;
        }
    }
}
