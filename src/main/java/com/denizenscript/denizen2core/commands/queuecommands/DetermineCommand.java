package com.denizenscript.denizen2core.commands.queuecommands;

import com.denizenscript.denizen2core.events.ScriptEvent;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.objects.BooleanTag;
import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.tags.objects.MapTag;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import com.denizenscript.denizen2core.utilities.debugging.ColorSet;

public class DetermineCommand extends AbstractCommand {

    // <--[command]
    // @Since 0.3.0
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
        AbstractTagObject ato = BooleanTag.getForBoolean(true);
        if (entry.arguments.size() > 1) {
            ato = entry.getArgumentObject(queue, 1);
        }
        ScriptEvent sendTo = queue.commandStack.peek().sendDeterminesTo;
        if (sendTo != null) {
            sendTo.applyDetermination(true, det, ato);
            queue.commandStack.peek().definitions.put("context", new MapTag(sendTo.getDefinitions(sendTo.curRun)));
        }
        else {
            queue.determinations.getInternal().put(det, ato);
        }
        if (queue.shouldShowGood()) {
            queue.outGood("Determined '" + ColorSet.emphasis + det
                    + ColorSet.good + "' as '" + ColorSet.emphasis + ato.debug()
                    + ColorSet.good + "' successfully.");
        }
    }
}
