package com.denizenscript.denizen2core;

public enum DebugMode {

    FULL(true, true),
    MINIMAL(false, true),
    NONE(false, false);

    DebugMode(boolean a, boolean b) {
        showFull = a;
        showMinimal = b;
    }

    public final boolean showFull;
    public final boolean showMinimal;
}
