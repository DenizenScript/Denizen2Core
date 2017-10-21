package com.denizenscript.denizen2core.tags.handlers;

import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.objects.MapTag;
import com.denizenscript.denizen2core.tags.objects.NullTag;
import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.scripts.CommandScript;
import com.denizenscript.denizen2core.scripts.commontypes.ProcedureScript;
import com.denizenscript.denizen2core.tags.AbstractTagBase;
import com.denizenscript.denizen2core.utilities.CoreUtilities;

import java.util.HashMap;

public class ProcedureTagBase extends AbstractTagBase {

    // <--[tagbase]
    // @Since 0.3.0
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
        context.remove("path");
        queue.commandStack.peek().setDefinition("context", new MapTag(context));
        queue.start();
        return new MapTag(queue.determinations.getInternal()).handle(data.shrink());
    }
}
