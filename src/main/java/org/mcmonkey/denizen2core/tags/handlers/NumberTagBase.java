package org.mcmonkey.denizen2core.tags.handlers;

import org.mcmonkey.denizen2core.tags.AbstractTagBase;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.TagData;
import org.mcmonkey.denizen2core.tags.objects.NumberTag;

public class NumberTagBase extends AbstractTagBase {

    // <--[tagbase]
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
