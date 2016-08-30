package org.mcmonkey.denizen2core.commands.queuecommands;

import org.mcmonkey.denizen2core.commands.AbstractCommand;
import org.mcmonkey.denizen2core.commands.CommandEntry;
import org.mcmonkey.denizen2core.commands.CommandQueue;
import org.mcmonkey.denizen2core.tags.objects.QueueTag;

public class StopCommand extends AbstractCommand {

    // <--[command]
    // @Name stop
    // @Arguments [queue]
    // @Short stops the current queue, or the one specified if available.
    // @Updated 2016/08/11
    // @Group Queue
    // @Procedural true
    // @Minimum 0
    // @Maximum 1
    // @Description
    // Stops the current queue, or the one specified if available.
    // TODO: Explain more!
    // @Example
    // # This example echoes "hello".
    // - echo "hello"
    // - stop
    // - echo "This won't show"
    // @Example
    // # This example runs the task script "test", then stops it right away.
    // - run test
    // - stop <def[run_queue]>
    // -->

    @Override
    public String getName() {
        return "stop";
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
    public boolean isProcedural() {
        return true;
    }

    @Override
    public void execute(CommandQueue queue, CommandEntry entry) {
        if (!queue.procedural && entry.arguments.size() > 0) {
            QueueTag qid = QueueTag.getFor(queue.error, entry.getArgumentObject(queue, 0));
            CommandQueue q = qid.getInternal();
            if (queue.shouldShowGood()) {
                queue.outGood("Stopping queue: " + q.qID);
            }
            q.stop();
        }
        else {
            if (queue.shouldShowGood()) {
                queue.outGood("Stopping current queue.");
            }
            queue.stop();
        }
    }
}
