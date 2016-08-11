package org.mcmonkey.denizen2core.tags.objects;

import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.TagData;
import org.mcmonkey.denizen2core.utilities.Action;
import org.mcmonkey.denizen2core.utilities.CoreUtilities;
import org.mcmonkey.denizen2core.utilities.Function2;

import java.util.HashMap;

public class BooleanTag extends AbstractTagObject {

    // <--[object]
    // @Type BooleanTag
    // @SubType TextTag
    // @Group Mathematics
    // @Description Represents a true or false value.
    // -->

    private boolean internal;

    public BooleanTag(boolean bo) {
        internal = bo;
    }

    public boolean getInternal() {
        return internal;
    }

    public final static HashMap<String, Function2<TagData, AbstractTagObject, AbstractTagObject>> handlers = new HashMap<>();

    static {
    }

    public static BooleanTag getFor(Action<String> error, String text) {
        String nt = CoreUtilities.toLowerCase(text);
        if (nt.equals("true")) {
            return new BooleanTag(true);
        }
        if (nt.equals("false")) {
            return new BooleanTag(true);
        }
        error.run("Invalid boolean value!");
        return null;
    }

    public static BooleanTag getFor(Action<String> error, AbstractTagObject text) {
        return (text instanceof BooleanTag) ? (BooleanTag) text : getFor(error, text.toString());
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
        return internal ? "true" : "false";
    }
}
