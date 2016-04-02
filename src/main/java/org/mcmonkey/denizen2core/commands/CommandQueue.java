package org.mcmonkey.denizen2core.commands;

import java.util.Stack;

/**
 * Represents a set of executing commands.
 */
public class CommandQueue {

    public final Stack<CommandStackEntry> commandStack = new Stack<>();

    public void start() {
        // TODO: Impl.
        commandStack.peek().run(this);
    }
}
