package org.mcmonkey.denizen2core.commands;

import org.mcmonkey.denizen2core.Denizen2Core;
import org.mcmonkey.denizen2core.arguments.Argument;
import org.mcmonkey.denizen2core.arguments.TextArgumentBit;
import org.mcmonkey.denizen2core.utilities.CoreUtilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single entry in a CommandQueue.
 */
public class CommandEntry {

    public final AbstractCommand command;

    public final ArrayList<Argument> arguments;

    public String getArgument(CommandQueue queue, int index) {
        return arguments.get(index).getString();
    }

    public static CommandEntry forLine(String input) {
        List<String> args = CoreUtilities.split(input, ' '); // TODO: Split with quotes.
        AbstractCommand tcmd = Denizen2Core.getCommands().get(CoreUtilities.toLowerCase(args.get(0)));
        if (tcmd == null) {
            throw new RuntimeException("Invalid command!"); // TODO: Neater error.
        }
        ArrayList<Argument> fargs = new ArrayList<>();
        for (int i = 1; i < args.size(); i++) {
            Argument arg = new Argument();
            arg.addBit(new TextArgumentBit(args.get(i)));
            fargs.add(arg);
        }
        return new CommandEntry(tcmd, fargs);
    }

    public CommandEntry(AbstractCommand cmd, ArrayList<Argument> args) {
        command = cmd;
        arguments = args;
        if (args.size() < cmd.getMinimumArguments()) {
            throw new RuntimeException("Not enough arguments!");
        }
        int max = cmd.getMaximumArguments();
        if (max >= 0 && args.size() > max) {
            throw new RuntimeException("Too many arguments!");
        }
    }
}
