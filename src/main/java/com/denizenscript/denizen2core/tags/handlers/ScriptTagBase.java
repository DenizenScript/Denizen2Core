package com.denizenscript.denizen2core.tags.handlers;

import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.objects.NullTag;
import com.denizenscript.denizen2core.tags.objects.ScriptTag;
import com.denizenscript.denizen2core.tags.AbstractTagBase;

public class ScriptTagBase extends AbstractTagBase {

    // <--[tagbase]
    // @Since 0.3.0
    // @Base script[<ScriptTag>]
    // @Modifier optional
    // @Group Definitions
    // @ReturnType ScriptTag
    // @Returns the input as a script, or the current script if none is specified.
    // -->

    @Override
    public String getName() {
        return "script";
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        if (!data.hasNextModifier()) {
            if (data.currentQueue != null
                    && data.currentQueue.commandStack.size() > 0
                    && data.currentQueue.commandStack.peek().originalScript != null) {
                return new ScriptTag(data.currentQueue.commandStack.peek().originalScript).handle(data.shrink());
            }
            data.error.run("No current script available!");
            return NullTag.NULL;
        }
        return ScriptTag.getFor(data.error, data.getNextModifier()).handle(data.shrink());
    }
}
