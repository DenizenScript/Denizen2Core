package com.denizenscript.denizen2core.arguments;

import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.objects.IntegerTag;
import com.denizenscript.denizen2core.tags.objects.TextTag;
import com.denizenscript.denizen2core.DebugMode;
import com.denizenscript.denizen2core.utilities.Action;

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

    public AbstractTagObject value;

    public final boolean wasQuoted;

    @Override
    public String getString() {
        return value.toString();
    }

    @Override
    public AbstractTagObject parse(CommandQueue queue, HashMap<String, AbstractTagObject> vars, DebugMode mode, Action<String> error) {
        return value;
    }
}
