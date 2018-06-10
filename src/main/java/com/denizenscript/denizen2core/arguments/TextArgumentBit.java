package com.denizenscript.denizen2core.arguments;

import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.tags.objects.*;
import com.denizenscript.denizen2core.DebugMode;
import com.denizenscript.denizen2core.utilities.Action;

import java.util.HashMap;

public class TextArgumentBit extends ArgumentBit {

    public TextArgumentBit(String inputText, boolean quoted) {
        this(inputText, quoted, true);
    }

    public TextArgumentBit(String inputText, boolean quoted, boolean depthAllowed) {
        wasQuoted = quoted;
        try {
            long l = Long.parseLong(inputText);
            if (String.valueOf(l).equals(inputText)) {
                value = new IntegerTag(l);
                return;
            }
        }
        catch (NumberFormatException ex) {
            // Ignore
        }
        try {
            double d = Double.parseDouble(inputText);
            if (String.valueOf(d).equals(inputText)) {
                value = new NumberTag(d);
                return;
            }
        }
        catch (NumberFormatException ex) {
            // Ignore
        }
        if (depthAllowed) {
            ListTag ltTest = ListTag.getFor(TextArgumentBit::noAction, inputText);
            if (ltTest != null && ltTest.toString().equals(inputText)) {
                value = ltTest;
                return;
            }
            MapTag mtTest = MapTag.getFor(TextArgumentBit::noAction, inputText);
            if (mtTest != null && mtTest.toString().equals(inputText)) {
                value = mtTest;
                return;
            }
        }
        value = new TextTag(inputText);
    }

    public static void noAction(String err) {
        // Ignore
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
