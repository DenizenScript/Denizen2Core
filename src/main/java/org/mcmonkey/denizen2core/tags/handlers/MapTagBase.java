package org.mcmonkey.denizen2core.tags.handlers;

import org.mcmonkey.denizen2core.tags.AbstractTagBase;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.TagData;
import org.mcmonkey.denizen2core.tags.objects.MapTag;

public class MapTagBase extends AbstractTagBase {

    // <--[tagbase]
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
