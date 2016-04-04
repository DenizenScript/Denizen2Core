package org.mcmonkey.denizen2core.arguments;

import org.mcmonkey.denizen2core.DebugMode;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.utilities.Action;

import java.util.HashMap;

/**
 * Represents a single piece of an argument in a command.
 */
public abstract class ArgumentBit {

    public abstract String getString();

    public abstract AbstractTagObject parse(HashMap<String, AbstractTagObject> vars, DebugMode mode, Action<String> error);
}
