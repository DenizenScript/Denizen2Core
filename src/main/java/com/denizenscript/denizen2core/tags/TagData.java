package com.denizenscript.denizen2core.tags;

import com.denizenscript.denizen2core.DebugMode;
import com.denizenscript.denizen2core.arguments.Argument;
import com.denizenscript.denizen2core.arguments.TagArgumentBit;
import com.denizenscript.denizen2core.arguments.TagBit;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.tags.objects.NullTag;
import com.denizenscript.denizen2core.utilities.Action;
import com.denizenscript.denizen2core.utilities.debugging.ColorSet;

import java.util.HashMap;

public class TagData {

    private int cInd = 0;

    public TagData(Action<String> err, TagBit[] b, Argument fb, HashMap<String, AbstractTagObject> vars, DebugMode dbm,
                   CommandQueue cQueue, TagArgumentBit tab) {
        bits = b;
        modifiersTracked = new AbstractTagObject[b.length];
        returnsTracked = new AbstractTagObject[b.length];
        fallback = fb;
        variables = vars;
        dbmode = dbm;
        currentQueue = cQueue;
        backingError = err;
        originalTab = tab;
        error = this::handleError;
    }

    public final TagArgumentBit originalTab;

    public final Action<String> backingError;

    public final CommandQueue currentQueue;

    public final Action<String> error;

    public final TagBit[] bits;

    public final AbstractTagObject[] modifiersTracked;

    public final AbstractTagObject[] returnsTracked;

    public final Argument fallback;

    public final HashMap<String, AbstractTagObject> variables;

    public final DebugMode dbmode;

    public String placeMarkedString() {
        StringBuilder tag = new StringBuilder();
        tag.append(ColorSet.emphasis);
        tag.append("<");
        for (int i = 0; i < bits.length; i++) {
            if (i > 0) {
                tag.append(ColorSet.emphasis);
                tag.append(".");
            }
            String tCol;
            if (i >= cInd) {
                tCol = ColorSet.warning;
            }
            else {
                tCol = ColorSet.good;
            }
            tag.append(tCol);
            tag.append(bits[i].key);
            if (bits[i].variable != null && bits[i].variable.bits.size() != 0) {
                tag.append(ColorSet.emphasis);
                tag.append("[");
                tag.append(tCol);
                tag.append(bits[i].variable.toString());
                if (modifiersTracked[i] != null) {
                    tag.append(ColorSet.emphasis);
                    tag.append(" -> ");
                    tag.append(tCol);
                    tag.append(modifiersTracked[i].toString());
                }
                tag.append(ColorSet.emphasis);
                tag.append("]");
            }
            if (returnsTracked[i] != null) {
                tag.append(ColorSet.emphasis);
                tag.append("(returned: ");
                tag.append(tCol);
                tag.append(returnsTracked[i].debug());
                tag.append(ColorSet.emphasis);
                tag.append(")");
            }
        }
        if (fallback != null) {
            tag.append(ColorSet.emphasis);
            tag.append("||").append(fallback.toString());
        }
        tag.append(ColorSet.emphasis);
        tag.append(">");
        return tag.toString();
    }

    public void handleError(String err) {
        backingError.run(ColorSet.warning + "Tag error occurred: " + err + "\n while processing tag "
                + placeMarkedString() + ColorSet.warning);
    }

    public boolean hasFallback() {
        return fallback != null;
    }

    public TagData shrink() {
        cInd++;
        return this;
    }

    public int currentIndex() {
        return cInd;
    }

    public int remaining() {
        return bits.length - cInd;
    }

    public String getNext() {
        return bits[cInd].key;
    }

    public boolean hasNextModifier() {
        return bits[cInd].variable != null && bits[cInd].variable.bits.size() > 0;
    }

    public AbstractTagObject getNextModifier() {
        if (bits[cInd].variable == null) {
            error.run("No tag modifier given when required for tag part '"
                    + ColorSet.emphasis + bits[cInd].key + ColorSet.warning + "'.");
            return NullTag.NULL;
        }
        AbstractTagObject ato = bits[cInd].variable.parse(currentQueue, variables, dbmode, error);
        modifiersTracked[cInd] = ato;
        return ato;
    }
}
