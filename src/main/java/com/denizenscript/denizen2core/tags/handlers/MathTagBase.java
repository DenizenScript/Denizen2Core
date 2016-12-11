package com.denizenscript.denizen2core.tags.handlers;

import com.denizenscript.denizen2core.tags.AbstractTagBase;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.objects.NullTag;
import com.denizenscript.denizen2core.tags.objects.NumberTag;
import com.denizenscript.denizen2core.utilities.MathHelper;

import java.util.List;

public class MathTagBase extends AbstractTagBase {

    // <--[tagbase]
    // @Base math[<TextTag>]
    // @Group Common Base Types
    // @ReturnType IntegerTag
    // @Returns the input parsed from textual math statement input.
    // @Note this is a bit slow. Generally, prefer NumberTag math tags, EG <def[myNumber].+[5]>
    // -->

    @Override
    public String getName() {
        return "math";
    }

    // TODO: Preparse this as much as possible!

    @Override
    public AbstractTagObject handle(TagData data) {
        String input = data.getNextModifier().toString();
        StringBuilder err = new StringBuilder();
        List<MathHelper.MathOperation> mops = MathHelper.parse(input, err);
        if (mops == null) {
            data.error.run("Invalid math statement (parsing): " + err.toString());
            return new NullTag();
        }
        String vf = MathHelper.verify(mops, MathHelper.baseFunctions);
        if (vf != null) {
            data.error.run("Invalid math statement (verification): " + vf);
            return new NullTag();
        }
        return new NumberTag(MathHelper.calculate(mops, MathHelper.baseFunctions)).handle(data.shrink());
    }
}
