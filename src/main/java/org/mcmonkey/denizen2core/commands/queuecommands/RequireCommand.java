package org.mcmonkey.denizen2core.commands.queuecommands;

import org.mcmonkey.denizen2core.commands.AbstractCommand;
import org.mcmonkey.denizen2core.commands.CommandEntry;
import org.mcmonkey.denizen2core.commands.CommandQueue;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.objects.ListTag;
import org.mcmonkey.denizen2core.tags.objects.QueueTag;

public class RequireCommand extends AbstractCommand {

    // <--[command]
    // @Name require <list of definition names>
    // @Arguments
    // @Short throws an error if the specified definition(s) are not present.
    // @Updated 2016/08/11
    // @Authors mcmonkey
    // @Group Queue
    // @Procedural true
    // @Minimum 1
    // @Maximum 1
    // @Description
    // Throws an error if the specified definition(s) are not present.
    // TODO: Explain more!
    // @Example
    // # This example throws an error if the definitions 'hello' or 'world' are not present.
    // - require hello|world
    // -->

    @Override
    public String getName() {
        return "require";
    }

    @Override
    public String getArguments() {
        return "<list of definition names>";
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
        ListTag defs = ListTag.getFor(queue.error, entry.getArgumentObject(queue, 0));
        for (AbstractTagObject obj : defs.getInternal()) {
            String d = obj.toString();
            if (!queue.commandStack.peek().hasDefinition(d)) {
                queue.handleError(entry, "Missing required definition: " + d);
            }
        }
        if (queue.shouldShowGood()) {
            queue.outGood("Requirements passed!");
        }
    }
}
