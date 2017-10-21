package com.denizenscript.denizen2core.tags.handlers;

import com.denizenscript.denizen2core.tags.AbstractTagBase;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.objects.IntegerTag;

public class IntegerTagBase extends AbstractTagBase {

    // <--[tagbase]
// @Since 0.3.0
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
