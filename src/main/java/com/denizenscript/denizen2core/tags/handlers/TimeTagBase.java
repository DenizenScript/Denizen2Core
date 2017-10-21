package com.denizenscript.denizen2core.tags.handlers;

import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.objects.TimeTag;
import com.denizenscript.denizen2core.tags.AbstractTagBase;
import com.denizenscript.denizen2core.tags.AbstractTagObject;

public class TimeTagBase extends AbstractTagBase {

    // <--[tagbase]
    // @Since 0.3.0
    // @Base time[<TimeTag>]
    // @Group Common Base Types
    // @ReturnType TimeTag
    // @Returns the input number as a TimeTag.
    // -->

    @Override
    public String getName() {
        return "time";
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        return TimeTag.getFor(data.error, data.getNextModifier()).handle(data.shrink());
    }
}
