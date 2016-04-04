package org.mcmonkey.denizen2core.tags.handlers;

import org.mcmonkey.denizen2core.tags.AbstractTagBase;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.TagData;
import org.mcmonkey.denizen2core.tags.objects.IntegerTag;

public class IntegerTagBase extends AbstractTagBase {

    // <--[tagbase]
    // @Base integer[<IntegerTag>]
    // @Group Common Base Types
    // @ReturnType IntegerTag
    // @Returns the input number as a IntegerTag.
    // -->

    @Override
    public String getName() {
        return "integer";
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        return IntegerTag.getFor(data.error, data.getNextModifier()).handle(data.shrink());
    }
}
