package com.denizenscript.denizen2core.commands.queuecommands;

import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.commands.AbstractCommand;
import com.denizenscript.denizen2core.commands.CommandEntry;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.commands.CommandScriptSection;
import com.denizenscript.denizen2core.scripts.commontypes.TaskScript;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.objects.MapTag;
import com.denizenscript.denizen2core.utilities.debugging.ColorSet;
import com.denizenscript.denizen2core.scripts.CommandScript;
import com.denizenscript.denizen2core.tags.objects.QueueTag;
import com.denizenscript.denizen2core.utilities.CoreUtilities;

import java.util.List;

public class RunCommand extends AbstractCommand {

    // <--[command]
// @Since 0.3.0
    // @Name run
    // @Arguments <script> [definition map]
    // @Short Runs a script as a new queue.
    // @Updated 2016/04/06
    // @Group Queue
    // @Minimum 1
    // @Maximum 2
    // @Tag <[run_queue]> (QueueTag) returns the ran queue.
    // @Save run_queue (QueueTag) returns the queue that was created and ran.
    // @Description
    // Runs a script as a new queue.
    // Optionally add definitions to pass to the new queue.
    // TODO: Explain more!
    // @Example
    // # This example runs the script "test".
    // - run test
    // @Example
    // # This example runs the script "test" and echoes back its results after it finishes.
    // - &run test
    // - echo <[run_queue].determinations>
    // @Example
    // # This example runs the script "mytask" with definitions "banana" (set to "2")
    // # and "potato" (set to "5").
    // - run mytask banana:2|potato:5
    // -->

    @Override
    public String getName() {
        return "run";
    }

    @Override
    public String getArguments() {
        return "<script> [definition map]";
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
        CommandScriptSection section = task.getSection(bits.size() > 1 ? bits.get(1) : null);
        if (section == null) {
            queue.handleError(entry, "Invalid script section!");
            return;
        }
        if (queue.shouldShowGood()) {
            queue.outGood("Running script: " + ColorSet.emphasis + script.title);
        }
        CommandQueue nq = section.toQueue();
        if (entry.waitFor) {
            nq.onStop = (nqueue) -> queue.waitFor(null);
        }
        if (entry.arguments.size() > 1) {
            MapTag defs = MapTag.getFor(queue.error, entry.getArgumentObject(queue, 1));
            nq.commandStack.peek().definitions.putAll(defs.getInternal());
        }
        nq.sender = queue.sender;
        nq.start();
        queue.commandStack.peek().setDefinition(entry.resName(queue, "run_queue"), new QueueTag(nq));
    }
}
