package com.denizenscript.denizen2core.tags.handlers;

import com.denizenscript.denizen2core.tags.AbstractTagBase;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.objects.BooleanTag;

public class DefExistsTagBase extends AbstractTagBase {

    // <--[tagbase]
    // @Since 0.3.0
    // @Base def_exists[<TextTag>]
    // @Group Definitions
    // @ReturnType BooleanTag
    // @Returns whether a current definition by the given name exists.
    // -->

    @Override
    public String getName() {
        return "def_exists";
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        String def = data.getNextModifier().toString();
        AbstractTagObject obj = data.variables.get(def);
        if (obj == null) {
            obj = data.currentQueue.commandStack.peek().getDefinition(def);
        }
        if (obj == null) {
            return BooleanTag.getForBoolean(false).handle(data.shrink());
        }
        return BooleanTag.getForBoolean(true).handle(data.shrink());
    }
}
