package com.denizenscript.denizen2core.commands;

import com.denizenscript.denizen2core.Denizen2Core;
import com.denizenscript.denizen2core.tags.objects.MapTag;
import com.denizenscript.denizen2core.utilities.ErrorInducedException;
import com.denizenscript.denizen2core.utilities.debugging.ColorSet;
import com.denizenscript.denizen2core.utilities.debugging.Debug;
import com.denizenscript.denizen2core.utilities.AbstractSender;
import com.denizenscript.denizen2core.utilities.Action;

import java.util.ArrayDeque;

/**
 * Represents a set of executing commands.
 */
public class CommandQueue {

    public final ArrayDeque<CommandStackEntry> commandStack = new ArrayDeque<>();

    public Action<String> error = this::handleError;

    public Action<String> specialErrorHandler = null;

    public Action<CommandQueue> onStop;

    private CommandStackEntry currentEntry = null;

    private CommandEntry waitingOn = null;

    public void waitFor(CommandEntry entry) {
        waitingOn = entry;
    }

    public CommandEntry waitingFor() {
        return waitingOn;
    }

    private double wait = 0;

    public double getWait() {
        return wait;
    }

    public void setWait(double w) {
        wait = w;
    }

    public long qID;

    public boolean running = false;

    public boolean paused = false;

    public boolean procedural = false;

    public MapTag determinations = new MapTag();

    public AbstractSender sender = null;

    public long startTime;

    public long runTime;

    public void start() {
        shouldDebugStart = shouldShowGood();
        startTime = System.currentTimeMillis();
        qID = Denizen2Core.cqID++;
        if (!run(0)) {
            Denizen2Core.queues.add(this); // TODO: Maybe this should be added to the queue list somewhere for lookup reasons before first-run?
        }
    }

    public boolean shouldShowError() {
        return commandStack.size() == 0 || commandStack.peek().getDebugMode().showMinimal;
    }

    public boolean shouldShowGood() {
        return commandStack.size() == 0 || commandStack.peek().getDebugMode().showFull;
    }

    public void outInfo(String message) {
        Debug.info(message);
        if (sender != null) {
            sender.sendColoredMessage(ColorSet.base + "[Denizen2/Info] " + message);
        }
    }

    public void outGood(String message) {
        if (shouldShowGood()) {
            Debug.good(message);
            if (sender != null) {
                sender.sendColoredMessage(ColorSet.good + "[Denizen2/Good] " + message);
            }
        }
    }

    boolean shouldDebugStart = true;

    public boolean run(double delta) {
        running = true;
        if (waitingOn != null) {
            return false;
        }
        if (paused) {
            return false;
        }
        wait -= delta;
        if (wait > 0) {
            return false;
        }
        if (wait < 0) {
            wait = 0;
        }
        long sT = System.currentTimeMillis();
        while (commandStack.size() > 0) {
            currentEntry = commandStack.peek();
            CommandStackEntry.CommandStackRetVal ret = currentEntry.run(this);
            if (ret == CommandStackEntry.CommandStackRetVal.BREAK) {
                runTime += System.currentTimeMillis() - sT;
                return false;
            }
            else if (ret == CommandStackEntry.CommandStackRetVal.STOP) {
                break;
            }
        }
        runTime += System.currentTimeMillis() - sT;
        if (onStop != null) {
            onStop.run(this);
        }
        if (shouldDebugStart) {
            outGood("Took: " + ColorSet.emphasis + ((System.currentTimeMillis() - startTime) / 1000.0)
                    + ColorSet.good + " seconds to run queue " + ColorSet.emphasis + qID
                    + ColorSet.good + ". "
                    + ColorSet.emphasis + (runTime / 1000.0)
                    + ColorSet.good + " seconds were spent in execution.");
        }
        running = false;
        return true;
    }

    public void stop() {
        commandStack.clear();
    }

    public void handleError(String error) {
        handleError(currentEntry != null && (currentEntry.getIndex() > 0 && currentEntry.getIndex() - 1 < currentEntry.entries.length) ?
                currentEntry.entries[currentEntry.getIndex() - 1] : null, error);
    }

    public void handleError(CommandEntry entry, String error) {
        String emsg = "Script error occurred: " + error + "\n  in queue " + ColorSet.emphasis + qID + ColorSet.warning;
        if (currentEntry != null) {
            emsg += ", in script '" + ColorSet.emphasis + currentEntry.scriptTitle + ColorSet.warning + "'";
        }
        else {
            emsg += ", in an unknown script";
        }
        if (entry == null) {
            emsg += ", while handling an unknown command: " + ColorSet.warning;
        }
        else {
            emsg += ", while handling command '" + ColorSet.emphasis + entry.originalLine + ColorSet.warning
                    + "': " + ColorSet.warning;
        }
        if (!commandStack.isEmpty()) {
            commandStack.pop();
            // TODO: Try/catch commands
            while (!commandStack.isEmpty()) {
                CommandStackEntry cse = commandStack.pop();
                int index = cse.getIndex() - 1;
                if (index < 0 || index >= cse.entries.length) {
                    emsg += "\n  while handling an invalid/unknown position within script '"
                            + ColorSet.emphasis + cse.scriptTitle + ColorSet.warning + "'";
                }
                else {
                    CommandEntry cseEntry = cse.entries[index];
                    emsg += "\n  in script '" + ColorSet.emphasis + cse.scriptTitle + ColorSet.warning
                            + "', while handling command '" + ColorSet.emphasis
                            + cseEntry.originalLine + ColorSet.warning + "'";
                }
            }
        }
        if (specialErrorHandler != null) {
            stop();
            throw new ErrorInducedException(emsg);
        }
        // TODO: Error event.
        if (shouldShowError()) {
            Debug.error(emsg);
            if (sender != null) {
                sender.sendColoredMessage(ColorSet.warning + "[Denizen2/Error] " + emsg);
            }
        }
        stop();
        throw new ErrorInducedException(null);
    }
}
