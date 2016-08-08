package org.mcmonkey.denizen2core.arguments;

import org.mcmonkey.denizen2core.DebugMode;
import org.mcmonkey.denizen2core.Denizen2Core;
import org.mcmonkey.denizen2core.commands.CommandQueue;
import org.mcmonkey.denizen2core.tags.AbstractTagBase;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.TagData;
import org.mcmonkey.denizen2core.tags.objects.NullTag;
import org.mcmonkey.denizen2core.utilities.Action;
import org.mcmonkey.denizen2core.utilities.debugging.ColorSet;

import java.util.HashMap;

public class TagArgumentBit extends ArgumentBit {

    public final TagBit[] bits;

    private AbstractTagBase start;

    private Argument fallback;

    public void setStart(AbstractTagBase base) {
        start = base;
    }

    public void setFallback(Argument arg) {
        fallback = arg;
    }

    public TagArgumentBit(TagBit[] b) {
        bits = b;
    }

    @Override
    public String getString() {
        StringBuilder tag = new StringBuilder();
        tag.append("<");
        for (int i = 0; i < bits.length; i++) {
            tag.append(bits[i].toString());
        }
        if (fallback != null) {
            tag.append("||").append(fallback.toString());
        }
        tag.append(">");
        return tag.toString();
    }

    @Override
    public AbstractTagObject parse(CommandQueue queue, HashMap<String, AbstractTagObject> vars, DebugMode mode, Action<String> error) {
        if (start == null && bits.length > 0) {
            start = Denizen2Core.tagBases.get(bits[0].key);
        }
        AbstractTagObject res;
        if (start == null) {
            if (fallback == null) {
                error.run("Invalid tag bits -> empty tag, or invalid base: " + ColorSet.emphasis + (bits.length > 0 ? bits[0] : ""));
            }
            res = new NullTag();
        }
        else {
            TagData data = new TagData(error, bits, fallback, vars, mode, queue);
            res = start.handle(data);
        }
        if (res instanceof NullTag && fallback != null) {
            res = fallback.parse(queue, vars, mode, error);
        }
        if (queue.shouldShowGood()) {
            queue.outGood("Filled tag '" + ColorSet.emphasis + getString() + ColorSet.good
                    + "' with '" + ColorSet.emphasis + res.toString() + ColorSet.good + "'.");
        }
        return res;
    }
}
