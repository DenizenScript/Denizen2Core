package com.denizenscript.denizen2core.tags;

import com.denizenscript.denizen2core.DebugMode;
import com.denizenscript.denizen2core.arguments.Argument;
import com.denizenscript.denizen2core.arguments.TagBit;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.utilities.Action;

import java.util.HashMap;

public class TagData {

    private int cInd = 0;

    public TagData(Action<String> err, TagBit[] b, Argument fb, HashMap<String, AbstractTagObject> vars, DebugMode dbm,
                   CommandQueue cQueue) {
        bits = b;
        fallback = fb;
        variables = vars;
        dbmode = dbm;
        currentQueue = cQueue;
        error = err;
    }

    public final CommandQueue currentQueue;

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

    public boolean hasNextModifier() {
        return bits[cInd].variable != null && bits[cInd].variable.bits.size() > 0;
    }

    public AbstractTagObject getNextModifier() {
        return bits[cInd].variable.parse(currentQueue, variables, dbmode, error);
    }
}
