package com.denizenscript.denizen2core.tags.handlers;

import com.denizenscript.denizen2core.tags.AbstractTagBase;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.objects.BooleanTag;

public class BooleanTagBase extends AbstractTagBase {

    // <--[tagbase]
    // @Since 0.3.0
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
