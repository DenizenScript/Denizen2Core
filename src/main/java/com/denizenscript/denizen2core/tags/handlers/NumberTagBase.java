package com.denizenscript.denizen2core.tags.handlers;

import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.AbstractTagBase;
import com.denizenscript.denizen2core.tags.objects.NumberTag;

public class NumberTagBase extends AbstractTagBase {

    // <--[tagbase]
// @Since 0.3.0
    // @Base number[<NumberTag>]
    // @Group Common Base Types
    // @ReturnType NumberTag
    // @Returns the input number as a NumberTag.
    // -->

    @Override
    public String getName() {
        return "number";
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        return NumberTag.getFor(data.error, data.getNextModifier()).handle(data.shrink());
    }
}
