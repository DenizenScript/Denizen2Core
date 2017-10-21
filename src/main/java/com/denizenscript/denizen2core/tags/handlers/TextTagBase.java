package com.denizenscript.denizen2core.tags.handlers;

import com.denizenscript.denizen2core.tags.AbstractTagBase;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.objects.TextTag;

public class TextTagBase extends AbstractTagBase {

    // <--[explanation]
// @Since 0.3.0
    // @Name Text Tags
    // @Group Tags
    // @Description
    // TextTags are any random text, built into the tag system.
    // TODO: Explain better
    // TODO: Link tag system explanation
    // -->

    // <--[tagbase]
// @Since 0.3.0
    // @Base text[<TextTag>]
    // @Group Common Base Types
    // @ReturnType TextTag
    // @Returns the input text as a TextTag.
    // <@link explanation Text Tags>What are text tags?<@/link>
    // -->

    @Override
    public String getName() {
        return "text";
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        return TextTag.getFor(data.error, data.getNextModifier()).handle(data.shrink());
    }
}
