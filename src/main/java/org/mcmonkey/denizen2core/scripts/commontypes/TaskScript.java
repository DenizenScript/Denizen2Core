package org.mcmonkey.denizen2core.scripts.commontypes;

import org.mcmonkey.denizen2core.commands.CommandScriptSection;
import org.mcmonkey.denizen2core.scripts.CommandScript;
import org.mcmonkey.denizen2core.utilities.CoreUtilities;
import org.mcmonkey.denizen2core.utilities.yaml.YAMLConfiguration;

public class TaskScript extends CommandScript {

    public TaskScript(String name, YAMLConfiguration section) {
        super(name, section);
    }

    @Override
    public boolean isExecutable(String section) {
        return !section.contains("constants");
    }

    public CommandScriptSection getSection(String name) {
        if (name == null || name.length() == 0) {
            return sections.get("script");
        }
        return sections.get(CoreUtilities.toLowerCase(name));
    }
}
