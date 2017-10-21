package com.denizenscript.denizen2core.scripts.commontypes;

import com.denizenscript.denizen2core.commands.CommandScriptSection;
import com.denizenscript.denizen2core.scripts.CommandScript;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import com.denizenscript.denizen2core.utilities.yaml.YAMLConfiguration;

public class ProcedureScript extends CommandScript {

    // <--[explanation]
// @Since 0.3.0
    // @Name Procedure Scripts
    // @Group Script Types
    // @Description
    // A procedure script is the most basic procedural script in Denizen.
    // It runs its code (usually the "script" section) when told to run it by the
    // <@link tag procedure>procedure<@/link> tag.
    // A procedural script can only calculate things, it cannot execute changes upon the world.
    // -->

    public ProcedureScript(String name, YAMLConfiguration section) {
        super(name, section);
    }

    @Override
    public boolean isExecutable(String section) {
        return !section.startsWith("constant");
    }

    public CommandScriptSection getSection(String name) {
        if (name == null || name.length() == 0) {
            return sections.get("script");
        }
        return sections.get(CoreUtilities.toLowerCase(name));
    }
}
