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
        // <--[tag]
        // @Name BooleanTag.not
        // @Updated 2016/08/26
        // @Group Boolean Logic
        // @ReturnType BooleanTag
        // @Returns whether the opposite of this boolean.
        // @Example "true" .not returns "false".
        // @Example "false" .not returns "true".
        // -->
        handlers.put("not", (dat, obj) -> {
            return new BooleanTag(!((BooleanTag) obj).getInternal());
        });
        // <--[tag]
        // @Name BooleanTag.and[<BooleanTag>]
        // @Updated 2016/08/26
        // @Group Boolean Logic
        // @ReturnType BooleanTag
        // @Returns whether this boolean and the input boolean are both 'true'.
        // @Example "true" .and[true] returns "true".
        // @Example "true" .and[false] returns "false".
        // @Example "false" .and[true] returns "false".
        // @Example "false" .and[false] returns "false".
        // -->
        handlers.put("and", (dat, obj) -> {
            return new BooleanTag(((BooleanTag) obj).getInternal() && BooleanTag.getFor(dat.error, dat.getNextModifier()).getInternal());
        });
        // <--[tag]
        // @Name BooleanTag.or[<BooleanTag>]
        // @Updated 2016/08/26
        // @Group Boolean Logic
        // @ReturnType BooleanTag
        // @Returns whether this boolean or the input boolean are 'true'.
        // @Example "true" .or[true] returns "true".
        // @Example "true" .or[false] returns "true".
        // @Example "false" .or[true] returns "true".
        // @Example "false" .or[false] returns "false".
        // -->
        handlers.put("or", (dat, obj) -> {
            return new BooleanTag(((BooleanTag) obj).getInternal() && BooleanTag.getFor(dat.error, dat.getNextModifier()).getInternal());
        });
        // <--[tag]
        // @Name BooleanTag.xor[<BooleanTag>]
        // @Updated 2016/08/26
        // @Group Boolean Logic
        // @ReturnType BooleanTag
        // @Returns whether this boolean exclusive-or the input boolean are 'true'. Meaning, exactly one of the two must be true, and the other false.
        // @Example "true" .xor[true] returns "false".
        // @Example "true" .xor[false] returns "true".
        // @Example "false" .xor[true] returns "true".
        // @Example "false" .xor[false] returns "false".
        // -->
        handlers.put("xor", (dat, obj) -> {
            return new BooleanTag(((BooleanTag) obj).getInternal() != BooleanTag.getFor(dat.error, dat.getNextModifier()).getInternal());
        });
    }

    public static BooleanTag getFor(Action<String> error, String text) {
        String nt = CoreUtilities.toLowerCase(text);
        if (nt.equals("true")) {
            return new BooleanTag(true);
        }
        if (nt.equals("false")) {
            return new BooleanTag(false);
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
