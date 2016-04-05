package org.mcmonkey.denizen2core.scripts;

import org.mcmonkey.denizen2core.commands.CommandScriptSection;
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

    public boolean init() {
        for (StringHolder strh : contents.getKeys(true)) {
            Object obj = contents.get(strh.str);
            if (obj instanceof List
                    && isExecutable(strh.low)) {
                try {
                    CommandScriptSection sect = CommandScriptSection.forSection(title + "." + strh.str, (List<Object>) obj);
                    if (sect == null) {
                        Debug.error("Null script section for script '" + title + "', in path '" + strh.str + "'.");
                    }
                    sections.put(strh.low, sect);
                }
                catch (Exception ex) {
                    Debug.error("Error in script section for script '" + title + "', in path '" + strh.str + "'.");
                    Debug.exception(ex);
                }
            }
        }
        return false;
    }

    public abstract boolean isExecutable(String str);
}
