package org.mcmonkey.denizen2core.commands.queuecommands;

import org.mcmonkey.denizen2core.commands.AbstractCommand;
import org.mcmonkey.denizen2core.commands.CommandEntry;
import org.mcmonkey.denizen2core.commands.CommandQueue;
import org.mcmonkey.denizen2core.commands.CommandStackEntry;
import org.mcmonkey.denizen2core.utilities.debugging.Debug;

import java.util.ArrayList;
import java.util.List;

public class IfCommand extends AbstractCommand {

    public static class IfCommandData {
        public int result = 0;
    }

    // <--[command]
    // @Name if
    // @Arguments <if comparisons>
    // @Short runs a block of code if-and-only-if the comparisons return true.
    // @Updated 2016/04/18
    // @Authors mcmonkey
    // @Group Queue
    // @Minimum 1
    // @Maximum -1
    // @Description
    // runs a block of code if-and-only-if the comparisons return true.
    // TODO: Explain more!
    // @Example
    // # This example always echoes "hi".
    // - if true:
    //   - echo "hi"
    // @Tags
    // None.
    // -->

    @Override
    public String getName() {
        return "if";
    }

    @Override
    public String getArguments() {
        return "<if comparisons>";
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
    public boolean isWaitable() {
        return false;
    }

    @Override
    public void execute(CommandQueue queue, CommandEntry entry) {
        if (entry.arguments.get(0).toString().equals("\0CALLBACK")) {
            CommandStackEntry cse = queue.commandStack.peek();
            CommandEntry ifentry = cse.entries[entry.blockStart - 1];
            entry.setData(queue, ifentry.getData(queue));
            if (cse.getIndex() < cse.entries.length) {
                CommandEntry elseentry = cse.entries[cse.getIndex()];
                if (elseentry.command instanceof ElseCommand) {
                    elseentry.setData(queue, ifentry.getData(queue));
                }
            }
            return;
        }
        IfCommandData dat = new IfCommandData();
        dat.result = 0;
        entry.setData(queue, dat);
        List<String> parsedargs = new ArrayList<>(entry.arguments.size());
        for (int i = 0; i < entry.arguments.size(); i++) {
            parsedargs.add(entry.getArgumentObject(queue, i).toString()); // TODO: Don't pre-parse. Parse in TryIf.
        }
        boolean success = parsedargs.size() > 0 && parsedargs.get(0).equalsIgnoreCase("true"); // TODO: TryIf
        if (success) {
            if (queue.shouldShowGood()) {
                queue.outGood("If is true, executing...");
            }
            ((IfCommandData) entry.getData(queue)).result = 1;
        }
        else {
            if (queue.shouldShowGood()) {
                queue.outGood("If is false, doing nothing!");
            }
            queue.commandStack.peek().goTo(entry.blockEnd + 1);
        }
    }
}
