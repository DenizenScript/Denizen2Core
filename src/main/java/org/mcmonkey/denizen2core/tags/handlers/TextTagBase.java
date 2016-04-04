package org.mcmonkey.denizen2core.tags.handlers;

import org.mcmonkey.denizen2core.tags.AbstractTagBase;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.TagData;
import org.mcmonkey.denizen2core.tags.objects.TextTag;

// <--[explanation]
// @Name Text Tags
// @Description
// TextTags are any random text, built into the tag system.
// TODO: Explain better
// TODO: Link tag system explanation
// -->
public class TextTagBase extends AbstractTagBase {

    // <--[tagbase]
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
