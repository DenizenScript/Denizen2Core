package com.denizenscript.denizen2core.tags.objects;

import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.utilities.Action;
import com.denizenscript.denizen2core.utilities.Function2;

import java.util.HashMap;

public class NullTag extends AbstractTagObject {

    // <--[object]
    // @Type NullTag
    // @SubType TextTag
    // @Group Mathematics
    // @Description Represents a null value. Identified as exactly "&{NULL}".
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

    public static NullTag getFor(Action<String> error, String text) {
        return new NullTag();
    }

    public static NullTag getFor(Action<String> error, AbstractTagObject text) {
        return (text instanceof NullTag) ? (NullTag) text : getFor(error, text.toString());
    }

    @Override
    public String getTagTypeName() {
        return "NullTag";
    }

    @Override
    public String toString() {
        return "&{NULL}";
    }
}
