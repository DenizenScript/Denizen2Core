package org.mcmonkey.denizen2core.arguments;

public class TextArgumentBit extends ArgumentBit {

    public TextArgumentBit(String inputText, boolean quoted) {
        text = inputText;
        wasQuoted = quoted;
    }

    public final String text;

    public final boolean wasQuoted;

    @Override
    public String getString() {
        return text;
    }
}
