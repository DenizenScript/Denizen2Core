package com.denizenscript.denizen2core.arguments;

import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.TagData;
import com.denizenscript.denizen2core.tags.objects.NullTag;
import com.denizenscript.denizen2core.utilities.debugging.ColorSet;
import com.denizenscript.denizen2core.DebugMode;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.tags.AbstractTagBase;
import com.denizenscript.denizen2core.utilities.Action;
import com.denizenscript.denizen2core.utilities.debugging.Debug;

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
        tag.append(bits.length > 0 ? bits[0].toString() : "");
        for (int i = 1; i < bits.length; i++) {
            tag.append(".").append(bits[i].toString());
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
            res = NullTag.NULL;
        }
        else {
            TagData data = new TagData(error, bits, fallback, vars, mode, queue);
            res = start.handle(data);
        }
        if (res instanceof NullTag && fallback != null) {
            res = fallback.parse(queue, vars, mode, error);
        }
        if (mode.showFull) {
            String outp = "Filled tag '" + ColorSet.emphasis + getString() + ColorSet.good
                    + "' with '" + ColorSet.emphasis + res.debug() + ColorSet.good + "'.";
            if (queue != null) {
                queue.outGood(outp);
            }
            else {
                Debug.good(outp);
            }
        }
        return res;
    }
}
