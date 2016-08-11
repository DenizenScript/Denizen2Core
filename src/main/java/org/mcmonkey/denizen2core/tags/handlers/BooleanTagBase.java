package org.mcmonkey.denizen2core.tags.handlers;

import org.mcmonkey.denizen2core.tags.AbstractTagBase;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.TagData;
import org.mcmonkey.denizen2core.tags.objects.BooleanTag;

public class BooleanTagBase extends AbstractTagBase {

    // <--[tagbase]
    // @Base boolean[<BooleanTag>]
    // @Group Common Base Types
    // @ReturnType BooleanTag
    // @Returns the input as a BooleanTag.
    // -->

    @Override
    public String getName() {
        return "boolean";
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        return BooleanTag.getFor(data.error, data.getNextModifier()).handle(data.shrink());
    }
}
