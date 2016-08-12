package org.mcmonkey.denizen2core.commands.queuecommands;

import org.mcmonkey.denizen2core.commands.AbstractCommand;
import org.mcmonkey.denizen2core.commands.CommandEntry;
import org.mcmonkey.denizen2core.commands.CommandQueue;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.objects.BooleanTag;
import org.mcmonkey.denizen2core.tags.objects.ListTag;

public class AssertCommand extends AbstractCommand {

    // <--[command]
    // @Name assert <required boolean> <error message>
    // @Arguments
    // @Short throws an error if the specified boolean is false.
    // @Updated 2016/08/11
    // @Authors mcmonkey
    // @Group Queue
    // @Procedural true
    // @Minimum 2
    // @Maximum 2
    // @Description
    // Throws an error if the specified boolean is false.
    // TODO: Explain more!
    // @Example
    // # This example throws an error.
    // - assert false "This is an error message!"
    // -->

    @Override
    public String getName() {
        return "assert";
    }

    @Override
    public String getArguments() {
        return "<required boolean> <error message>";
    }

    @Override
    public int getMinimumArguments() {
        return 2;
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
        BooleanTag check = BooleanTag.getFor(queue.error, entry.getArgumentObject(queue, 0));
        if (!check.getInternal()) {
            queue.handleError(entry, entry.getArgumentObject(queue, 1).toString());
        }
        if (queue.shouldShowGood()) {
            queue.outGood("Assertion passed!");
        }
    }
}
