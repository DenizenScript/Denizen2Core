package org.mcmonkey.denizen2core.commands;

import org.mcmonkey.denizen2core.DebugMode;
import org.mcmonkey.denizen2core.Denizen2Core;
import org.mcmonkey.denizen2core.arguments.Argument;
import org.mcmonkey.denizen2core.commands.commoncommands.DebugInvalidCommand;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.utilities.CoreUtilities;
import org.mcmonkey.denizen2core.utilities.debugging.ColorSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a single entry in a CommandQueue.
 */
public class CommandEntry {

    public final AbstractCommand command;

    public final List<Argument> arguments;

    public final String originalLine;

    public final String cmdName;

    public final boolean waitFor;

    public final String scriptName;

    public int blockStart = 0;

    public int blockEnd = 0;

    public int ownIndex = 0;

    public List<CommandEntry> innerCommandBlock;

    public Object getData(CommandQueue queue) {
        return queue.commandStack.peek().entryObjects[ownIndex];
    }

    public void setData(CommandQueue queue, Object obj) {
        queue.commandStack.peek().entryObjects[ownIndex] = obj;
    }

    public AbstractTagObject getArgumentObject(CommandQueue queue, int index) {
        return arguments.get(index).parse(queue, new HashMap<>(), DebugMode.FULL, queue.error);
    }

    public static CommandEntry forLine(String scrName, String input) {
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
                    fargs.add(Denizen2Core.splitToArgument(arg, thisArgQuoted, qtype));
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
            fargs.add(Denizen2Core.splitToArgument(arg, thisArgQuoted, qtype));
        }
        if (fargs.size() == 0) {
            throw new RuntimeException("Invalid command line - looks blank!");
        }
        String cmd = CoreUtilities.toLowerCase(fargs.get(0).toString());
        boolean wf = false;
        if (cmd.startsWith("&")) {
            wf = true;
            cmd = cmd.substring(1);
        }
        AbstractCommand tcmd = Denizen2Core.commands.get(cmd);
        if (tcmd == null) {
            return new CommandEntry(scrName, DebugInvalidCommand.instance, fargs, input, fargs.get(0).toString(), false);
        }
        fargs.remove(0);
        return new CommandEntry(scrName, tcmd, fargs, input, tcmd.getName(), wf);
    }

    public CommandEntry(String scrName, AbstractCommand cmd, List<Argument> args, String original, String name, boolean wf) {
        command = cmd;
        arguments = args;
        originalLine = original;
        cmdName = name;
        waitFor = wf;
        scriptName = scrName;
        if (args.size() < cmd.getMinimumArguments()) {
            throw new RuntimeException("Not enough arguments for command '" + originalLine
                    + "', expected: " + ColorSet.emphasis + cmd.getArguments());
        }
        int max = cmd.getMaximumArguments();
        if (max >= 0 && args.size() > max) {
            throw new RuntimeException("Too many arguments for command '" + originalLine
                    + "', expected: " + ColorSet.emphasis + cmd.getArguments());
        }
    }
}
