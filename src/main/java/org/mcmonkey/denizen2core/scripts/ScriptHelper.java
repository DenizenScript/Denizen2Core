package org.mcmonkey.denizen2core.scripts;

import org.mcmonkey.denizen2core.utilities.CoreUtilities;

import java.util.List;

public class ScriptHelper {

    public static String ClearComments(String input) {
        StringBuilder result = new StringBuilder(input.length());
        List<String> lines = CoreUtilities.split(input.replace("\t", "    ").replace("\r", ""), '\n');
        for (String l: lines) {
            String line = l.trim();
            if (!line.startsWith("#")) {
                if ((line.startsWith("}") || line.startsWith("{") || line.startsWith("else")) && !line.endsWith(":")) {
                    result.append(' ').append(l.replace('\0', ' ')
                            .replace(": ", "<&co>").replace("#", "<&ns>")).append("\n");
                }
                else {
                    String liner = l.replace('\0', ' ');
                    if (!line.endsWith(":") && line.startsWith("-")) {
                        liner = liner.replace(": ", "<&co> ");
                        liner = liner.replace("#", "<&ns>");
                    }
                    result.append(liner.replace('\0', ' ')).append("\n");
                }
            }
            else {
                result.append("\n");
            }
        }
        result.append("\n");
        return result.toString();
    }
}
