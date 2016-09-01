package com.denizenscript.denizen2core.commands.commoncommands;

import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.commands.CommandEntry;

public class DebugInvalidCommand extends AbstractCommand {

    // Intentionally no meta: This command is not user-triggerable!

    public static final DebugInvalidCommand instance = new DebugInvalidCommand();

    @Override
    public String getName() {
        return "\0DebugOutputInvalidCommand";
    }

    @Override
    public String getArguments() {
        return "<invalid command>";
    }

    @Override
    public int getMinimumArguments() {
        return 1;
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
        Denizen2Core.getImplementation().outputInvalid(queue, entry);
    }
}
