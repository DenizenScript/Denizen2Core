package org.mcmonkey.denizen2core.commands;

import org.mcmonkey.denizen2core.DebugMode;
import org.mcmonkey.denizen2core.utilities.ErrorInducedException;
import org.mcmonkey.denizen2core.utilities.debugging.Debug;

/**
 * Represents an entry in a queue's command stack.
 */
public class CommandStackEntry implements Cloneable {

    public final CommandEntry[] entries;

    public final String scriptTitle;

    private int index;

    private DebugMode dbMode = DebugMode.FULL;

    public DebugMode getDebugMode() {
        return dbMode;
    }

    public void setDebugMode(DebugMode dbm) {
        dbMode = dbm;
    }

    public void goTo(int location) {
        index = location;
    }

    public CommandStackEntry(CommandEntry[] entriesArray, String scrTitle) {
        entries = entriesArray;
        scriptTitle = scrTitle;
    }

    public CommandStackRetVal run(CommandQueue queue) {
        while (index < entries.length) {
            CommandEntry currentCommand = entries[index];
            index++;
            if (currentCommand.command.isWaitable() && currentCommand.waitFor) {
                queue.waitFor(currentCommand);
            }
            if (getDebugMode().showFull) {
                Debug.info("Script '" + scriptTitle + "' in queue (FILL ME IN) executing command: " + currentCommand.originalLine);
            }
            try {
                currentCommand.command.execute(queue, currentCommand);
            }
            catch (Exception ex) {
                if (!(ex instanceof ErrorInducedException)) {
                    try {
                        queue.handleError(currentCommand, "Internal exception: " + ex.toString());
                    }
                    catch (Exception ex2) {
                        if (dbMode.showMinimal) {
                            Debug.exception(ex2);
                        }
                        index = entries.length + 1;
                        queue.commandStack.clear();
                    }
                }
            }
            if (((queue.getWait() > 0f) || queue.waitingFor() != null)) {
                return CommandStackRetVal.BREAK;
            }
            if (queue.commandStack.size() == 0) {
                return CommandStackRetVal.BREAK;
            }
            if (queue.commandStack.peek() != this) {
                return CommandStackRetVal.CONTINUE;
            }
        }
        if (queue.commandStack.size() > 0) {
            queue.commandStack.pop();
            /*
            if (queue.commandStack.size() > 0 && Determinations != null) {
                queue.setDeterminations(determinations);
                CommandStackEntry tcse = queue.commandStack.peek();
                tcse.variables.set("determinations", new ListTag(Determinations));
            }
            else {
                    queue.setDeterminations(null);
            }
            */
            return CommandStackRetVal.CONTINUE;
        }
        return CommandStackRetVal.STOP;
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

    public enum CommandStackRetVal {
        CONTINUE,
        BREAK,
        STOP
    }
}
