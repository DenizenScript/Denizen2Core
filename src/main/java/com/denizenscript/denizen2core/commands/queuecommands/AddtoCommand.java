package com.denizenscript.denizen2core.commands.queuecommands;

import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.objects.ListTag;
import com.denizenscript.denizen2core.tags.objects.TextTag;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import com.denizenscript.denizen2core.utilities.debugging.ColorSet;

public class AddtoCommand extends AbstractCommand {

    // <--[command]
    // @Name addto
    // @Arguments <definition> 'raw/parsed/list' <values>
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
    // If you specify 'list', it will add each item to the definition as a list tag.
    // Note that if the definition is not a list tag, it will become one, which could damage its data in some cases.
    // Note that if you specify a list as a single entry, it will become a sub-list rather than splitting into the main list.
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
    // @Example
    // # This example adds some new entries to the list definition "mylist".
    // - define mylist <list[a|b]>
    // - addto mylist list c d e
    // -->

    // TODO "web" style tag parsing as an option maybe?

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
        AbstractTagObject resultant;
        if (ato == null) {
            queue.handleError(entry, "Invalid definition name!");
            return;
        }
        String mode = CoreUtilities.toLowerCase(entry.getArgumentObject(queue, 1).toString());
        if (mode.equals("list")) {
            ListTag lt = ListTag.getFor(queue.error, ato);
            for (int i = 2; i < entry.arguments.size(); i++) {
                lt.getInternal().add(entry.getArgumentObject(queue, i));
            }
            resultant = lt;
        }
        else if (mode.equals("parsed")) {
            StringBuilder res = new StringBuilder();
            res.append(ato.toString());
            for (int i = 2; i < entry.arguments.size(); i++) {
                res.append(entry.getArgumentObject(queue, i).toString());
                if (i + 1 < entry.arguments.size()) {
                    res.append(" ");
                }
            }
            resultant = new TextTag(res.toString());
        }
        else if (mode.equals("raw")) {
            StringBuilder res = new StringBuilder();
            res.append(ato.toString());
            int s = entry.originalLine.indexOf(" raw ");
            String gotten = entry.originalLine.substring(s + " raw ".length());
            String td = def;
            int tdi = td.indexOf(" raw ");
            // This is an interesting workaround here... Perhaps look for a better option?
            while (tdi >= 0) {
                s = gotten.indexOf(" raw ");
                gotten = entry.originalLine.substring(s + " raw ".length());
                td = td.substring(tdi + " raw ".length());
                tdi = td.indexOf(" raw ");
            }
            res.append(gotten);
            resultant = new TextTag(res.toString());
        }
        else {
            queue.handleError(entry, "Invalid add mode: " + mode);
            return;
        }
        queue.commandStack.peek().setDefinition(def, resultant);
        if (queue.shouldShowGood()) {
            queue.outGood("Updated definition '" + ColorSet.emphasis + def
                    + ColorSet.good + "' successfully.");
        }
    }
}
