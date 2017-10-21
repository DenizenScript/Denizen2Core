package com.denizenscript.denizen2core.tags.handlers;

import com.denizenscript.denizen2core.tags.AbstractTagBase;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.objects.TextTag;

public class SaveTagBase extends AbstractTagBase {

    // <--[tagbase]
// @Since 0.3.0
    // @Base save[<Dynamic>]
    // @Group Definitions
    // @ReturnType TextTag
    // @Returns a "savable" variant of an object identity.
    // -->

    @Override
    public String getName() {
        return "save";
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        return new TextTag(data.getNextModifier().savable());
    }
}
