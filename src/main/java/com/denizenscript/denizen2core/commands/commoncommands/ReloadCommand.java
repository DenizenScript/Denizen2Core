package com.denizenscript.denizen2core.commands.commoncommands;

import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.tags.objects.BooleanTag;

public class ReloadCommand extends AbstractCommand {

    // <--[command]
// @Since 0.3.0
    // @Name reload
    // @Arguments [debug boolean]
    // @Short reloads the script engine, recalculating any scripts available.
    // @Updated 2016/08/07
    // @Group Common
    // @Minimum 0
    // @Maximum 1
    // @Description
    // Reloads the script engine, recalculating any scripts available.
    // TODO: Explain more?
    // @Example
    // # This example reloads all scripts.
    // - reload
    // -->

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getArguments() {
        return "";
    }

    @Override
    public int getMinimumArguments() {
        return 0;
    }

    @Override
    public int getMaximumArguments() {
        return 1;
    }

    @Override
    public void execute(CommandQueue queue, CommandEntry entry) {
        Denizen2Core.reload();
        if (entry.arguments.size() > 0) {
            BooleanTag bool = BooleanTag.getFor(queue.error, entry.getArgumentObject(queue, 0));
            if (bool.getInternal()) {
                Denizen2Core.dumpDebug();
            }
        }
    }
}
