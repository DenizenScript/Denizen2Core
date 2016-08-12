package org.mcmonkey.denizen2core.commands.queuecommands;

import org.mcmonkey.denizen2core.commands.AbstractCommand;
import org.mcmonkey.denizen2core.commands.CommandEntry;
import org.mcmonkey.denizen2core.commands.CommandQueue;
import org.mcmonkey.denizen2core.tags.objects.QueueTag;

public class ResumeCommand extends AbstractCommand {

    // <--[command]
    // @Name resume
    // @Arguments <queue>
    // @Short resumes the specified queue.
    // @Updated 2016/08/12
    // @Authors mcmonkey
    // @Group Queue
    // @Minimum 1
    // @Maximum 1
    // @Description
    // Resumes the specified queue.
    // TODO: Explain more!
    // @Example
    // # This example runs the task script "test", then resumes it right away.
    // - run test
    // - resume <def[run_queue]>
    // -->

    @Override
    public String getName() {
        return "resume";
    }

    @Override
    public String getArguments() {
        return "[queue]";
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
        QueueTag qid = QueueTag.getFor(queue.error, entry.getArgumentObject(queue, 0));
        CommandQueue q = qid.getInternal();
        if (queue.shouldShowGood()) {
            queue.outGood("Resuming queue: " + q.qID);
        }
        q.paused = false;
    }
}
