package com.denizenscript.denizen2core.commands.queuecommands;

import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.objects.TextTag;
import com.denizenscript.denizen2core.utilities.debugging.ColorSet;

public class AddtoCommand extends AbstractCommand {

    // <--[command]
    // @Name addto
    // @Arguments <definition> 'raw/parsed' <values>
    // @Short Adds all input to a definition, optionally parsing tags.
    // @Updated 2016/08/27
    // @Group Queue
    // @Procedural true
    // @Minimum 3
    // @Maximum -1
    // @tag <def[<TextTag>]> (Dynamic) returns the updated definition's value.
    // @Description
    // Adds all input to a definition, optionally parsing tags.
    // The definition must exist before hand.
    // Can go across multiple lines!
    // Note that raw values will include ALL input, even quotes!
    // Note that it will trim spaces on each line automatically though.
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
        StringBuilder res = new StringBuilder();
        res.append(ato.toString());
        boolean parsed = !entry.getArgumentObject(queue, 1).toString().equals("raw");
        if (parsed) {
            for (int i = 2; i < entry.arguments.size(); i++) {
                res.append(entry.getArgumentObject(queue, i).toString());
                if (i + 1 < entry.arguments.size()) {
                    res.append(" ");
                }
            }
        }
        else {
            int s = entry.originalLine.indexOf(" raw ");
            String gotten = entry.originalLine.substring(s + " raw ".length());
            if (def.equals("raw")) {
                s = gotten.indexOf(" raw ");
                gotten = entry.originalLine.substring(s + " raw ".length());
            }
            res.append(gotten);
        }
        queue.commandStack.peek().setDefinition(def, new TextTag(res.toString()));
        if (queue.shouldShowGood()) {
            queue.outGood("Updated definition '" + ColorSet.emphasis + def
                    + ColorSet.good + "' successfully.");
        }
    }
}
