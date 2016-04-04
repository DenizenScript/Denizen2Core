package org.mcmonkey.denizen2core;

import org.mcmonkey.denizen2core.arguments.Argument;
import org.mcmonkey.denizen2core.arguments.TagArgumentBit;
import org.mcmonkey.denizen2core.arguments.TagBit;
import org.mcmonkey.denizen2core.arguments.TextArgumentBit;
import org.mcmonkey.denizen2core.commands.AbstractCommand;
import org.mcmonkey.denizen2core.commands.CommandScriptSection;
import org.mcmonkey.denizen2core.commands.commoncommands.EchoCommand;
import org.mcmonkey.denizen2core.tags.AbstractTagBase;
import org.mcmonkey.denizen2core.tags.handlers.IntegerTagBase;
import org.mcmonkey.denizen2core.tags.handlers.SystemTagBase;
import org.mcmonkey.denizen2core.tags.handlers.TextTagBase;
import org.mcmonkey.denizen2core.utilities.CoreUtilities;
import org.mcmonkey.denizen2core.utilities.debugging.Debug;
import org.mcmonkey.denizen2core.utilities.yaml.YAMLConfiguration;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The main entry class for Denizen2's core engine.
 */
public class Denizen2Core {

    public final static String version;

    static {
        YAMLConfiguration config = null;
        try {
            InputStream is = Denizen2Core.class.getResourceAsStream("/denizen2.yml");
            config = YAMLConfiguration.load(CoreUtilities.streamToString(is));
            is.close();
        }
        catch (Exception ex) {
            Debug.exception(ex);
        }
        if (config == null) {
            version = "UNKNOWN (Error reading version file!)";
        }
        else {
            version = config.getString("VERSION", "UNKNOWN") + " (build " + config.getString("BUILD_NUMBER", "UNKNOWN") + ")";
        }
    }

    private static Denizen2Implementation implementation;

    public final static HashMap<String, AbstractCommand> commands = new HashMap<>();

    public final static HashMap<String, AbstractTagBase> tagBases = new HashMap<>();

    public static Denizen2Implementation getImplementation() {
        return implementation;
    }

    public static void register(AbstractCommand command) {
        commands.put(command.getName(), command);
    }

    public static void register(AbstractTagBase tagbase) {
        tagBases.put(tagbase.getName(), tagbase);
    }

    public static void init(Denizen2Implementation impl) {
        implementation = impl;
        // Common Commands
        register(new EchoCommand());
        // Common Tag Handlers
        register(new SystemTagBase());
        register(new TextTagBase());
        register(new IntegerTagBase());
    }

    public static void runString(String cmd) {
        CommandScriptSection sec = CommandScriptSection.forLine(cmd);
        if (sec != null) {
            sec.toQueue().start();
        }
    }

    public static String escape(String input) {
        return input.replace("<", "\0TAGSTART").replace(">", "\0TAGEND");
    }

    public static String unescape(String input) {
        return input.replace("\0TAGSTART", "<").replace("\0TAGEND", ">");
    }

    public static Argument splitToArgument(String input, boolean wasQuoted) {
        if (input.length() == 0) {
            return new Argument();
        }
        if (input.indexOf('<') < 0) {
            Argument arg = new Argument();
            arg.addBit(new TextArgumentBit(input, wasQuoted));
            return arg;
        }
        Argument arg = new Argument();
        arg.setQuoted(wasQuoted);
        int len = input.length();
        int blocks = 0;
        int brackets = 0;
        StringBuilder blockbuilder = new StringBuilder();
        StringBuilder tbuilder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            char c = input.charAt(i);
            if (c == '<') {
                blocks++;
                if (blocks == 1) {
                    continue;
                }
            }
            else if (c == '>') {
                blocks--;
                if (blocks == 0) {
                    if (tbuilder.length() > 0) {
                        arg.addBit(new TextArgumentBit(tbuilder.toString(), wasQuoted));
                        tbuilder = new StringBuilder();
                    }
                    String value = blockbuilder.toString();
                    String fallback = null;
                    int brack = 0;
                    for (int fb = 1; fb < value.length(); fb++) {
                        if (value.charAt(fb) == '[') {
                            brack++;
                        }
                        if (value.charAt(fb) == ']') {
                            brack--;
                        }
                        if (brack == 0 && value.charAt(fb) == '|' && value.charAt(fb - 1) == '|') {
                            fallback = value.substring(fb + 1);
                            value = value.substring(0, fb);
                            break;
                        }
                    }
                    List<String> split = CoreUtilities.split(value, '.');
                    for (int s = 0; s < split.size(); s++) {
                        split.set(s, split.get(s).replace("&dot", ".").replace("&amp", "&"));
                    }
                    List<TagBit> bits = new ArrayList<>();
                    for (int x = 0; x < split.size(); x++) {
                        String key;
                        Argument variable;
                        if (split.get(x).length() > 1 && split.get(x).contains("[") && split.get(x).charAt(split.get(x).length() - 1) == ']') {
                            int index = split.get(x).indexOf('[');
                            variable = splitToArgument(split.get(x).substring(index + 1, split.get(x).length() - 1), wasQuoted);
                            split.set(x, CoreUtilities.toLowerCase(split.get(x).substring(0, index)));
                            if (split.get(x).length() == 0) {
                                if (x == 0) {
                                    split.set(x, "def");
                                }
                                else {
                                    split.set(x, "get");
                                }
                            }
                        }
                        else {
                            split.set(x, CoreUtilities.toLowerCase(split.get(x)));
                            variable = new Argument();
                        }
                        key = split.get(x);
                        bits.add(new TagBit(key, variable));
                    }
                    TagBit[] tbits = new TagBit[bits.size()];
                    TagArgumentBit tab = new TagArgumentBit(bits.toArray(tbits));
                    if (tab.bits.length > 0) {
                        AbstractTagBase start;
                        tab.setStart(tagBases.get(CoreUtilities.toLowerCase(tab.bits[0].key)));
                    }
                    tab.setFallback(fallback == null ? null : splitToArgument(fallback, false));
                    arg.addBit(tab);
                    blockbuilder = new StringBuilder();
                    continue;
                }
            }
            else if (blocks == 1 && c == '[') {
                brackets++;
            }
            else if (blocks == 1 && c == ']') {
                brackets--;
            }
            if (blocks > 0) {
                switch (c) {
                    case '.':
                        if (blocks > 1 || brackets > 0)
                        {
                            blockbuilder.append("&dot");
                        }
                        else
                        {
                            blockbuilder.append(".");
                        }
                        break;
                    case '&':
                        blockbuilder.append("&amp");
                        break;
                    default:
                        blockbuilder.append(c);
                        break;
                }
            }
            else {
                tbuilder.append(c);
            }
        }
        if (tbuilder.length() > 0) {
            arg.addBit(new TextArgumentBit(tbuilder.toString(), wasQuoted));
        }
        return arg;
    }
}
