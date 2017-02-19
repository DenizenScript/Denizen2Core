package com.denizenscript.denizen2core.tags.handlers;

import com.denizenscript.denizen2core.tags.AbstractTagBase;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.objects.YamlTag;

public class YamlTagBase extends AbstractTagBase {

    // <--[tagbase]
    // @Base yaml[<BooleanTag>]
    // @Group Common Base Types
    // @ReturnType YamlTag
    // @Returns the input as a YamlTag.
    // -->

    @Override
    public String getName() {
        return "yaml";
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        return YamlTag.getFor(data.error, data.getNextModifier()).handle(data.shrink());
    }
}
