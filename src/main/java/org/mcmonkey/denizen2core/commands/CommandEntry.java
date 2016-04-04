package org.mcmonkey.denizen2core.commands;

import org.mcmonkey.denizen2core.DebugMode;
import org.mcmonkey.denizen2core.Denizen2Core;
import org.mcmonkey.denizen2core.arguments.Argument;
import org.mcmonkey.denizen2core.commands.commoncommands.DebugInvalidCommand;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.utilities.CoreUtilities;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a single entry in a CommandQueue.
 */
public class CommandEntry {

    public final AbstractCommand command;

    public final ArrayList<Argument> arguments;

    public final String originalLine;

    public AbstractTagObject getArgumentObject(CommandQueue queue, int index) {
        return arguments.get(index).parse(new HashMap<>(), DebugMode.FULL, queue.error);
    }

    public static CommandEntry forLine(String input) {
        input = input.replace('\0', ' ');
        ArrayList<Argument> fargs = new ArrayList<>();
        boolean quoted = false;
        boolean qtype = false;
        boolean thisArgQuoted = false;
        int start = 0;
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '"' && (!quoted || qtype)) {
                qtype = true;
                quoted = !quoted;
                thisArgQuoted = true;
            }
            else if (c == '\'' && (!quoted || !qtype)) {
                qtype = false;
                quoted = !quoted;
                thisArgQuoted = true;
            }
            else if (c == '\n' && !quoted) {
                input = (i + 1 < input.length()) ? input.substring(0, i) + input.substring(i + 1) : input.substring(0, i);
            }
            else if (!quoted && c == ' ') {
                if (i - start > 0) {
                    String arg = input.substring(start, i).trim().replace('\'', '"').replace("\"", "");
                    fargs.add(Denizen2Core.splitToArgument(arg, thisArgQuoted));
                    start = i + 1;
                    thisArgQuoted = false;
                }
                else {
                    start = i + 1;
                }
            }
        }
        if (input.length() - start > 0) {
            String arg = input.substring(start, input.length()).trim().replace('\'', '"').replace("\"", "");
            fargs.add(Denizen2Core.splitToArgument(arg, thisArgQuoted));
        }
        if (fargs.size() == 0) {
            throw new RuntimeException("Invalid command line - looks blank!");
        }
        String cmd = CoreUtilities.toLowerCase(fargs.get(0).toString());
        AbstractCommand tcmd = Denizen2Core.commands.get(cmd);
        if (tcmd == null) {
            return new CommandEntry(DebugInvalidCommand.instance, fargs, input);
        }
        fargs.remove(0);
        return new CommandEntry(tcmd, fargs, input);
    }

    public CommandEntry(AbstractCommand cmd, ArrayList<Argument> args, String original) {
        command = cmd;
        arguments = args;
        originalLine = original;
        if (args.size() < cmd.getMinimumArguments()) {
            throw new RuntimeException("Not enough arguments, expected: " + cmd.getArguments());
        }
        int max = cmd.getMaximumArguments();
        if (max >= 0 && args.size() > max) {
            throw new RuntimeException("Too many arguments, expected: " + cmd.getArguments());
        }
    }
}
