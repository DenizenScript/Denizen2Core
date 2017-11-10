package com.denizenscript.denizen2core.commands.queuecommands;

import com.denizenscript.denizen2core.arguments.Argument;
import com.denizenscript.denizen2core.arguments.TextArgumentBit;
import com.denizenscript.denizen2core.commands.*;
import com.denizenscript.denizen2core.tags.objects.IntegerTag;
import com.denizenscript.denizen2core.tags.objects.TextTag;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import com.denizenscript.denizen2core.utilities.ErrorInducedException;
import com.denizenscript.denizen2core.utilities.Tuple;

import java.util.*;

public class ChooseCommand extends AbstractCommand {

    // <--[command]
    // @Since 0.4.0
    // @Name choose
    // @Arguments <choice>
    // @Short selects and runs the sub-block matching the given choice, or the default sub-block.
    // @Updated 2017/11/10
    // @Group Queue
    // @Procedural true
    // @Minimum 0
    // @Maximum -1
    // @Description
    // Selects and runs the sub-block matching the given choice, or the default sub-block.
    // TODO: Explain more!
    // @Example
    // # This example always echoes "hi".
    // - choose 3:
    //   - case 3:
    //     - echo "hi"
    // @Example
    // # This example always echoes "hi".
    // - choose 4:
    //   - case 3:
    //     - echo "nope"
    //   - default:
    //     - echo "hi"
    // -->

    @Override
    public String getName() {
        return "choose";
    }

    @Override
    public String getArguments() {
        return "<choice>";
    }

    @Override
    public int getMinimumArguments() {
        return 1;
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

    @Override
    public void customBlockHandle(CommandEntry entry, String scrName, List<Object> innards, int istart, List<CommandEntry> entries) {
        Map<String, Integer> choices = new HashMap<>();
        entry.specialLocalData = choices;
        List<TextArgumentBit> fixmes = new ArrayList<>();
        for (Object obj : innards) {
            if (!(obj instanceof Map)) {
                throw new ErrorInducedException("Entry to a 'choose' command is not a map: " + obj);
            }
            String name = ((String) ((Map) obj).keySet().iterator().next()).trim();
            List<Object> vals = (List<Object>) ((Map) obj).get(name);
            List<CommandEntry> ce = CommandScriptSection.getEntries(scrName, vals, istart);
            entries.addAll(ce);
            if (name.startsWith("case")) {
                name = name.substring("case ".length());
                if ((name.endsWith("\"") && name.startsWith("\"")) || name.endsWith("\'") && name.startsWith("\'")) {
                    name = name.substring(1, name.length() - 1);
                }
                choices.put(CoreUtilities.toLowerCase(name), istart);
            }
            else if (CoreUtilities.toLowerCase(name).equals("default")) {
                choices.put("\0DEFAULT", istart);
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
            queue.handleError(entry, "Mis-constructed choose command?");
            return;
        }
        String choice = CoreUtilities.toLowerCase(entry.getArgumentObject(queue, 0).toString());
        Map<String, Integer> choices = (Map<String, Integer>) entry.specialLocalData;
        Integer ix = choices.get(choice);
        if (ix == null) {
            ix = choices.get("\0DEFAULT");
            if (ix == null) {
                if (queue.shouldShowGood()) {
                    queue.outGood("No matching choice for CHOOSE command.");
                }
                queue.commandStack.peek().goTo(entry.blockEnd + 1);
                return;
            }
            if (queue.shouldShowGood()) {
                queue.outGood("DEFAULT choice for CHOOSE command.");
            }
        }
        else {
            if (queue.shouldShowGood()) {
                queue.outGood("Found matching choice for CHOOSE command.");
            }
        }
        queue.commandStack.peek().goTo(ix);
    }
}
