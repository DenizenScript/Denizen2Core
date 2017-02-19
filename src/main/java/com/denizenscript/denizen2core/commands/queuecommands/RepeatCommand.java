package com.denizenscript.denizen2core.commands.queuecommands;

import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.commands.CommandStackEntry;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.objects.IntegerTag;
import com.denizenscript.denizen2core.utilities.debugging.ColorSet;
import com.denizenscript.denizen2core.utilities.CoreUtilities;

public class RepeatCommand extends AbstractCommand {

    public static class RepeatCommandData {
        public int current = 0;
        public int end = 0;
        public String resName;
    }

    // <--[command]
    // @Name repeat
    // @Arguments "stop"/"next"/<times>
    // @Short runs a block of code the specified number of types.
    // @Updated 2016/08/07
    // @Group Queue
    // @Procedural true
    // @Minimum 1
    // @Maximum 1
    // @Tag <def[repeat_index]> (IntegerTag) returns the current index in the loop.
    // @Tag <def[repeat_total]> (IntegerTag) returns the value the repeat is counting up to.
    // @Save repeat_index (IntegerTag) returns the current index in the loop.
    // @Description
    // Runs a block of code the specified number of types.
    // TODO: Explain more!
    // @Example
    // # This example echoes "hi" three times.
    // - repeat 3:
    //   - echo "hi"
    // -->

    @Override
    public String getName() {
        return "repeat";
    }

    @Override
    public String getArguments() {
        return "'stop'/'continue'/<times>";
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
        if (entry.arguments.get(0).toString().equals("\0CALLBACK")) {
            RepeatCommandData rcd = (RepeatCommandData) queue.commandStack.peek().entries[entry.blockStart - 1].getData(queue);
            rcd.current++;
            queue.commandStack.peek().setDefinition(rcd.resName, new IntegerTag(rcd.current));
            queue.commandStack.peek().setDefinition("repeat_total", new IntegerTag(rcd.end));
            if (rcd.current <= rcd.end) {
                if (queue.shouldShowGood()) {
                    queue.outGood("Repeating " + ColorSet.emphasis + rcd.current + "/" + rcd.end);
                }
                queue.commandStack.peek().goTo(entry.blockStart);
            }
            else {
                if (queue.shouldShowGood()) {
                    queue.outGood("Repeat completed!");
                }
            }
            return;
        }
        AbstractTagObject cobj = entry.getArgumentObject(queue, 0);
        String val = "";
        if (!(cobj instanceof IntegerTag)) {
            val = CoreUtilities.toLowerCase(cobj.toString());
        }
        if (val.equals("stop")) {
            CommandStackEntry cse = queue.commandStack.peek();
            for (int i = cse.getIndex(); i < cse.entries.length; i++) {
                if (cse.entries[i].command instanceof RepeatCommand && cse.entries[i].arguments.get(0).toString().equals("\0CALLBACK")) {
                    if (queue.shouldShowGood()) {
                        queue.outGood("Stopping a repeat loop.");
                    }
                    cse.goTo(i + 1);
                    return;
                }
            }
            queue.handleError(entry, "Cannot stop repeat: not in one!");
        }
        else if (val.equals("next")) {
            CommandStackEntry cse = queue.commandStack.peek();
            for (int i = cse.getIndex(); i < cse.entries.length; i++) {
                if (cse.entries[i].command instanceof RepeatCommand && cse.entries[i].arguments.get(0).toString().equals("\0CALLBACK")) {
                    if (queue.shouldShowGood()) {
                        queue.outGood("Jumping forward in a repeat loop.");
                    }
                    cse.goTo(i);
                    return;
                }
            }
            queue.handleError(entry, "Cannot advance repeat: not in one!");
        }
        else {
            IntegerTag itag = IntegerTag.getFor(queue.error, cobj);
            if (itag.getInternal() <= 0) {
                if (queue.shouldShowGood()) {
                    queue.outGood("Repeat number is 0, skipping.");
                }
                queue.commandStack.peek().goTo(entry.blockEnd + 2);
                return;
            }
            RepeatCommandData rcd = new RepeatCommandData();
            rcd.current = 1;
            rcd.end = (int) itag.getInternal();
            rcd.resName = entry.resName(queue, "repeat_index");
            entry.setData(queue, rcd);
            queue.commandStack.peek().setDefinition(rcd.resName, new IntegerTag(rcd.current));
            queue.commandStack.peek().setDefinition("repeat_total", new IntegerTag(rcd.end));
            if (queue.shouldShowGood()) {
                queue.outGood("Repeat number is " + ColorSet.emphasis + itag.getInternal() + ColorSet.good + ", repeating...");
            }
        }
    }
}
