package org.mcmonkey.denizen2core.commands.commoncommands;

import org.mcmonkey.denizen2core.Denizen2Core;
import org.mcmonkey.denizen2core.commands.AbstractCommand;
import org.mcmonkey.denizen2core.commands.CommandEntry;
import org.mcmonkey.denizen2core.commands.CommandQueue;

public class ReloadCommand extends AbstractCommand {

    // <--[command]
    // @Name reload
    // @Arguments
    // @Short reloads the script engine, recalculating any scripts available.
    // @Updated 2016/08/07
    // @Authors mcmonkey
    // @Group Common
    // @Minimum 0
    // @Maximum 0
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
        return 0;
    }

    @Override
    public void execute(CommandQueue queue, CommandEntry entry) {
        Denizen2Core.reload();
    }
}
