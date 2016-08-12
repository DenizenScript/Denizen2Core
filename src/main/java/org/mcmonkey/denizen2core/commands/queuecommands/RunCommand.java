package org.mcmonkey.denizen2core.commands.queuecommands;

import org.mcmonkey.denizen2core.Denizen2Core;
import org.mcmonkey.denizen2core.commands.*;
import org.mcmonkey.denizen2core.scripts.CommandScript;
import org.mcmonkey.denizen2core.scripts.commontypes.TaskScript;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.objects.IntegerTag;
import org.mcmonkey.denizen2core.tags.objects.MapTag;
import org.mcmonkey.denizen2core.tags.objects.QueueTag;
import org.mcmonkey.denizen2core.utilities.CoreUtilities;
import org.mcmonkey.denizen2core.utilities.debugging.ColorSet;

import java.util.List;

public class RunCommand extends AbstractCommand {

    // <--[command]
    // @Name run
    // @Arguments <script> [definition map]
    // @Short Runs a script as a new queue.
    // @Updated 2016/04/06
    // @Authors mcmonkey
    // @Group Queue
    // @Minimum 1
    // @Maximum 2
    // @Tag <def[run_queue]> (IntegerTag) returns the qID of the ran queue.
    // @Description
    // Runs a script as a new queue. Can pass definitions.
    // TODO: Explain more!
    // @Example
    // # This example runs the script "test".
    // - run test
    // @Example
    // # This example runs the script "myTask" with definitions "banana"
    // and "potato" of values "2" and "5".
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
        CommandScriptSection section = task.getSection(bits.size() > 1 ? bits.get(1): null);
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
        nq.start();
        queue.commandStack.peek().setDefinition("run_queue", new QueueTag(nq));
    }
}
