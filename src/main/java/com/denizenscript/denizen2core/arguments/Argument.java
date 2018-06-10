package com.denizenscript.denizen2core.arguments;

import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.objects.TextTag;
import com.denizenscript.denizen2core.DebugMode;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.utilities.Action;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a single argument in a command.
 */
public class Argument {

    public final ArrayList<ArgumentBit> bits = new ArrayList<>();

    private boolean wasQuoted = false;

    public void setQuoted(boolean q) {
        wasQuoted = q;
    }

    public boolean getQuoted() {
        return wasQuoted;
    }

    private boolean quoteMode = false;

    public void setQuoteMode(boolean q) {
        quoteMode = q;
    }

    public boolean getQuoteMode() {
        return quoteMode;
    }

    public void addBit(ArgumentBit bit) {
        bits.add(bit);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ArgumentBit bit : bits) {
            sb.append(bit.getString());
        }
        return sb.toString();
    }

    public AbstractTagObject parse(CommandQueue queue, HashMap<String, AbstractTagObject> vars, DebugMode mode, Action<String> error) {
        if (bits.size() == 1) {
            return bits.get(0).parse(queue, vars, mode, error);
        }
        StringBuilder sb = new StringBuilder();
        for (ArgumentBit bit : bits) {
            sb.append(bit.parse(queue, vars, mode, error).toString());
        }
        return new TextTag(sb.toString());
    }
}
