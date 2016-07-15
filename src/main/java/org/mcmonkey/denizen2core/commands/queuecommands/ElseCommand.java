package org.mcmonkey.denizen2core.commands.queuecommands;

import org.mcmonkey.denizen2core.commands.AbstractCommand;
import org.mcmonkey.denizen2core.commands.CommandEntry;
import org.mcmonkey.denizen2core.commands.CommandQueue;
import org.mcmonkey.denizen2core.commands.CommandStackEntry;
import org.mcmonkey.denizen2core.utilities.CoreUtilities;

import java.util.ArrayList;
import java.util.List;

public class ElseCommand extends AbstractCommand {

    // <--[command]
    // @Name else
    // @Arguments ['if' <if comparisons>]
    // @Short runs a block of code if-and-only-if the comparisons return true, or no comparison is specified, and the preceding if did not run.
    // @Updated 2016/04/18
    // @Authors mcmonkey
    // @Group Queue
    // @Minimum 0
    // @Maximum -1
    // @Description
    // Runs a block of code if-and-only-if the comparisons return true, or no comparison is specified, and the preceding if did not run.
    // TODO: Explain more!
    // @Example
    // # This example always echoes "hi".
    // - if true:
    //   - echo "nope"
    // - else:
    //   - echo "hi"
    // -->

    @Override
    public String getName() {
        return "else";
    }

    @Override
    public String getArguments() {
        return "['if' <if comparisons>]";
    }

    @Override
    public int getMinimumArguments() {
        return 0;
    }

    @Override
    public int getMaximumArguments() {
        return -1;
    }

    @Override
    public boolean isWaitable() {
        return false;
    }

    @Override
    public void execute(CommandQueue queue, CommandEntry entry) {
        if (entry.arguments.size() > 0 && entry.arguments.get(0).toString().equals("\0CALLBACK")) {
            CommandStackEntry cse = queue.commandStack.peek();
            CommandEntry ifentry = cse.entries[entry.blockStart - 1];
            if (cse.getIndex() < cse.entries.length) {
                CommandEntry elseentry = cse.entries[cse.getIndex()];
                if (elseentry.command instanceof ElseCommand) {
                    elseentry.setData(queue, ifentry.getData(queue));
                }
            }
            return;
        }
        if (!(entry.getData(queue) instanceof IfCommand.IfCommandData)) {
            queue.handleError(entry, "ELSE invalid, IF did not precede.");
            return;
        }
        IfCommand.IfCommandData data = (IfCommand.IfCommandData)entry.getData(queue);
        if (data.result == 1) {
            if (queue.shouldShowGood()) {
                queue.outGood("Else continuing, previous IF passed.");
            }
            queue.commandStack.peek().goTo(entry.blockEnd + 1);
            return;
        }
        boolean success = true;
        if (entry.arguments.size() >= 1) {
            String ifbit = entry.getArgumentObject(queue, 0).toString();
            if (!CoreUtilities.toLowerCase(ifbit).equals("if")) {
                queue.handleError(entry, "Invalid ELSE command!");
                return;
            }
            else {
                List<String> parsedargs = new ArrayList<>(entry.arguments.size());
                for (int i = 1; i < entry.arguments.size(); i++) {
                    parsedargs.add(entry.getArgumentObject(queue, i).toString()); // TODO: Don't pre-parse. Parse in TryIf.
                }
                success = parsedargs.size() > 0 && parsedargs.get(0).equalsIgnoreCase("true"); // TODO: TryIf
            }
        }
        if (success) {
            if (queue.shouldShowGood()) {
                queue.outGood("Else [if] is true, executing...");
            }
            data.result = 1;
        }
        else {
            if (queue.shouldShowGood()) {
                queue.outGood("Else continuing, ELSE-IF is false!");
            }
            queue.commandStack.peek().goTo(entry.blockEnd + 1);
        }
    }
}
