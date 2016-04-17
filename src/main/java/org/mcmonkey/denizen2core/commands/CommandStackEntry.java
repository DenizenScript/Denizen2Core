package org.mcmonkey.denizen2core.commands;

import org.mcmonkey.denizen2core.DebugMode;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.utilities.CoreUtilities;
import org.mcmonkey.denizen2core.utilities.ErrorInducedException;
import org.mcmonkey.denizen2core.utilities.debugging.Debug;

import java.util.HashMap;

/**
 * Represents an entry in a queue's command stack.
 */
public class CommandStackEntry implements Cloneable {

    public final CommandEntry[] entries;

    public final String scriptTitle;

    private int index;

    private DebugMode dbMode = DebugMode.FULL;

    private HashMap<String, AbstractTagObject> definitions = new HashMap<>();

    public void setDefinition(String str, AbstractTagObject obj) {
        definitions.put(CoreUtilities.toLowerCase(str), obj);
    }

    public AbstractTagObject getDefinition(String str) {
        return definitions.get(CoreUtilities.toLowerCase(str));
    }

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
