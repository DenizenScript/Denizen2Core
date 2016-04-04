package org.mcmonkey.denizen2core.arguments;

import org.mcmonkey.denizen2core.DebugMode;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.objects.TextTag;
import org.mcmonkey.denizen2core.utilities.Action;

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

    public AbstractTagObject parse(HashMap<String, AbstractTagObject> vars, DebugMode mode, Action<String> error) {
        if (bits.size() == 1) {
            return bits.get(0).parse(vars, mode, error);
        }
        StringBuilder sb = new StringBuilder();
        for (ArgumentBit bit : bits) {
            sb.append(bit.parse(vars, mode, error));
        }
        return new TextTag(sb.toString());
    }
}
