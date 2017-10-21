package com.denizenscript.denizen2core.tags.handlers;

import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.tags.AbstractTagBase;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;

public class FromSavedTagBase extends AbstractTagBase {

    // <--[tagbase]
    // @Since 0.3.0
    // @Base from_saved[<TextTag>]
    // @Group Definitions
    // @ReturnType Dynamic
    // @Returns a "savable" variant of an object identity will be converted to its object form.
    // -->

    @Override
    public String getName() {
        return "from_saved";
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        return Denizen2Core.loadFromSaved(data.error, data.getNextModifier().toString());
    }
}
