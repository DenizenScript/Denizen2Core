package com.denizenscript.denizen2core.tags.handlers;

import com.denizenscript.denizen2core.tags.AbstractTagBase;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.objects.ListTag;

public class ListTagBase extends AbstractTagBase {

    // <--[tagbase]
    // @Since 0.3.0
    // @Base list[<ListTag>]
    // @Group Common Base Types
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
