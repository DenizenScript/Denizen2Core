package org.mcmonkey.denizen2core.tags.objects;

import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.TagData;
import org.mcmonkey.denizen2core.utilities.Action;
import org.mcmonkey.denizen2core.utilities.Function2;

import java.util.HashMap;

public class IntegerTag extends AbstractTagObject {

    // <--[object]
    // @Type IntegerTag
    // @SubType NumberTag
    // @Group Mathematics
    // @Description Represents an integer.
    // @Other note that the number is internally stored as a 64-bit signed integer (a 'long').
    // -->

    private long internal;

    public IntegerTag(long inty) {
        internal = inty;
    }

    public long getInternal() {
        return internal;
    }

    public final static HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> handlers = new HashMap<>();

    static {
    }

    public static IntegerTag getFor(Action<String> error, String text) {
        try {
            Long l = Long.parseLong(text);
            return new IntegerTag(l);
        }
        catch (NumberFormatException ex) {
            error.run("Invalid IntegerTag input!");
            return null;
        }
    }

    public static IntegerTag getFor(Action<String> error, AbstractTagObject text) {
        return (text instanceof IntegerTag) ? (IntegerTag) text : getFor(error, text.toString());
    }

    @Override
    public HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> getHandlers() {
        return handlers;
    }

    @Override
    public AbstractTagObject handleElseCase(TagData data) {
        return new TextTag(toString()).handle(data);
    }

    @Override
    public String toString() {
        return String.valueOf(internal);
    }
}
