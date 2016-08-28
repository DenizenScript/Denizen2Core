package org.mcmonkey.denizen2core.commands.queuecommands;

import org.mcmonkey.denizen2core.commands.AbstractCommand;
import org.mcmonkey.denizen2core.commands.CommandEntry;
import org.mcmonkey.denizen2core.commands.CommandQueue;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.objects.TextTag;
import org.mcmonkey.denizen2core.utilities.debugging.ColorSet;

public class AddtoCommand extends AbstractCommand {

    // <--[command]
    // @Name addto
    // @Arguments <definition> 'raw/parsed' <values>
    // @Short Adds all input to a definition, optionally parsing tags.
    // @Updated 2016/08/27
    // @Authors mcmonkey
    // @Group Queue
    // @Procedural true
    // @Minimum 3
    // @Maximum -1
    // @tag <def[<TextTag>]> (Dynamic) returns the updated definition's value.
    // @Description
    // Adds all input to a definition, optionally parsing tags.
    // The definition must exist before hand.
    // Can go across multiple lines!
    // TODO: Explain more!
    // @Example
    // # This example adds "potato" to the end of definition "test".
    // - define test "hi"
    // - addto test raw potato
    // @Example
    // # This example adds a long bit of information to the definition "test".
    // - define test "Hello World"
    // - addto test raw
    //   and all who inhabit it.
    //   In the end,
    //   it doesn't even matter anymore.
    // -->

    @Override
    public String getName() {
        return "addto";
    }

    @Override
    public String getArguments() {
        return "<definition> 'raw/parsed' <values>";
    }

    @Override
    public int getMinimumArguments() {
        return 3;
    }

    @Override
    public int getMaximumArguments() {
        return -1;
    }

    @Override
    public boolean isProcedural() {
        return true;
    }

    @Override
    public void execute(CommandQueue queue, CommandEntry entry) {
        String def = entry.getArgumentObject(queue, 0).toString();
        AbstractTagObject ato = queue.commandStack.peek().getDefinition(def);
        if (ato == null) {
            queue.handleError(entry, "Invalid definition name!");
            return;
        }
        String res = ato.toString();
        if (entry.getArgumentObject(queue, 1).toString().equals("parsed")) {
            res += entry.getArgumentObject(queue, 2).toString();
        }
        else {
            res += entry.arguments.get(2).toString();
        }
        queue.commandStack.peek().setDefinition(def, new TextTag(res));
        if (queue.shouldShowGood()) {
            queue.outGood("Updated definition '" + ColorSet.emphasis + def
                    + ColorSet.good + "' successfully.");
        }
    }
}
