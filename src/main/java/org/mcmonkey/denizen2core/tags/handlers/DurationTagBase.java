package org.mcmonkey.denizen2core.tags.handlers;

import org.mcmonkey.denizen2core.tags.AbstractTagBase;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.TagData;
import org.mcmonkey.denizen2core.tags.objects.DurationTag;

public class DurationTagBase extends AbstractTagBase {

    // <--[tagbase]
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
