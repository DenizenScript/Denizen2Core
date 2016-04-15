package org.mcmonkey.denizen2core.commands;

import org.mcmonkey.denizen2core.DebugMode;
import org.mcmonkey.denizen2core.utilities.debugging.Debug;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a section of a script.
 */
public class CommandScriptSection {

    public static CommandScriptSection forLine(String line) {
        try {
            CommandEntry[] cmds = new CommandEntry[1];
            cmds[0] = CommandEntry.forLine(line);
            return new CommandScriptSection(new CommandStackEntry(cmds, "<single line>"));
        }
        catch (Exception ex) {
            Debug.error("Compiling script <single line>: ");
            Debug.exception(ex);
            return null;
        }
    }

    private static List<CommandEntry> getEntries(List<Object> lines) {
        List<CommandEntry> entries = new ArrayList<>();
        for (Object obj : lines) {
            if (obj instanceof String) {
                entries.add(CommandEntry.forLine((String) obj));
            }
            else if (obj instanceof Map) {
                Map map = (Map) obj;
                Object key = map.keySet().iterator().next();
                List<Object> innards = (List<Object>) map.get(key);
                List<CommandEntry> inentries = getEntries(innards);
                // TODO: Finish impl. for sub-command sections.
            }
        }
        return entries;
    }

    public static CommandScriptSection forSection(String scriptName, List<Object> lines, DebugMode debugMode) {
        try {
            List<CommandEntry> entries = getEntries(lines);
            CommandEntry[] cmds = new CommandEntry[entries.size()];
            cmds = entries.toArray(cmds);
            CommandStackEntry cse = new CommandStackEntry(cmds, scriptName);
            cse.setDebugMode(debugMode);
            return new CommandScriptSection(cse);
        }
        catch (Exception ex) {
            Debug.error("Compiling script '" + scriptName + "': ");
            Debug.exception(ex);
            return null;
        }
    }

    public final CommandStackEntry created;

    public CommandScriptSection(CommandStackEntry entry) {
        created = entry;
    }

    public CommandQueue toQueue() {
        CommandQueue queue = new CommandQueue();
        CommandStackEntry stackEntry = created.clone();
        queue.commandStack.push(stackEntry);
        return queue;
    }
}
