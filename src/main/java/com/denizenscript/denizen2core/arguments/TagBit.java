package com.denizenscript.denizen2core.arguments;

public class TagBit {

    public final String key;

    public final Argument variable;

    public TagBit(String k, Argument var) {
        key = k;
        variable = var;
    }

    @Override
    public String toString() {
        if (variable == null || variable.bits.size() == 0) {
            return key;
        }
        return key + "[" + variable.toString() + "]";
    }
}
