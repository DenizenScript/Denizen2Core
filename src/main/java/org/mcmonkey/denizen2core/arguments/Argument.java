package org.mcmonkey.denizen2core.arguments;

import java.util.ArrayList;

/**
 * Represents a single argument in a command.
 */
public class Argument {

    private ArrayList<ArgumentBit> bits = new ArrayList<>();

    public void addBit(ArgumentBit bit) {
        bits.add(bit);
    }

    public String getString() {
        StringBuilder sb = new StringBuilder();
        for (ArgumentBit bit : bits) {
            sb.append(bit.getString());
        }
        return sb.toString();
    }
}
