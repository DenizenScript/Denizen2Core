package com.denizenscript.denizen2core.tags.objects;

import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.utilities.Action;
import com.denizenscript.denizen2core.utilities.Function2;
import com.denizenscript.denizen2core.utilities.debugging.ColorSet;

import java.util.HashMap;

public class NullTag extends AbstractTagObject {

    public final static NullTag NULL = new NullTag();

    public final static String STRING_VAL = "&{NULL}";

    // <--[object]
    // @Since 0.3.0
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
        return new TextTag(STRING_VAL);
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        if (data.remaining() > 0 && !data.hasFallback()) {
            data.error.run("Tag " + ColorSet.emphasis + data.bits[data.currentIndex() - 1].key
                    + ColorSet.warning + " returned a NullTag, causing tag "
                    + ColorSet.emphasis + data.bits[data.currentIndex()]
                    + ColorSet.warning + " to fail.");
        }
        return this;
    }

    public static NullTag getFor(Action<String> error, String text) {
        return NULL;
    }

    public static NullTag getFor(Action<String> error, AbstractTagObject text) {
        return NULL;
    }

    @Override
    public String getTagTypeName() {
        return "NullTag";
    }

    @Override
    public String toString() {
        return STRING_VAL;
    }
}
