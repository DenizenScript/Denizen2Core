package org.mcmonkey.denizen2core.commands.queuecommands;

import org.mcmonkey.denizen2core.Denizen2Core;
import org.mcmonkey.denizen2core.commands.*;
import org.mcmonkey.denizen2core.scripts.CommandScript;
import org.mcmonkey.denizen2core.scripts.commontypes.TaskScript;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.objects.IntegerTag;
import org.mcmonkey.denizen2core.utilities.CoreUtilities;

import java.util.List;

public class RunCommand extends AbstractCommand {

    // <--[command]
    // @Name run
    // @Arguments <script>
    // @Short Runs a script as a new queue.
    // @Updated 2016/04/06
    // @Authors mcmonkey
    // @Group Queue
    // @Minimum 1
    // @Maximum 1
    // @Tag <def[run_queue]> (IntegerTag) returns the qID of the ran queue.
    // @Description
    // Runs a script as a new queue.
    // TODO: Explain more!
    // @Example
    // # This example runs the script "test".
    // - run test
    // -->

    @Override
    public String getName() {
        return "run";
    }

    @Override
    public String getArguments() {
        return "<script>";
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
    public boolean isWaitable() {
        return true;
    }

    @Override
    public void execute(CommandQueue queue, CommandEntry entry) {
        AbstractTagObject scriptobj = entry.getArgumentObject(queue, 0);
        String scriptName = scriptobj.toString();
        List<String> bits = CoreUtilities.split(scriptName, '.', 2);
        CommandScript script = Denizen2Core.currentScripts.get(CoreUtilities.toLowerCase(bits.get(0)));
        if (script == null) {
            queue.handleError(entry, "Invalid script name!");
            return;
        }
        if (!(script instanceof TaskScript)) {
            queue.handleError(entry, "Trying to run a non-task typed script!");
            return;
        }
        TaskScript task = (TaskScript) script;
        CommandScriptSection section = task.getSection(bits.size() > 1 ? bits.get(1): null);
        if (section == null) {
            queue.handleError(entry, "Invalid script section!");
            return;
        }
        if (queue.shouldShowGood()) {
            queue.outGood("Running script: " + script.title);
        }
        CommandQueue nq = section.toQueue();
        nq.start();
        // TODO: Queue tag.
        queue.commandStack.peek().setDefinition("run_queue", new IntegerTag(nq.qID));
        if (entry.waitFor) {
            queue.waitFor(null);
        }
        // TODO: Track variable edits, waiting
    }
}
