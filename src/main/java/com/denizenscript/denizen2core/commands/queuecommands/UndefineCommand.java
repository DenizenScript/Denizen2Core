package com.denizenscript.denizen2core.commands.queuecommands;

import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.tags.objects.QueueTag;
import com.denizenscript.denizen2core.utilities.debugging.ColorSet;

public class UndefineCommand extends AbstractCommand {

    // <--[command]
    // @Name undefine
    // @Arguments <definition> [queue]
    // @Short Defines a value on the current queue.
    // @Updated 2017/02/16
    // @Group Queue
    // @Procedural true
    // @Minimum 1
    // @Maximum 2
    // @Description
    // Removes a value from the current queue.
    // You can optionally remove a value from a specific queue.
    // TODO: Explain more!
    // @Example
    // # This example removes the variable "test".
    // - undefine test
    // @Example
    // # This example removes the variable "test"  from the queue specified by the definition "run_queue".
    // - undefine test <[run_queue]>
    // -->

    @Override
    public String getName() {
        return "undefine";
    }

    @Override
    public String getArguments() {
        return "<definition> [queue]";
    }

    @Override
    public int getMinimumArguments() {
        return 1;
    }

    @Override
    public int getMaximumArguments() {
        return 2;
    }

    @Override
    public boolean isProcedural() {
        return true;
    }

    @Override
    public void execute(CommandQueue queue, CommandEntry entry) {
        CommandQueue tq = queue;
        if (entry.arguments.size() > 1) {
            tq = QueueTag.getFor(queue.error, entry.getArgumentObject(queue, 1)).getInternal();
        }
        String def = entry.getArgumentObject(queue, 0).toString();
        tq.commandStack.peek().removeDefinition(def);
        if (queue.shouldShowGood()) {
            if (tq == queue) {
                queue.outGood("Removed definition '" + ColorSet.emphasis + def
                        + ColorSet.good + "' successfully.");
            }
            else {
                queue.outGood("Removed definition '" + ColorSet.emphasis + def
                        + ColorSet.good + "' successfully from queue " + ColorSet.emphasis + new QueueTag(tq).debug()
                        + ColorSet.good + ".");
            }
        }
    }
}
