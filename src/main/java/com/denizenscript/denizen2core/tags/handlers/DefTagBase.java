package com.denizenscript.denizen2core.tags.handlers;

import com.denizenscript.denizen2core.tags.AbstractTagBase;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.objects.NullTag;

public class DefTagBase extends AbstractTagBase {

    // <--[tagbase]
    // @Base def[<TextTag>]
    // @Group Definitions
    // @ReturnType Dynamic
    // @Returns a current definition by the given name.
    // -->

    @Override
    public String getName() {
        return "def";
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        String def = data.getNextModifier().toString();
        AbstractTagObject obj = data.variables.get(def);
        if (obj == null) {
            obj = data.currentQueue.commandStack.peek().getDefinition(def);
        }
        if (obj == null) {
            data.error.run("Invalid definition name: '" + def + "'!");
            return new NullTag();
        }
        return obj.handle(data.shrink());
    }
}
