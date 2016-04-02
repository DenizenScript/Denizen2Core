package org.mcmonkey.denizen2core.arguments;

public class TextArgumentBit extends ArgumentBit {

    public TextArgumentBit(String inputText) {
        text = inputText;
    }

    private String text;

    @Override
    public String getString() {
        return text;
    }
}
