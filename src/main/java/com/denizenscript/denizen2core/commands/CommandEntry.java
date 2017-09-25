package com.denizenscript.denizen2core.commands;

import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.arguments.Argument;
import com.denizenscript.denizen2core.commands.commoncommands.DebugInvalidCommand;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.utilities.ErrorInducedException;
import com.denizenscript.denizen2core.utilities.debugging.ColorSet;
import com.denizenscript.denizen2core.utilities.CoreUtilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a single entry in a CommandQueue.
 */
public class CommandEntry {

    public final AbstractCommand command;

    public final List<Argument> arguments;

    // <--[explanation]
    // @Name Named Arguments
    // @Group Commands
    // @Description
    // Many commands in Denizen2 support NAMED ARGUMENTS.
    // A named argument is a non-linear argument (IE, an argument that can be put anywhere in the command, orderlessly),
    // to specify some data that is easier or clearer to write with an explicit name, particular to clarify boolean options.
    //
    // An example of the syntax is the following:
    // <@code>
    // - do a thing --power 7
    // <@/code>
    // In that example, 'do' is the command, 'a' and 'thing' are arguments, and 'power' is a named argument with value '7'.
    //
    // It's worth noting that the argument name may not contain a tag. It's value, however, can.
    // -->

    public final HashMap<String, Argument> namedArgs;

    public final String originalLine;

    public final String cmdName;

    public final boolean waitFor;

    public final String scriptName;

    public int blockStart = 0;

    public int blockEnd = 0;

    public int ownIndex = 0;

    public List<CommandEntry> innerCommandBlock;

    // <--[explanation]
    // @Name Saved Commands
    // @Group Commands
    // @Description
    // Some commands have to give back data to better fulfill their purpose.
    // Rather than create a specific tag for this data, we allow commands to save into local definitions.
    // They will always do this, with default names, and you can see these names and their results in the documentation for any command.
    // However, it is useful to know that you may rename at least one if not more return values for a command.
    // You do this via the 'save' named argument.
    // You give that argument as a value the name of the definition you want to save into.
    //
    // An example of the syntax is as follows:
    // <@code>
    // - do a thing --save example
    // <@/code>
    // In that example, 'do a thing' is the command with arguments, and any data it gives back is saved under the definition 'example'.
    // To access the data, one would simply type '<[example]>' as per normal definition access!
    //
    // It is important to note that the data is only available after the command is completed,
    // and the definition will not be present in prior code, unless defined by a further prior command.
    // -->

    public String resName(CommandQueue queue, String def) {
        return namedArgs.containsKey("save") ? getNamedArgumentObject(queue, "save").toString() : def;
    }

    public Object getData(CommandQueue queue) {
        return queue.commandStack.peek().entryObjects[ownIndex];
    }

    public void setData(CommandQueue queue, Object obj) {
        queue.commandStack.peek().entryObjects[ownIndex] = obj;
    }

    public AbstractTagObject getNamedArgumentObject(CommandQueue queue, String name) {
        Argument obj = namedArgs.get(name);
        if (obj == null) {
            return null;
        }
        return obj.parse(queue, new HashMap<>(), queue.commandStack.peek().getDebugMode(), queue.error);
    }

    public AbstractTagObject getArgumentObject(CommandQueue queue, int index) {
        return arguments.get(index).parse(queue, new HashMap<>(), queue.commandStack.peek().getDebugMode(), queue.error);
    }

    private static void setupError(String message) {
        throw new ErrorInducedException(message);
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
                    fargs.add(Denizen2Core.splitToArgument(arg, thisArgQuoted, qtype, CommandEntry::setupError));
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
            fargs.add(Denizen2Core.splitToArgument(arg, thisArgQuoted, qtype, CommandEntry::setupError));
        }
        if (fargs.size() == 0) {
            throw new ErrorInducedException("Invalid command line - looks blank!");
        }
        String cmd = CoreUtilities.toLowerCase(fargs.get(0).toString());
        boolean wf = false;
        if (cmd.startsWith("&")) {
            wf = true;
            cmd = cmd.substring(1);
        }
        HashMap<String, Argument> nameds = new HashMap<>();
        for (int i = 0; i < fargs.size(); i++) {
            if (!fargs.get(i).getQuoted() && fargs.get(i).toString().startsWith("--")) {
                nameds.put(CoreUtilities.toLowerCase(fargs.get(i).toString().substring(2)), fargs.get(i + 1));
                fargs.remove(i);
                fargs.remove(i);
                i -= 2;
            }
        }
        AbstractCommand tcmd = Denizen2Core.commands.get(cmd);
        if (tcmd == null) {
            return new CommandEntry(scrName, DebugInvalidCommand.instance, fargs, nameds, input, fargs.get(0).toString(), false);
        }
        fargs.remove(0);
        return new CommandEntry(scrName, tcmd, fargs, nameds, input, tcmd.getName(), wf);
    }

    public CommandEntry(String scrName, AbstractCommand cmd, List<Argument> args, HashMap<String, Argument> nameds,
                        String original, String name, boolean wf) {
        command = cmd;
        arguments = args;
        namedArgs = nameds;
        originalLine = original;
        cmdName = name;
        waitFor = wf;
        scriptName = scrName;
        if (args.size() < cmd.getMinimumArguments()) {
            throw new ErrorInducedException("Not enough arguments for command '" + ColorSet.emphasis + originalLine
                    + ColorSet.warning + "', expected: " + ColorSet.emphasis + cmd.getArguments());
        }
        int max = cmd.getMaximumArguments();
        if (max >= 0 && args.size() > max) {
            throw new ErrorInducedException("Too many arguments for command '" + ColorSet.emphasis + originalLine
                    + ColorSet.warning + "', expected: " + ColorSet.emphasis + cmd.getArguments());
        }
    }
}
