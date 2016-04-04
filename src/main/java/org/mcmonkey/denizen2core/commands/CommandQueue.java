package org.mcmonkey.denizen2core.commands;

import org.mcmonkey.denizen2core.utilities.Action;
import org.mcmonkey.denizen2core.utilities.debugging.Debug;

import java.util.Stack;

/**
 * Represents a set of executing commands.
 */
public class CommandQueue {

    public final Stack<CommandStackEntry> commandStack = new Stack<>();

    public Action<String> error = this::handleError;

    public void start() {
        // TODO: Impl.
        commandStack.peek().run(this);
    }

    public void stop() {
        // TODO: Impl.
    }

    public void handleError(String error) {
        Debug.error(error);
        stop();
    }
}
