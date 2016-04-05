package org.mcmonkey.denizen2core.scripts.commontypes;

import org.mcmonkey.denizen2core.scripts.CommandScript;
import org.mcmonkey.denizen2core.utilities.yaml.YAMLConfiguration;

public class TaskScript extends CommandScript {

    public TaskScript(String name, YAMLConfiguration section) {
        super(name, section);
    }

    @Override
    public boolean isExecutable(String section) {
        return !section.contains("constants");
    }
}
