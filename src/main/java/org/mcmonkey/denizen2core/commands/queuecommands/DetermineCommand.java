package org.mcmonkey.denizen2core.commands.queuecommands;

import org.mcmonkey.denizen2core.commands.AbstractCommand;
import org.mcmonkey.denizen2core.commands.CommandEntry;
import org.mcmonkey.denizen2core.commands.CommandQueue;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.objects.BooleanTag;
import org.mcmonkey.denizen2core.utilities.CoreUtilities;

public class DetermineCommand extends AbstractCommand {

    // <--[command]
    // @Name determine
    // @Arguments <determination> [value]
    // @Short Defines a value on the current queue.
    // @Updated 2016/08/11
    // @Group Queue
    // @Procedural true
    // @Minimum 1
    // @Maximum 2
    // @Description
    // Determines a value on the current queue.
    // Default value is true.
    // TODO: Explain more!
    // @Example
    // # This example determines the option "cancelled" as the value "true".
    // - determine cancelled
    // -->

    @Override
    public String getName() {
        return "determine";
    }

    @Override
    public String getArguments() {
        return "<determination> [value]";
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
        String det = CoreUtilities.toLowerCase(entry.getArgumentObject(queue, 0).toString());
        AbstractTagObject ato = new BooleanTag(true);
        if (entry.arguments.size() > 1) {
            ato = entry.getArgumentObject(queue, 1);
        }
        queue.determinations.getInternal().put(det, ato);
        if (queue.shouldShowGood()) {
            queue.outGood("Determined successfully.");
        }
    }
}
