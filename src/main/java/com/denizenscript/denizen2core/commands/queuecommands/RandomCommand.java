package com.denizenscript.denizen2core.commands.queuecommands;

import com.denizenscript.denizen2core.arguments.Argument;
import com.denizenscript.denizen2core.arguments.TextArgumentBit;
import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.commands.CommandScriptSection;
import com.denizenscript.denizen2core.tags.objects.TextTag;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import com.denizenscript.denizen2core.utilities.ErrorInducedException;
import com.denizenscript.denizen2core.utilities.debugging.ColorSet;

import java.util.*;

public class RandomCommand extends AbstractCommand {

    // <--[command]
    // @Since 0.4.0
    // @Name random
    // @Arguments
    // @Short selects and runs a randomly chosen sub-block, based on given weights.
    // @Updated 2017/11/10
    // @Group Queue
    // @Procedural true
    // @Minimum 0
    // @Maximum 2
    // @Description
    // Selects and runs a randomly chosen sub-block, based on given weights.
    // TODO: Explain more!
    // @Example
    // # This example will either echo "A" or "B".
    // - random:
    //   - chance 1:
    //     - echo "A"
    //   - chance 1:
    //     - echo "b"
    // @Example
    // # This example will tend to echo "A", but sometimes as well echo "B"
    // - random:
    //   - chance 10:
    //     - echo "A"
    //   - chance 1:
    //     - echo "b"
    // -->

    @Override
    public String getName() {
        return "random";
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
        return 2;
    }

    @Override
    public boolean isProcedural() {
        return true;
    }

    @Override
    public boolean allowsBlock() {
        return true;
    }

    @Override
    public boolean blockIsCustom() {
        return true;
    }

    public static class Choice {
        double chance;
        int start;
    }

    @Override
    public void customBlockHandle(CommandEntry entry, String scrName, List<Object> innards, int istart, List<CommandEntry> entries) {
        List<Choice> choices = new ArrayList<>();
        entry.specialLocalData = choices;
        List<TextArgumentBit> fixmes = new ArrayList<>();
        if (innards.size() == 0) {
            throw new ErrorInducedException("Empty random command?");
        }
        for (Object obj : innards) {
            if (!(obj instanceof Map)) {
                throw new ErrorInducedException("Entry to a 'random' command is not a map: " + obj);
            }
            String name = ((String) ((Map) obj).keySet().iterator().next()).trim();
            List<Object> vals = (List<Object>) ((Map) obj).get(name);
            List<CommandEntry> ce = CommandScriptSection.getEntries(scrName, vals, istart);
            entries.addAll(ce);
            if (name.startsWith("chance")) {
                name = name.substring("chance ".length());
                if ((name.endsWith("\"") && name.startsWith("\"")) || name.endsWith("\'") && name.startsWith("\'")) {
                    name = name.substring(1, name.length() - 1);
                }
                Choice c = new Choice();
                c.chance = Double.parseDouble(name);
                if (c.chance <= 0) {
                    throw new ErrorInducedException("Invalid chance (Must be greater than zero): " + c.chance);
                }
                c.start = istart;
                choices.add(c);
            }
            else {
                throw new ErrorInducedException("Ridiculous random command input: " + name);
            }
            istart += ce.size();
            Argument arg = new Argument();
            arg.addBit(new TextArgumentBit("\0CALLBACK", false));
            Argument arg2 = new Argument();
            TextArgumentBit fm = new TextArgumentBit("TO_SET", false);
            arg2.addBit(fm);
            fixmes.add(fm);
            List<Argument> args = Arrays.asList(arg, arg2);
            CommandEntry ent = new CommandEntry(entry.scriptName, entry.command, args, new HashMap<>(), entry.cmdName + " \0CALLBACK TO_SET", entry.cmdName, false);
            entries.add(ent);
            istart++;
        }
        for (TextArgumentBit tab : fixmes) {
            tab.value = new TextTag(String.valueOf(istart));
        }
        entry.blockEnd = istart - 1;
    }

    @Override
    public void execute(CommandQueue queue, CommandEntry entry) {
        if (entry.arguments.size() > 0 && entry.arguments.get(0).toString().equals("\0CALLBACK")) {
            if (entry.arguments.size() > 1) {
                int go_to = Integer.valueOf(entry.arguments.get(1).toString());
                queue.commandStack.peek().goTo(go_to);
            }
            return;
        }
        if (entry.specialLocalData == null) {
            queue.handleError(entry, "Mis-constructed random command?");
            return;
        }
        List<Choice> choices = (List<Choice>) entry.specialLocalData;
        double d = 0;
        for (Choice c : choices) {
            d += c.chance;
        }
        double chosen = CoreUtilities.random.nextDouble() * d;
        final double ch2 = chosen;
        Choice finalc = choices.get(0);
        for (Choice c : choices) {
            if (chosen <= c.chance) {
                finalc = c;
                break;
            }
            chosen -= c.chance;
        }
        if (queue.shouldShowGood()) {
            queue.outGood("Random command rolled " + ColorSet.emphasis + ch2 + ColorSet.good + " / " + ColorSet.emphasis + d + ColorSet.good
                    + " -> selected choice with weight " + ColorSet.emphasis + finalc.chance);
        }
        queue.commandStack.peek().goTo(finalc.start);
    }
}
