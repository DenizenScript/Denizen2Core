package com.denizenscript.denizen2core.tags.handlers;

import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.scripts.CommandScript;
import com.denizenscript.denizen2core.scripts.commontypes.TaskScript;
import com.denizenscript.denizen2core.tags.AbstractTagBase;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.objects.MapTag;
import com.denizenscript.denizen2core.tags.objects.NullTag;
import com.denizenscript.denizen2core.utilities.CoreUtilities;

import java.util.HashMap;

public class TaskTagBase extends AbstractTagBase {

    // <--[tagbase]
// @Since 0.3.0
    // @Base task[<MapTag>]
    // @Group Scripts
    // @ReturnType MapTag
    // @Returns the result of a taggable task script.
    // -->

    @Override
    public String getName() {
        return "task";
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        MapTag mt = MapTag.getFor(data.error, data.getNextModifier());
        if (!mt.getInternal().containsKey("script")) {
            data.error.run("Missing script: setting in the task tag modifier!");
            return new NullTag();
        }
        String sname = CoreUtilities.toLowerCase(mt.getInternal().get("script").toString());
        CommandScript script = Denizen2Core.currentScripts.get(sname);
        if (script == null || !(script instanceof TaskScript)) {
            data.error.run("Invalid task script name!");
            return new NullTag();
        }
        TaskScript pscript = (TaskScript) script;
        if (pscript.isTaggable == TaskScript.TaggableType.NONE) {
            data.error.run("Task script specified is not taggable!");
            return new NullTag();
        }
        // TODO: Implemenet always vs. on?!
        CommandQueue queue = pscript.getSection(mt.getInternal().containsKey("path") ?
                CoreUtilities.toLowerCase(mt.getInternal().get("path").toString()) : null).toQueue();
        HashMap<String, AbstractTagObject> context = new HashMap<>(mt.getInternal());
        context.remove("script");
        context.remove("path");
        queue.commandStack.peek().setDefinition("context", new MapTag(context));
        queue.start();
        return new MapTag(queue.determinations.getInternal()).handle(data.shrink());
    }
}
