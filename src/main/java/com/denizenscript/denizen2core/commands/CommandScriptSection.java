package com.denizenscript.denizen2core.commands;

import com.denizenscript.denizen2core.utilities.ErrorInducedException;
import com.denizenscript.denizen2core.utilities.Tuple;
import com.denizenscript.denizen2core.utilities.debugging.ColorSet;
import com.denizenscript.denizen2core.utilities.debugging.Debug;
import com.denizenscript.denizen2core.DebugMode;
import com.denizenscript.denizen2core.scripts.CommandScript;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a section of a script.
 */
public class CommandScriptSection {

    public static List<String> splitSingleLine(String line) {
        boolean quoted = false;
        boolean qtype = false;
        List<String> split = new ArrayList<>();
        int start = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == '\"') {
                if (quoted && !qtype) {
                    quoted = false;
                }
                else if (!quoted) {
                    quoted = true;
                    qtype = false;
                }
            }
            else if (line.charAt(i) == '\'') {
                if (quoted && qtype) {
                    quoted = false;
                }
                else if (!quoted) {
                    quoted = true;
                    qtype = true;
                }
            }
            if (!quoted && line.charAt(i) == '-' && (i == 0 || (line.charAt(i - 1) == ' ' && i + 1 < line.length() && line.charAt(i + 1) == ' '))) {
                String l = line.substring(start, i).trim();
                if (l.length() > 0) {
                    split.add(l);
                }
                start = i + 1;
            }
        }
        String l = line.substring(start, line.length()).trim();
        if (l.length() > 0) {
            split.add(l);
        }
        return split;
    }

    public static Tuple<CommandScriptSection, String> forLine(String line) {
        try {
            List<String> data = splitSingleLine(line);
            CommandEntry[] cmds = new CommandEntry[data.size()];
            for (int i = 0; i < data.size(); i++) {
                cmds[i] = CommandEntry.forLine("<single line>", data.get(i));
            }
            return new Tuple<>(new CommandScriptSection(new CommandStackEntry(cmds, "<single line>", null)), null);
        }
        catch (Exception ex) {
            Debug.error("Compiling script <single line>: ");
            if (ex instanceof ErrorInducedException) {
                Debug.error(ex.getMessage());
                return new Tuple<>(null, ex.getMessage());
            }
            else {
                Debug.exception(ex);
                return new Tuple<>(null, ex.getClass().getCanonicalName() + ": " + ex.getMessage());
            }
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

    public static CommandScriptSection forSection(CommandScript cs, String scriptName, List<Object> lines, DebugMode debugMode) {
        try {
            List<CommandEntry> entries = getEntries(scriptName, lines, 0);
            for (int i = 0; i < entries.size(); i++) {
                entries.get(i).ownIndex = i;
            }
            CommandEntry[] cmds = new CommandEntry[entries.size()];
            cmds = entries.toArray(cmds);
            CommandStackEntry cse = new CommandStackEntry(cmds, scriptName, cs);
            cse.setDebugMode(debugMode);
            return new CommandScriptSection(cse);
        }
        catch (Exception ex) {
            Debug.error("Compiling script '" + ColorSet.emphasis + scriptName + ColorSet.warning + "': ");
            if (ex instanceof ErrorInducedException) {
                Debug.error(ex.getMessage());
            }
            else {
                Debug.exception(ex);
            }
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

    public CommandStackEntry toCSE() {
        return created.clone();
    }
}
