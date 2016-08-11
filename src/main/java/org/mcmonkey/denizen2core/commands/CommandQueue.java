package org.mcmonkey.denizen2core.commands;

import org.mcmonkey.denizen2core.Denizen2Core;
import org.mcmonkey.denizen2core.utilities.Action;
import org.mcmonkey.denizen2core.utilities.ErrorInducedException;
import org.mcmonkey.denizen2core.utilities.debugging.Debug;

import java.util.HashMap;
import java.util.Stack;

/**
 * Represents a set of executing commands.
 */
public class CommandQueue {

    public final Stack<CommandStackEntry> commandStack = new Stack<>();

    public Action<String> error = this::handleError;

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
        if (waitingOn != null) {
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
        return true;
    }

    public void stop() {
        commandStack.clear();
        throw new ErrorInducedException("Stopping queue...");
    }

    public void handleError(String error) {
        String emsg = "Error in queue " + qID + ": " + error;
        // TODO: Error event.
        if (shouldShowError()) {
            Debug.error(emsg);
        }
        stop();
    }

    public void handleError(CommandEntry entry, String error) {
        if (shouldShowError()) {
            Debug.error(error);
        }
        stop();
    }
}
