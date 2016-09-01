package com.denizenscript.denizen2core.tags.handlers;

import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.AbstractTagBase;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.objects.NullTag;
import com.denizenscript.denizen2core.tags.objects.QueueTag;

public class QueueTagBase extends AbstractTagBase {

    // <--[tagbase]
    // @Base queue[<QueueTag>]
    // @Modifier optional
    // @Group Definitions
    // @ReturnType QueueTag
    // @Returns the input as a queue, or the current queue if none is specified.
    // -->

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public AbstractTagObject handle(TagData data) {
        if (!data.hasNextModifier()) {
            if (data.currentQueue != null) {
                return new QueueTag(data.currentQueue).handle(data.shrink());
            }
            data.error.run("No queue available!");
            return new NullTag();
        }
        return QueueTag.getFor(data.error, data.getNextModifier()).handle(data.shrink());
    }
}
