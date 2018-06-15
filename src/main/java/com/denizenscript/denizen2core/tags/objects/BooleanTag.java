package com.denizenscript.denizen2core.tags.objects;

import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.utilities.Function2;
import com.denizenscript.denizen2core.utilities.Action;
import com.denizenscript.denizen2core.utilities.CoreUtilities;

import java.util.HashMap;

public class BooleanTag extends AbstractTagObject {

    // <--[object]
    // @Since 0.3.0
    // @Type BooleanTag
    // @SubType TextTag
    // @Group Mathematics
    // @Description Represents a true or false value. Identified as exactly "true" or "false".
    // -->

    public final static BooleanTag TRUE = new BooleanTag(true);

    public final static BooleanTag FALSE = new BooleanTag(false);

    public static BooleanTag getForBoolean(boolean val) {
        return val ? TRUE : FALSE;
    }

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
        // @Since 0.3.0
        // @Name BooleanTag.not
        // @Updated 2016/08/26
        // @Group Boolean Logic
        // @ReturnType BooleanTag
        // @Returns the opposite of this boolean.
        // @Example "true" .not returns "false".
        // @Example "false" .not returns "true".
        // -->
        handlers.put("not", (dat, obj) -> getForBoolean(!((BooleanTag) obj).getInternal()));
        // <--[tag]
        // @Since 0.3.0
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
            return getForBoolean(((BooleanTag) obj).getInternal() && BooleanTag.getFor(dat.checkedError, dat.getNextModifier()).getInternal());
        });
        // <--[tag]
        // @Since 0.3.0
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
            return getForBoolean(((BooleanTag) obj).getInternal() && BooleanTag.getFor(dat.checkedError, dat.getNextModifier()).getInternal());
        });
        // <--[tag]
        // @Since 0.3.0
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
            return getForBoolean(((BooleanTag) obj).getInternal() != BooleanTag.getFor(dat.checkedError, dat.getNextModifier()).getInternal());
        });
    }

    public static BooleanTag getFor(Action<String> error, String text) {
        String nt = CoreUtilities.toLowerCase(text);
        if (nt.equals("true")) {
            return TRUE;
        }
        if (nt.equals("false")) {
            return FALSE;
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
        return new TextTag(toString());
    }

    @Override
    public String getTagTypeName() {
        return "BooleanTag";
    }

    @Override
    public String toString() {
        return internal ? "true" : "false";
    }
}
