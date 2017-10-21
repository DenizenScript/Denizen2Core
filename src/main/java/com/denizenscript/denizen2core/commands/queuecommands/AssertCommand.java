package com.denizenscript.denizen2core.commands.queuecommands;

import com.denizenscript.denizen2core.tags.objects.BooleanTag;
import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;

public class AssertCommand extends AbstractCommand {

    // <--[command]
    // @Since 0.3.0
    // @Name assert
    // @Arguments <required boolean> <error message>
    // @Short throws an error if the specified boolean is false.
    // @Updated 2016/08/11
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
