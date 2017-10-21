package com.denizenscript.denizen2core.tags.handlers;

import com.denizenscript.denizen2core.tags.AbstractTagBase;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.objects.DurationTag;

public class DurationTagBase extends AbstractTagBase {

    // <--[tagbase]
    // @Since 0.3.0
    // @Base duration[<DurationTag>]
    // @Group Common Base Types
    // @ReturnType DurationTag
    // @Returns the input number as a DurationTag.
    // -->

    @Override
    public String getName() {
        return "duration";
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        return DurationTag.getFor(data.error, data.getNextModifier()).handle(data.shrink());
    }
}
