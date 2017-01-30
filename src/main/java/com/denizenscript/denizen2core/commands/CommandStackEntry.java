package com.denizenscript.denizen2core.commands;

import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.tags.AbstractTagObject;
import com.denizenscript.denizen2core.utilities.ErrorInducedException;
import com.denizenscript.denizen2core.utilities.debugging.ColorSet;
import com.denizenscript.denizen2core.utilities.debugging.Debug;
import com.denizenscript.denizen2core.DebugMode;
import com.denizenscript.denizen2core.scripts.CommandScript;
import com.denizenscript.denizen2core.utilities.CoreUtilities;

import java.util.HashMap;

/**
 * Represents an entry in a queue's command stack.
 */
public class CommandStackEntry implements Cloneable {

    public final CommandEntry[] entries;

    public final Object[] entryObjects;

    public final String scriptTitle;

    public final CommandScript originalScript;

    private int index;

    private DebugMode dbMode = DebugMode.FULL;

    public HashMap<String, AbstractTagObject> definitions = new HashMap<>();

    public void setDefinition(String str, AbstractTagObject obj) {
        definitions.put(CoreUtilities.toLowerCase(str), obj);
    }

    public boolean hasDefinition(String str) {
        return getDefinition(str) != null;
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

    public int getIndex() {
        return index;
    }

    public CommandStackEntry(CommandEntry[] entriesArray, String scrTitle, CommandScript cs) {
        entries = entriesArray;
        entryObjects = new Object[entries.length];
        scriptTitle = scrTitle;
        originalScript = cs;
    }

    public long lastTickUsed = 0;

    public CommandStackRetVal run(CommandQueue queue) {
        long nsNow = System.nanoTime();
        if (originalScript != null && lastTickUsed != Denizen2Core.currentTick) {
            lastTickUsed = Denizen2Core.currentTick;
            originalScript.ticksRan++;
        }
        while (index < entries.length) {
            CommandEntry currentCommand = entries[index];
            index++;
            if (currentCommand.command.isWaitable() && currentCommand.waitFor) {
                queue.waitFor(currentCommand);
            }
            if (queue.procedural && !currentCommand.command.isProcedural()) {
                queue.handleError(currentCommand, "Tried to run a non-procedural command in a procedural queue!");
                if (originalScript != null) {
                    originalScript.nsUsed += System.nanoTime() - nsNow;
                }
                return CommandStackRetVal.STOP;
            }
            if (getDebugMode().showFull && !currentCommand.originalLine.contains("\0")) {
                String good = "Script '" + ColorSet.emphasis + scriptTitle + ColorSet.good
                        + "' in queue " + ColorSet.emphasis + queue.qID + ColorSet.good
                        + " executing command: " + ColorSet.emphasis + currentCommand.originalLine;
                Debug.good(good);
                if (queue.sender != null) {
                    queue.sender.sendColoredMessage(ColorSet.good + "[Denizen2/Good] " + good);
                }
            }
            try {
                currentCommand.command.execute(queue, currentCommand);
            }
            catch (Exception ex) {
                if (ex instanceof ErrorInducedException) {
                    if (ex.getMessage() != null) {
                        try {
                            queue.handleError("Error running script: " + ex.getMessage());
                        }
                        catch (Exception ex2) {
                            if (!(ex2 instanceof ErrorInducedException) && dbMode.showMinimal) {
                                Debug.exception(ex2);
                            }
                            index = entries.length + 1;
                            queue.commandStack.clear();
                        }
                    }
                }
                else {
                    try {
                        // TODO: System exception event
                        queue.handleError(currentCommand, "Internal exception: " + CoreUtilities.exceptionString(ex));
                    }
                    catch (Exception ex2) {
                        if (!(ex2 instanceof ErrorInducedException) && dbMode.showMinimal) {
                            Debug.exception(ex2);
                        }
                        index = entries.length + 1;
                        queue.commandStack.clear();
                    }
                }
            }
            if ((queue.getWait() > 0f) || queue.waitingFor() != null || queue.paused) {
                if (originalScript != null) {
                    originalScript.nsUsed += System.nanoTime() - nsNow;
                }
                return CommandStackRetVal.BREAK;
            }
            if (queue.commandStack.size() == 0) {
                if (originalScript != null) {
                    originalScript.nsUsed += System.nanoTime() - nsNow;
                }
                return CommandStackRetVal.BREAK;
            }
            if (queue.commandStack.peek() != this) {
                if (originalScript != null) {
                    originalScript.nsUsed += System.nanoTime() - nsNow;
                }
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
            if (originalScript != null) {
                originalScript.nsUsed += System.nanoTime() - nsNow;
            }
            return CommandStackRetVal.CONTINUE;
        }
        if (originalScript != null) {
            originalScript.nsUsed += System.nanoTime() - nsNow;
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
