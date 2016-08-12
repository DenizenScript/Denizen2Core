package org.mcmonkey.denizen2core.tags.handlers;

import org.mcmonkey.denizen2core.Denizen2Core;
import org.mcmonkey.denizen2core.commands.CommandQueue;
import org.mcmonkey.denizen2core.scripts.CommandScript;
import org.mcmonkey.denizen2core.scripts.commontypes.ProcedureScript;
import org.mcmonkey.denizen2core.tags.AbstractTagBase;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.TagData;
import org.mcmonkey.denizen2core.tags.objects.MapTag;
import org.mcmonkey.denizen2core.tags.objects.NullTag;
import org.mcmonkey.denizen2core.utilities.CoreUtilities;

import java.util.HashMap;

public class ProcedureTagBase extends AbstractTagBase{

    // <--[tagbase]
    // @Base procedure[<MapTag>]
    // @Group Scripts
    // @ReturnType MapTag
    // @Returns the result of a procedure script.
    // -->

    @Override
    public String getName() {
        return "procedure";
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        MapTag mt = MapTag.getFor(data.error, data.getNextModifier());
        if (!mt.getInternal().containsKey("script")) {
            data.error.run("Missing script: setting in the procedure tag modifier!");
            return new NullTag();
        }
        String sname = CoreUtilities.toLowerCase(mt.getInternal().get("script").toString());
        CommandScript script = Denizen2Core.currentScripts.get(sname);
        if (script == null || !(script instanceof ProcedureScript)) {
            data.error.run("Invalid procedure script name!");
            return new NullTag();
        }
        ProcedureScript pscript = (ProcedureScript) script;
        CommandQueue queue = pscript.getSection(mt.getInternal().containsKey("path") ?
                CoreUtilities.toLowerCase(mt.getInternal().get("path").toString()) : null).toQueue();
        queue.procedural = true;
        HashMap<String, AbstractTagObject> context = new HashMap<>(mt.getInternal());
        context.remove("script");
        queue.commandStack.peek().setDefinition("context", new MapTag(context));
        queue.start();
        return new MapTag(queue.determinations.getInternal()).handle(data.shrink());
    }
}
