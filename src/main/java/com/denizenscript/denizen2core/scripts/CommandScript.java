package com.denizenscript.denizen2core.scripts;

import com.denizenscript.denizen2core.commands.CommandScriptSection;
import com.denizenscript.denizen2core.utilities.debugging.ColorSet;
import com.denizenscript.denizen2core.utilities.debugging.Debug;
import com.denizenscript.denizen2core.utilities.yaml.StringHolder;
import com.denizenscript.denizen2core.DebugMode;
import com.denizenscript.denizen2core.utilities.CoreUtilities;
import com.denizenscript.denizen2core.utilities.yaml.YAMLConfiguration;

import java.util.HashMap;
import java.util.List;

public abstract class CommandScript {

    public final String title;

    public final YAMLConfiguration contents;

    public long nsUsed = 0;

    public long ticksRan = 0;

    public final HashMap<String, CommandScriptSection> sections = new HashMap<>();

    public CommandScript(String name, YAMLConfiguration section) {
        title = name;
        contents = section;
    }

    // <--[explanation]
// @Since 0.3.0
    // @Name Debug Modes
    // @Group Script Options
    // @Description
    // There are three different debug modes any script can have.
    //
    // FULL: All debug output is shown.
    // MINIMAL: (Recommended!) only errors are shown.
    // NONE: Nothing at all is shown.
    //
    // You can set these in any script container like this example
    // <@code>
    // # Any script name is valid here
    // my_script:
    //   # Any type is valid here
    //   type: task
    //   # Note the "debug: " option, which can be any of the three options specified above.
    //   debug: minimal
    //   # ... contents of the script code here ...
    // <@/code>
    // TODO: Explain better
    // -->

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
                    CommandScriptSection sect = CommandScriptSection.forSection(this, title + "."
                            + strh.str, (List<Object>) obj, getDebugMode());
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

    // <--[explanation]
// @Since 0.3.0
    // @Name Executable Script Section
    // @Group Script Options
    // @Description
    // An executable script section is any part of a script that contains valid Denizen code.
    // This is not determined by the validity of code, but rather by the section title.
    // For the most part, any YAML list is going to be assumed to be an executable script section, and thus be compiled.
    // The exception is keys that start with the word "constant".
    // So if you wish to include non-executable data in your script (EG a constant value for reference),
    // you would generally put all data under the key "constants" or anything else that starts with "constant".
    // If you have a lot of constant data, consider putting it in a 'yaml data' typed script instead.
    // TODO: Link YAML Data script container info.
    // This script type has the special case of /all/ it's data being non-executable, meaning it can be used freely
    // for constant data.
    // TODO: Explain better
    // -->

    public abstract boolean isExecutable(String str);
}
