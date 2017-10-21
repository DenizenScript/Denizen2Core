package com.denizenscript.denizen2core.tags.handlers;

import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.objects.MapTag;
import com.denizenscript.denizen2core.tags.AbstractTagBase;

public class MapTagBase extends AbstractTagBase {

    // <--[tagbase]
// @Since 0.3.0
    // @Base map[<MapTag>]
    // @Group Common Base Types
    // @ReturnType MapTag
    // @Returns the input as a MapTag.
    // -->

    @Override
    public String getName() {
        return "map";
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        return MapTag.getFor(data.error, data.getNextModifier()).handle(data.shrink());
    }
}
