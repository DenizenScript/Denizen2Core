package org.mcmonkey.denizen2core.arguments;

import org.mcmonkey.denizen2core.DebugMode;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.objects.IntegerTag;
import org.mcmonkey.denizen2core.tags.objects.TextTag;
import org.mcmonkey.denizen2core.utilities.Action;

import java.util.HashMap;

public class TextArgumentBit extends ArgumentBit {

    public TextArgumentBit(String inputText, boolean quoted) {
        AbstractTagObject obj;
        try {
            Long l = Long.parseLong(inputText);
            obj = new IntegerTag(l);
        }
        catch (NumberFormatException ex) {
            obj = new TextTag(inputText);
        }
        value = obj;
        wasQuoted = quoted;
    }

    public final AbstractTagObject value;

    public final boolean wasQuoted;

    @Override
    public String getString() {
        return value.toString();
    }

    public AbstractTagObject parse(HashMap<String, AbstractTagObject> vars, DebugMode mode, Action<String> error) {
        return value;
    }
}
