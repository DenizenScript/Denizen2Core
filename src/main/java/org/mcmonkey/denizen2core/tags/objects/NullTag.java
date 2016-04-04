package org.mcmonkey.denizen2core.tags.objects;

import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.TagData;
import org.mcmonkey.denizen2core.utilities.Function2;

import java.util.HashMap;

public class NullTag extends AbstractTagObject {

    // <--[object]
    // @Type NullTag
    // @SubType TextTag
    // @Group Mathematics
    // @Description Represents a null value.
    // -->

    public final static HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> handlers = new HashMap<>();

    static {
    }

    @Override
    public HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> getHandlers() {
        return handlers;
    }

    @Override
    public AbstractTagObject handleElseCase(TagData data) {
        return new TextTag(toString());
    }

    @Override
    public String toString() {
        return "&{NULL}";
    }
}
