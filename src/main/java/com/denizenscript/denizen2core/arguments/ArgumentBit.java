package com.denizenscript.denizen2core.arguments;

import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.DebugMode;
import com.denizenscript.denizen2core.commands.CommandQueue;
import com.denizenscript.denizen2core.utilities.Action;

import java.util.HashMap;

/**
 * Represents a single piece of an argument in a command.
 */
public abstract class ArgumentBit {

    public abstract String getString();

    public abstract AbstractTagObject parse(CommandQueue queue, HashMap<String, AbstractTagObject> vars, DebugMode mode, Action<String> error);
}
