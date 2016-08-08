package org.mcmonkey.denizen2core.scripts;

import org.mcmonkey.denizen2core.DebugMode;
import org.mcmonkey.denizen2core.commands.CommandScriptSection;
import org.mcmonkey.denizen2core.utilities.CoreUtilities;
import org.mcmonkey.denizen2core.utilities.debugging.ColorSet;
import org.mcmonkey.denizen2core.utilities.debugging.Debug;
import org.mcmonkey.denizen2core.utilities.yaml.StringHolder;
import org.mcmonkey.denizen2core.utilities.yaml.YAMLConfiguration;

import java.util.HashMap;
import java.util.List;

public abstract class CommandScript {

    public final String title;

    public final YAMLConfiguration contents;

    public final HashMap<String, CommandScriptSection> sections = new HashMap<>();

    public CommandScript(String name, YAMLConfiguration section) {
        title = name;
        contents = section;
    }

    public DebugMode getDebugMode() {
        String dbm = CoreUtilities.toUpperCase(contents.getString("debug", "FULL"));
        if (dbm.equals("TRUE")) {
            return DebugMode.FULL;
        }
        else if (dbm.equals("FALSE")) {
            return DebugMode.MINIMAL;
        }
        try {
            return DebugMode.valueOf(dbm);
        }
        catch (IllegalArgumentException ex) {
            Debug.error("'" + ColorSet.emphasis + dbm + ColorSet.warning
                    + "' is not a valid debug mode, defaulting to FULL! Also permitted: NONE, MINIMAL.");
            return DebugMode.FULL;
        }
    }

    public boolean init() {
        for (StringHolder strh : contents.getKeys(true)) {
            Object obj = contents.get(strh.str);
            if (obj instanceof List
                    && isExecutable(strh.low)) {
                try {
                    CommandScriptSection sect = CommandScriptSection.forSection(title + "." + strh.str,
                            (List<Object>) obj, getDebugMode());
                    if (sect == null) {
                        Debug.error("Null script section for script '" + ColorSet.emphasis + title + ColorSet.warning +
                                "', in path '" + ColorSet.emphasis + strh.str + ColorSet.warning + "'.");
                    }
                    sections.put(strh.low, sect);
                }
                catch (Exception ex) {
                    Debug.error("Error in script section for script '" + ColorSet.emphasis + title + ColorSet.warning +
                            "', in path '" + ColorSet.emphasis + strh.str + ColorSet.warning + "'.");
                    Debug.exception(ex);
                    return false;
                }
            }
        }
        return true;
    }

    public abstract boolean isExecutable(String str);
}
