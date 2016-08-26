package org.mcmonkey.denizen2core.commands;

import org.mcmonkey.denizen2core.Denizen2Core;
import org.mcmonkey.denizen2core.tags.objects.MapTag;
import org.mcmonkey.denizen2core.utilities.Action;
import org.mcmonkey.denizen2core.utilities.ErrorInducedException;
import org.mcmonkey.denizen2core.utilities.debugging.Debug;

import java.util.Stack;

/**
 * Represents a set of executing commands.
 */
public class CommandQueue {

    public final Stack<CommandStackEntry> commandStack = new Stack<>();

    public Action<String> error = this::handleError;

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

    public void start() {
        qID = Denizen2Core.cqID++;
        if (!run(0)) {
            Denizen2Core.queues.add(this);
        }
    }

    public boolean shouldShowError() {
        return commandStack.size() == 0 || commandStack.peek().getDebugMode().showMinimal;
    }

    public boolean shouldShowGood() {
        return commandStack.size() == 0 || commandStack.peek().getDebugMode().showFull;
    }

    public void outGood(String message) {
        if (shouldShowGood()) {
            Debug.good(message);
        }
    }

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
        while (commandStack.size() > 0) {
            currentEntry = commandStack.peek();
            CommandStackEntry.CommandStackRetVal ret = currentEntry.run(this);
            if (ret == CommandStackEntry.CommandStackRetVal.BREAK) {
                return false;
            }
            else if (ret == CommandStackEntry.CommandStackRetVal.STOP) {
                break;
            }
        }
        if (onStop != null) {
            onStop.run(this);
        }
        running = false;
        return true;
    }

    public void stop() {
        commandStack.clear();
    }

    public void handleError(String error) {
        handleError(currentEntry.getIndex() < currentEntry.entries.length ? currentEntry.entries[currentEntry.getIndex()] : null, error);
    }

    public void handleError(CommandEntry entry, String error) {
        String emsg;
        if (entry == null) {
            emsg = "Error in queue " + qID + ", while handling an unknown command: " + error;
        }
        else {
            emsg = "Error in queue " + qID + ", while handling command '" + entry.originalLine + "': " + error;
        }
        // TODO: Error event.
        if (shouldShowError()) {
            Debug.error(emsg);
        }
        stop();
        throw new ErrorInducedException("Stopping queue...");
    }
}
