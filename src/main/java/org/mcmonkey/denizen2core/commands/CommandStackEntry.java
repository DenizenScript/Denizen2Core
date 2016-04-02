package org.mcmonkey.denizen2core.commands;

import org.mcmonkey.denizen2core.utilities.debugging.Debug;

/**
 * Represents an entry in a queue's command stack.
 */
public class CommandStackEntry implements Cloneable {

    public final CommandEntry[] entries;

    public CommandStackEntry(CommandEntry[] entriesArray) {
        entries = entriesArray;
    }

    public boolean run(CommandQueue queue) {
        for (int i = 0; i < entries.length; i++) {
            entries[i].command.execute(queue, entries[i]);
        }
        return true;
        // TODO: Impl.
    }

    @Override
    public CommandStackEntry clone() {
        try {
            return (CommandStackEntry) super.clone();
        }
        catch (CloneNotSupportedException ex) {
            // Should never happen.
            Debug.exception(ex);
            return null;
        }
    }
}
