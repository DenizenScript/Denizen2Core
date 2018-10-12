package com.denizenscript.denizen2core.commands.queuecommands;

import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.commands.CommandStackEntry;
import com.denizenscript.denizen2core.utilities.CoreUtilities;

import java.util.ArrayList;

public class ElseCommand extends AbstractCommand {

    // <--[command]
    // @Since 0.3.0
    // @Name else
    // @Arguments ["if" <if comparisons>]
    // @Short runs a block of code if-and-only-if the comparisons return true, or no comparison is specified, and the preceding if did not run.
    // @Updated 2016/04/18
    // @Group Queue
    // @Procedural true
    // @Minimum 0
    // @Maximum -1
    // @Description
    // Runs a block of code if-and-only-if the comparisons return true,
    // or no comparison is specified, and the preceding <@link command if>if<@/link> did not run.
    // TODO: Explain more!
    // @Example
    // # This example always echoes "hi".
    // - if false:
    //   - echo "This won't show"
    // - else:
    //   - echo "hi"
    // @Example
    // # This example always echoes "hi".
    // - if true:
    //   - echo "hi"
    // - else:
    //   - echo "This won't show"
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
    public boolean isProcedural() {
        return true;
    }

    @Override
    public boolean allowsBlock() {
        return true;
    }

    @Override
    public void execute(CommandQueue queue, CommandEntry entry) {
        if (entry.arguments.size() > 0 && entry.arguments.get(0).toString().equals("\0CALLBACK")) {
            IfCommand.jumpToEnd(queue.commandStack.peek());
            return;
        }
        if (!(entry.getData(queue) instanceof IfCommand.IfCommandData)) {
            queue.handleError(entry, "ELSE invalid, IF did not precede.");
            return;
        }
        IfCommand.IfCommandData data = (IfCommand.IfCommandData) entry.getData(queue);
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
                IfCommand.TryIfHelper helper = new IfCommand.TryIfHelper();
                helper.queue = queue;
                helper.entry = entry;
                helper.arguments = new ArrayList<>(entry.arguments);
                helper.arguments.remove(0);
                success = IfCommand.tryIf(helper);
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
