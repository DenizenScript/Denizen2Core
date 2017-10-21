package com.denizenscript.denizen2core.tags.handlers;

import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.objects.TextTag;
import com.denizenscript.denizen2core.tags.AbstractTagBase;

public class UnescapeTagBase extends AbstractTagBase {

    // <--[tagbase]
// @Since 0.3.0
    // @Base unescape[<TextTag>]
    // @Group Escaping
    // @ReturnType TextTag
    // @Returns an unescaped copy of the input text. See <@link explanation Escaping>Escaping<@/link>.
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
