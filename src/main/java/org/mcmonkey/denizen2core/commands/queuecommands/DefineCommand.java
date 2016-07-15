package org.mcmonkey.denizen2core.commands.queuecommands;

import org.mcmonkey.denizen2core.commands.AbstractCommand;
import org.mcmonkey.denizen2core.commands.CommandEntry;
import org.mcmonkey.denizen2core.commands.CommandQueue;

public class DefineCommand extends AbstractCommand {

    // <--[command]
    // @Name define
    // @Arguments <definition> <value>
    // @Short Defines a value on the current queue.
    // @Updated 2016/07/15
    // @Authors mcmonkey
    // @Group Queue
    // @Minimum 2
    // @Maximum 2
    // @Description
    // Defines a value on the current queue.
    // TODO: Explain more!
    // @Example
    // # This example defines the variable 'test' as the value '3', then echoes it back.
    // - define test 3
    // - echo <[test]>
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
        return 2;
    }

    @Override
    public boolean isWaitable() {
        return false;
    }

    @Override
    public void execute(CommandQueue queue, CommandEntry entry) {
        String def = entry.getArgumentObject(queue, 0).toString();
        queue.commandStack.peek().setDefinition(def, entry.getArgumentObject(queue, 1));
        if (queue.shouldShowGood()) {
            queue.outGood("Defined a new definition, '" + def + "' successfully.");
        }
    }
}
