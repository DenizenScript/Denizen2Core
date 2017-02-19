package com.denizenscript.denizen2core.commands.queuecommands;

import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.commands.CommandStackEntry;

import java.util.ArrayList;

public class WhileCommand extends AbstractCommand {

    // <--[command]
    // @Name while
    // @Arguments 'start'/'stop'/'next' [if comparisons]
    // @Short runs a block of code repeatedly for so long as the comparisons return true.
    // @Updated 2017/02/19
    // @Group Queue
    // @Procedural true
    // @Minimum 1
    // @Maximum -1
    // @Description
    // Runs a block of code repeatedly for so long as the comparisons return true.
    // TODO: Explain more!
    // @Example
    // # This example runs forever (until it's externally stopped, or the engine shuts down)
    // # and echoes "hi" every half second.
    // - while start true:
    //   - echo "hi"
    //   - wait 0.5s
    // -->

    @Override
    public String getName() {
        return "while";
    }

    @Override
    public String getArguments() {
        return "'start'/'stop'/'next' [if comparisons]";
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
        if (entry.arguments.get(0).toString().equals("\0CALLBACK")) {
            CommandEntry orig = queue.commandStack.peek().entries[entry.blockStart - 1];
            IfCommand.TryIfHelper helper = new IfCommand.TryIfHelper();
            helper.queue = queue;
            helper.entry = orig;
            helper.arguments = new ArrayList<>(orig.arguments);
            helper.arguments.remove(0);
            boolean success = IfCommand.tryIf(helper);
            if (success) {
                if (queue.shouldShowGood()) {
                    queue.outGood("While continuing...");
                }
                queue.commandStack.peek().goTo(entry.blockStart);
            }
            else {
                if (queue.shouldShowGood()) {
                    queue.outGood("While completed!");
                }
            }
            return;
        }
        String type = entry.getArgumentObject(queue, 0).toString();
        if (type.equals("start")) {
            IfCommand.TryIfHelper helper = new IfCommand.TryIfHelper();
            helper.queue = queue;
            helper.entry = entry;
            helper.arguments = new ArrayList<>(entry.arguments);
            helper.arguments.remove(0);
            boolean success = IfCommand.tryIf(helper);
            if (!success) {
                if (queue.shouldShowGood()) {
                    queue.outGood("While is false, skipping.");
                }
                queue.commandStack.peek().goTo(entry.blockEnd + 1);
                return;
            }
            if (queue.shouldShowGood()) {
                queue.outGood("While is true, looping...");
            }
        }
        else if (type.equals("stop")) {
            CommandStackEntry cse = queue.commandStack.peek();
            for (int i = cse.getIndex(); i < cse.entries.length; i++) {
                if (cse.entries[i].command instanceof WhileCommand && cse.entries[i].arguments.get(0).toString().equals("\0CALLBACK")) {
                    if (queue.shouldShowGood()) {
                        queue.outGood("Stopping a while loop.");
                    }
                    cse.goTo(i + 1);
                    return;
                }
            }
            queue.handleError(entry, "Cannot stop while: not in one!");
        }
        else if (type.equals("next")) {
            CommandStackEntry cse = queue.commandStack.peek();
            for (int i = cse.getIndex(); i < cse.entries.length; i++) {
                if (cse.entries[i].command instanceof WhileCommand && cse.entries[i].arguments.get(0).toString().equals("\0CALLBACK")) {
                    if (queue.shouldShowGood()) {
                        queue.outGood("Jumping forward in a while loop.");
                    }
                    cse.goTo(i);
                    return;
                }
            }
            queue.handleError(entry, "Cannot advance while: not in one!");
        }
    }
}
