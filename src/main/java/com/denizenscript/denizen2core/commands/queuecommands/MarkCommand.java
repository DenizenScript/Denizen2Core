package com.denizenscript.denizen2core.commands.queuecommands;

import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.commands.CommandEntry;

public class MarkCommand extends AbstractCommand {

    // <--[command]
    // @Since 0.3.0
    // @Name mark
    // @Arguments <name>
    // @Short marks a code location for the goto command.
    // @Updated 2016/04/06
    // @Group Queue
    // @Procedural true
    // @Minimum 1
    // @Maximum 1
    // @Description
    // Marks a code location for later usage with the <@link command goto>goto<@/link> command.
    // TODO: Explain more!
    // @Example
    // # This example marks the location "test".
    // - mark test
    // -->

    @Override
    public String getName() {
        return "mark";
    }

    @Override
    public String getArguments() {
        return "<name>";
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
    public boolean isProcedural() {
        return true;
    }

    @Override
    public void execute(CommandQueue queue, CommandEntry entry) {
        if (queue.shouldShowGood()) {
            queue.outGood("Passing mark...");
        }
    }
}
