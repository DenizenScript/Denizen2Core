package com.denizenscript.denizen2core.scripts.commontypes;

import com.denizenscript.denizen2core.events.ScriptEvent;
import com.denizenscript.denizen2core.commands.CommandScriptSection;
import com.denizenscript.denizen2core.scripts.CommandScript;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import com.denizenscript.denizen2core.utilities.yaml.YAMLConfiguration;

public class WorldScript extends CommandScript {

    // <--[explanation]
    // @Since 0.3.0
    // @Name World Scripts
    // @Group Script Types
    // @Description
    // A world script is one of the core executable script types in Denizen.
    // It runs its code beneath the "events" section when told to run it by a script event firing.
    // TODO: Link event system explanation!
    // -->

    public WorldScript(String name, YAMLConfiguration section) {
        super(name, section);
    }

    @Override
    public boolean isExecutable(String section) {
        return !section.startsWith("constant");
    }

    public CommandScriptSection getSection(String name) {
        if (name == null || name.length() == 0) {
            return null;
        }
        return sections.get(CoreUtilities.toLowerCase(name));
    }

    @Override
    public boolean init() {
        if (super.init()) {
            ScriptEvent.currentWorldScripts.add(this);
            return true;
        }
        return false;
    }
}
