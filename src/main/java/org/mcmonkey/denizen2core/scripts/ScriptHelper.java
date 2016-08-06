package org.mcmonkey.denizen2core.scripts;

import org.mcmonkey.denizen2core.utilities.CoreUtilities;

import java.util.List;

public class ScriptHelper {

    public static String ClearComments(String input) {
        StringBuilder result = new StringBuilder(input.length());
        List<String> lines = CoreUtilities.split(input.replace("\t", "    ").replace("\r", ""), '\n');
        for (String l: lines) {
            String line = l.trim();
            if (!line.startsWith("#") && !line.equals("}")) {
                String liner = l.replace('\0', ' ').replaceAll("\\s+$", "");
                if (!line.endsWith(":") && line.startsWith("-")) {
                    liner = liner.replace(": ", "<&co> ");
                    liner = liner.replace("#", "<&ns>");
                    if (liner.endsWith(" {")) {
                        liner = liner.substring(0, liner.length() - 2) + ":";
                    }
                }
                result.append(liner).append("\n");
            }
            else {
                result.append("\n");
            }
        }
        result.append("\n");
        return result.toString();
    }
}
