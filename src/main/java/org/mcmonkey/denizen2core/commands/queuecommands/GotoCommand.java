package org.mcmonkey.denizen2core.commands.queuecommands;

import org.mcmonkey.denizen2core.commands.AbstractCommand;
import org.mcmonkey.denizen2core.commands.CommandEntry;
import org.mcmonkey.denizen2core.commands.CommandQueue;
import org.mcmonkey.denizen2core.commands.CommandStackEntry;
import org.mcmonkey.denizen2core.utilities.CoreUtilities;

public class GotoCommand extends AbstractCommand {

    // <--[command]
    // @Name goto
    // @Arguments <name>
    // @Short jumps to a code location specified by the mark command.
    // @Updated 2016/04/06
    // @Authors mcmonkey
    // @Group Queue
    // @Procedural true
    // @Minimum 1
    // @Maximum 1
    // @Description
    // Jumps to a code location specified beforehand by the <@link command mark>mark<@/link> command.
    // TODO: Explain more!
    // @Example
    // # This example goes to the location 'test'.
    // - goto test
    // -->

    @Override
    public String getName() {
        return "goto";
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
        String arg0 = CoreUtilities.toLowerCase(entry.getArgumentObject(queue, 0).toString());
        CommandStackEntry stackEntry = queue.commandStack.peek();
        for (int i = 0; i < stackEntry.entries.length; i++) {
            if (stackEntry.entries[i].command instanceof MarkCommand
                    // POSSIBLE: /maybe/ parse tags?
                && CoreUtilities.toLowerCase(stackEntry.entries[i].arguments.get(0).toString()).equals(arg0)) {
                if (queue.shouldShowGood()) {
                    queue.outGood("Went to marker: " + arg0);
                }
                stackEntry.goTo(i);
                return;
            }
        }
        queue.handleError(entry, "Invalid/unknown GOTO target!");
    }
}
