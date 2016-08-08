package org.mcmonkey.denizen2core.tags.handlers;

import org.mcmonkey.denizen2core.tags.AbstractTagBase;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.TagData;
import org.mcmonkey.denizen2core.tags.objects.ListTag;

public class ListTagBase extends AbstractTagBase {

    // <--[tagbase]
    // @Base list[<ListTag>]
    // @Group Definitions
    // @ReturnType ListTag
    // @Returns the input as a ListTag.
    // -->

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        return ListTag.getFor(data.error, data.getNextModifier()).handle(data.shrink());
    }
}
