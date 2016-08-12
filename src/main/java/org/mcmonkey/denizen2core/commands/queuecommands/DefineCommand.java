package org.mcmonkey.denizen2core.commands.queuecommands;

import org.mcmonkey.denizen2core.commands.AbstractCommand;
import org.mcmonkey.denizen2core.commands.CommandEntry;
import org.mcmonkey.denizen2core.commands.CommandQueue;
import org.mcmonkey.denizen2core.tags.objects.QueueTag;
import org.mcmonkey.denizen2core.utilities.debugging.ColorSet;

public class DefineCommand extends AbstractCommand {

    // <--[command]
    // @Name define
    // @Arguments <definition> <value> [queue]
    // @Short Defines a value on the current queue.
    // @Updated 2016/07/15
    // @Authors mcmonkey
    // @Group Queue
    // @Procedural true
    // @Minimum 2
    // @Maximum 3
    // @tag <def[<TextTag>]> (Dynamic) returns the defined value.
    // @Description
    // Defines a value on the current queue.
    // You can optionally define a value on a specific queue.
    // TODO: Explain more!
    // @Example
    // # This example defines the variable "test" as the value "3", then echoes it back.
    // - define test 3
    // - echo <[test]>
    // @Example
    // # This example defines the variable "test" as the value "Hello World" on the queue specified by the definition "run_queue".
    // - define test "Hello World" <[run_queue]>
    // -->

    @Override
    public String getName() {
        return "define";
    }

    @Override
    public String getArguments() {
        return "<definition> <value>";
    }

    @Override
    public int getMinimumArguments() {
        return 2;
    }

    @Override
    public int getMaximumArguments() {
        return 3;
    }

    @Override
    public boolean isProcedural() {
        return true;
    }

    @Override
    public void execute(CommandQueue queue, CommandEntry entry) {
        CommandQueue tq = queue;
        if (entry.arguments.size() > 2) {
            tq = QueueTag.getFor(queue.error, entry.getArgumentObject(queue, 2)).getInternal();
        }
        String def = entry.getArgumentObject(queue, 0).toString();
        tq.commandStack.peek().setDefinition(def, entry.getArgumentObject(queue, 1));
        if (queue.shouldShowGood()) {
            if (tq == queue) {
                queue.outGood("Defined new definition '" + ColorSet.emphasis + def
                        + ColorSet.good + "' successfully.");
            }
            else {
                queue.outGood("Defined new definition '" + ColorSet.emphasis + def
                        + ColorSet.good + "' successfully on queue " + ColorSet.emphasis + tq.qID
                        + ColorSet.good + ".");
            }
        }
    }
}
