package org.mcmonkey.denizen2core.tags.handlers;

import org.mcmonkey.denizen2core.tags.AbstractTagBase;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.TagData;
import org.mcmonkey.denizen2core.tags.objects.NullTag;

public class DefTagBase extends AbstractTagBase {

    // <--[tagbase]
    // @Base def[<DefinitionName>]
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
        AbstractTagObject obj = data.currentQueue.commandStack.peek().getDefinition(data.getNextModifier().toString());
        if (obj == null) {
            data.error.run("Invalid definition name!");
            return new NullTag();
        }
        return obj.handle(data.shrink());
    }
}
