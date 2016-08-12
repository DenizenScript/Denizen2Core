package org.mcmonkey.denizen2core.commands.commoncommands;

import org.mcmonkey.denizen2core.Denizen2Core;
import org.mcmonkey.denizen2core.commands.AbstractCommand;
import org.mcmonkey.denizen2core.commands.CommandEntry;
import org.mcmonkey.denizen2core.commands.CommandQueue;

public class EchoCommand extends AbstractCommand {

    // <--[command]
    // @Name echo
    // @Arguments <text to echo>
    // @Short echoes text back to the input command line.
    // @Updated 2016/04/02
    // @Authors mcmonkey
    // @Group Common
    // @Procedural true
    // @Minimum 1
    // @Maximum 1
    // @Description
    // Echoes text back to the input command line.
    // TODO: Explain more?
    // @Example
    // # This example outputs the text "Hello World!" to console.
    // - echo "Hello World!"
    // -->

    @Override
    public String getName() {
        return "echo";
    }

    @Override
    public String getArguments() {
        return "<text to echo>";
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
        Denizen2Core.getImplementation().outputInfo(entry.getArgumentObject(queue, 0).toString());
    }
}
