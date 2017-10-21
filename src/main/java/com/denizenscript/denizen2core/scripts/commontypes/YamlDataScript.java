package com.denizenscript.denizen2core.scripts.commontypes;

import com.denizenscript.denizen2core.commands.CommandScriptSection;
import com.denizenscript.denizen2core.scripts.CommandScript;
import com.denizenscript.denizen2core.utilities.yaml.YAMLConfiguration;

public class YamlDataScript extends CommandScript {

    // <--[explanation]
    // @Since 0.3.0
    // @Name Yaml Data Scripts
    // @Group Script Types
    // @Description
    // A Yaml Data script contains data and does not execute it. For use with:
    // <@link tag ScriptTag.yaml_key>script.yaml_key[...]<@/link>.
    // -->

    public YamlDataScript(String name, YAMLConfiguration section) {
        super(name, section);
    }

    @Override
    public boolean isExecutable(String section) {
        return false;
    }

    public CommandScriptSection getSection(String name) {
        return null;
    }

    @Override
    public boolean init() {
        return true;
    }
}
