package org.mcmonkey.denizen2core.utilities.debugging;

import org.mcmonkey.denizen2core.Denizen2Core;

/**
 * Quick helper to output debug information.
 */
public class Debug {

    public static void exception(Exception ex) {
        Denizen2Core.getImplementation().outputException(ex);
    }

    public static void error(String message) {
        Denizen2Core.getImplementation().outputError(message);
    }

    public static void info(String message) {
        Denizen2Core.getImplementation().outputInfo(message);
    }
}
