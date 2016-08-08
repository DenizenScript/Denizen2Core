package org.mcmonkey.denizen2core.tags.handlers;

import org.mcmonkey.denizen2core.tags.AbstractTagBase;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.TagData;
import org.mcmonkey.denizen2core.tags.objects.TextTag;

public class UnescapeTagBase extends AbstractTagBase {

    // <--[tagbase]
    // @Base unescape[<TextTag>]
    // @Group Escaping
    // @ReturnType TextTag
    // @Returns an unescaped copy of the input text. See <@link explanation Property Escaping>Property Escaping<@/link>.
    // -->

    @Override
    public String getName() {
        return "unescape";
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        return new TextTag(EscapeTagBase.unescape(data.getNextModifier().toString())).handle(data.shrink());
    }
}
