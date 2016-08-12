package org.mcmonkey.denizen2core.tags.handlers;

import org.mcmonkey.denizen2core.tags.AbstractTagBase;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.TagData;
import org.mcmonkey.denizen2core.tags.objects.NullTag;
import org.mcmonkey.denizen2core.tags.objects.ScriptTag;

public class ScriptTagBase extends AbstractTagBase {

    // <--[tagbase]
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
            return new NullTag();
        }
        return ScriptTag.getFor(data.error, data.getNextModifier()).handle(data.shrink());
    }
}
