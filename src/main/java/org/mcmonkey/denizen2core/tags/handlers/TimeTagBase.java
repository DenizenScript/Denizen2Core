package org.mcmonkey.denizen2core.tags.handlers;

import org.mcmonkey.denizen2core.tags.AbstractTagBase;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.TagData;
import org.mcmonkey.denizen2core.tags.objects.TimeTag;

public class TimeTagBase extends AbstractTagBase {

    // <--[tagbase]
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
