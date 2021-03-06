package com.denizenscript.denizen2core.scripts;

import com.denizenscript.denizen2core.utilities.CoreUtilities;

import java.util.List;

public class ScriptHelper {

    public static String clearComments(String input) {
        StringBuilder result = new StringBuilder(input.length());
        List<String> lines = CoreUtilities.split(input.replace("\t", "    ").replace("\r", ""), '\n');
        for (String l : lines) {
            String line = l.trim();
            if (!line.startsWith("#") && !line.equals("}")) {
                String liner = l.replace('\0', ' ').replaceAll("\\s+$", "");
                if (!line.endsWith(":") && line.startsWith("-")) {
                    liner = liner.replace(": ", "<unescape[&co]> ");
                    liner = liner.replace("#", "<unescape[&ns]>");
                    if (liner.endsWith(" {")) {
                        liner = liner.substring(0, liner.length() - 2) + ":";
                    }
                }
                else if (line.endsWith(":") && !line.startsWith("-")) {
                    liner = liner.replace("&", "&amp");
                    liner = liner.replace(".", "&dot");
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
