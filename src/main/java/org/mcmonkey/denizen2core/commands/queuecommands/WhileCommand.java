package org.mcmonkey.denizen2core.commands.queuecommands;

import org.mcmonkey.denizen2core.commands.AbstractCommand;
import org.mcmonkey.denizen2core.commands.CommandEntry;
import org.mcmonkey.denizen2core.commands.CommandQueue;

public class WhileCommand extends AbstractCommand {

    // <--[command]
    // @Name while
    // @Arguments <if comparisons>
    // @Short runs a block of code repeatedly for so long as the comparisons return true.
    // @Updated 2016/08/08
    // @Group Queue
    // @Procedural true
    // @Minimum 1
    // @Maximum -1
    // @Description
    // Runs a block of code repeatedly for so long as the comparisons return true.
    // TODO: Explain more!
    // @Example
    // # This example runs forever, and echoes "hi" every half second.
    // - while true:
    //   - echo "hi"
    //   - wait 0.5s
    // -->

    @Override
    public String getName() {
        return "while";
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
            helper.arguments = orig.arguments;
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
        IfCommand.TryIfHelper helper = new IfCommand.TryIfHelper();
        helper.queue = queue;
        helper.entry = entry;
        helper.arguments = entry.arguments;
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
}
