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
            cmds[0] = CommandEntry.forLine("<single line>", line);
            return new CommandScriptSection(new CommandStackEntry(cmds, "<single line>"));
        }
        catch (Exception ex) {
            Debug.error("Compiling script <single line>: ");
            Debug.exception(ex);
            return null;
        }
    }

    private static List<CommandEntry> getEntries(String scrName, List<Object> lines, int istart) {
        List<CommandEntry> entries = new ArrayList<>();
        for (Object obj : lines) {
            if (obj instanceof String) {
                CommandEntry cent = CommandEntry.forLine(scrName, (String) obj);
                cent.ownIndex = istart;
                entries.add(cent);
                istart++;
            }
            else if (obj instanceof Map) {
                Map map = (Map) obj;
                Object key = map.keySet().iterator().next();
                List<Object> innards = (List<Object>) map.get(key);
                CommandEntry cent = CommandEntry.forLine(scrName, key.toString());
                cent.ownIndex = istart;
                entries.add(cent);
                istart++;
                List<CommandEntry> block = getEntries(scrName, innards, istart);
                cent.blockStart = istart;
                istart += block.size();
                cent.blockEnd = istart - 1;
                List<CommandEntry> toinj = new ArrayList<>(block);
                int bc = block.size();
                cent.command.adaptBlockFollowers(cent, toinj, block);
                istart += (toinj.size() - bc);
                cent.innerCommandBlock = block;
                entries.addAll(toinj);
            }
        }
        return entries;
    }

    public static CommandScriptSection forSection(String scriptName, List<Object> lines, DebugMode debugMode) {
        try {
            List<CommandEntry> entries = getEntries(scriptName, lines, 0);
            for (int i = 0; i < entries.size(); i++) {
                entries.get(i).ownIndex = i;
            }
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
