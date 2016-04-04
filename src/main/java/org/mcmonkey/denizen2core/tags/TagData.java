package org.mcmonkey.denizen2core.tags;

import org.mcmonkey.denizen2core.DebugMode;
import org.mcmonkey.denizen2core.arguments.Argument;
import org.mcmonkey.denizen2core.arguments.TagBit;
import org.mcmonkey.denizen2core.utilities.Action;

import java.util.HashMap;

public class TagData {

    private int cInd = 0;

    public TagData(Action<String> err, TagBit[] b, Argument fb, HashMap<String, AbstractTagObject> vars, DebugMode dbm) {
        error = err;
        bits = b;
        fallback = fb;
        variables = vars;
        dbmode = dbm;
    }

    public final Action<String> error;

    public final TagBit[] bits;

    public final Argument fallback;

    public final HashMap<String, AbstractTagObject> variables;

    public final DebugMode dbmode;

    public TagData shrink() {
        cInd++;
        return this;
    }

    public int remaining() {
        return bits.length - cInd;
    }

    public String getNext() {
        return bits[cInd].key;
    }

    public AbstractTagObject getNextModifier() {
        return bits[cInd].variable.parse(variables, dbmode, error);
    }
}
